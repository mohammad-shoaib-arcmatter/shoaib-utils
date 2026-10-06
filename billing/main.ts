import { app, BrowserWindow, ipcMain } from 'electron'
import type { IpcMainInvokeEvent } from 'electron'
import { join } from 'node:path'
import type { Customer, NewInvoice, NewProduct } from './shared/types'
import {
  addCustomer,
  addProduct,
  createInvoice,
  deleteProduct,
  getCustomers,
  getInvoices,
  getProducts,
  initDB,
  searchCustomers,
} from './database'

app.whenReady().then(async () => {
  initDB()
  const win = new BrowserWindow({
    width: 1200,
    height: 800,
    webPreferences: {
      preload: join(__dirname, 'preload.js'),
      contextIsolation: true,
      nodeIntegration: false,
    },
  })
  if (app.commandLine.hasSwitch('dev')) {
    await win.loadURL('http://localhost:5173')
  } else {
    await win.loadFile(join(__dirname, '../frontend/dist/index.html'))
  }
})

ipcMain.handle('get-products', () => getProducts())
ipcMain.handle('add-product', (_event: IpcMainInvokeEvent, product: NewProduct) =>
  addProduct(product),
)
ipcMain.handle(
  'create-invoice',
  (_event: IpcMainInvokeEvent, invoice: NewInvoice) => createInvoice(invoice),
)
ipcMain.handle('get-invoices', () => getInvoices())
ipcMain.handle(
  'delete-product',
  (_event: IpcMainInvokeEvent, id: number) => deleteProduct(id),
)
ipcMain.handle('get-customers', () => getCustomers())
ipcMain.handle(
  'add-customer',
  (_event: IpcMainInvokeEvent, customer: Customer) => addCustomer(customer),
)
ipcMain.handle(
  'search-customers',
  (_event: IpcMainInvokeEvent, query: string) => searchCustomers(query),
)