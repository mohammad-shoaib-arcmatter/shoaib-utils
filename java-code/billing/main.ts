import { app, BrowserWindow, dialog } from 'electron'
import { spawn, type ChildProcess } from 'node:child_process'
import { copyFileSync, existsSync, mkdirSync } from 'node:fs'
import { join } from 'node:path'

const backendPort = 18080
const backendUrl = `http://127.0.0.1:${backendPort}`
let backendProcess: ChildProcess | undefined

function javaExecutable(): string {
  const executable = process.platform === 'win32' ? 'java.exe' : 'java'
  return process.env.JAVA_HOME
    ? join(process.env.JAVA_HOME, 'bin', executable)
    : executable
}

function backendJarPath(): string {
  return app.isPackaged
    ? join(process.resourcesPath, 'backend', 'billing-backend-1.0.0.jar')
    : join(app.getAppPath(), 'backend', 'target', 'billing-backend-1.0.0.jar')
}

function prepareDatabasePath(): string {
  const dataDirectory = app.getPath('userData')
  const databasePath = join(dataDirectory, 'billing.db')
  const existingDatabase =
    process.env.BILLING_LEGACY_DB_PATH ?? join(app.getAppPath(), 'billing.db')

  mkdirSync(dataDirectory, { recursive: true })
  if (!existsSync(databasePath) && existsSync(existingDatabase)) {
    copyFileSync(existingDatabase, databasePath)
  }
  return databasePath
}

async function startBackend(): Promise<void> {
  const jarPath = backendJarPath()
  if (!existsSync(jarPath)) {
    throw new Error(`The Java backend JAR was not found at ${jarPath}`)
  }

  backendProcess = spawn(javaExecutable(), ['-jar', jarPath], {
    cwd: app.getPath('userData'),
    env: {
      ...process.env,
      BILLING_DB_PATH: prepareDatabasePath(),
      PORT: String(backendPort),
    },
    stdio: ['ignore', 'pipe', 'pipe'],
  })
  backendProcess.stdout?.on('data', (data: Buffer) => console.log(data.toString()))
  backendProcess.stderr?.on('data', (data: Buffer) => console.error(data.toString()))

  await new Promise<void>((resolve, reject) => {
    let settled = false
    const fail = (error: Error) => {
      if (!settled) {
        settled = true
        reject(error)
      }
    }

    backendProcess?.once('error', fail)
    backendProcess?.once('exit', (code, signal) => {
      fail(new Error(`Java backend exited (code ${code ?? 'none'}, signal ${signal ?? 'none'})`))
    })

    const waitUntilReady = async () => {
      const deadline = Date.now() + 60_000
      while (!settled && Date.now() < deadline) {
        if (backendProcess?.exitCode !== null && backendProcess?.exitCode !== undefined) {
          return
        }
        try {
          const response = await fetch(`${backendUrl}/api/products`)
          if (response.ok) {
            settled = true
            resolve()
            return
          }
        } catch {
          // Connection refusals are expected while Spring Boot is starting.
        }
        await new Promise((wait) => setTimeout(wait, 500))
      }
      fail(new Error('Java backend did not become ready within 60 seconds'))
    }

    void waitUntilReady()
  })
}

function stopBackend(): void {
  backendProcess?.kill()
  backendProcess = undefined
}

async function createWindow(): Promise<void> {
  const window = new BrowserWindow({
    width: 1200,
    height: 800,
    webPreferences: {
      contextIsolation: true,
      nodeIntegration: false,
    },
  })

  if (app.commandLine.hasSwitch('dev')) {
    await window.loadURL('http://localhost:5173')
  } else {
    await window.loadURL(backendUrl)
  }
}

app.whenReady().then(async () => {
  try {
    await startBackend()
    await createWindow()
  } catch (error) {
    const message = error instanceof Error ? error.message : String(error)
    console.error(message)
    dialog.showErrorBox('Billing app could not start', message)
    app.quit()
  }
})

app.on('before-quit', stopBackend)
app.on('window-all-closed', () => {
  if (process.platform !== 'darwin') {
    app.quit()
  }
})
