# Billing desktop app

The application uses React and TypeScript for its interface, Electron for its
desktop window, Spring Boot for its local REST API, and SQLite for persistence.
Electron starts the Java backend on `127.0.0.1:18080` and opens the frontend
served by that backend.

## Requirements

- Node.js and npm
- JDK 21 or newer available on `PATH` (or set `JAVA_HOME`)
- Maven 3.6.3 or newer

## Development

From the repository root, install dependencies and start the desktop app:

```sh
npm install
npm install --prefix frontend
npm run dev
```

The React interface runs in Vite with hot reload. Electron starts the Java
backend and opens the Vite development server.

## Build and run

```sh
npm install
npm install --prefix frontend
npm start
```

To build a Windows installer in `release/`, run `npm run package`.

The packaged application requires a Java 21+ runtime on the target computer.
Its SQLite database is stored in Electron's per-user application data directory.
In development, an existing repository-root `billing.db` is copied there on
first launch. To import an existing database into a packaged installation, set
`BILLING_LEGACY_DB_PATH` to the old database file's full path before launching
the app for the first time.
