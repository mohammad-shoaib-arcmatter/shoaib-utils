import Database from 'better-sqlite3'
import type { Customer, Invoice, NewInvoice, NewProduct, Product } from './shared/types'

const db = new Database('billing.db')
let statements: ReturnType<typeof prepareStatements> | undefined

function prepareStatements() {
  return {
    getProducts: db.prepare<[], Product>('SELECT * FROM products'),
    addProduct: db.prepare<[string, string, number, number, number]>(
      'INSERT INTO products (name,hsn,price,gst,stock) VALUES (?,?,?,?,?)',
    ),
    deleteProduct: db.prepare<[number]>('DELETE FROM products WHERE id=?'),
    getCustomers: db.prepare<[], Customer>(
      'SELECT * FROM customers ORDER BY name',
    ),
    addCustomer: db.prepare<[string, string, string, string]>(
      'INSERT OR REPLACE INTO customers (phone, name, gstin, address) VALUES (?,?,?,?)',
    ),
    searchCustomers: db.prepare<[string, string], Customer>(
      'SELECT * FROM customers WHERE name LIKE ? OR phone LIKE ?',
    ),
    createInvoice: db.prepare<[string, string, string, number, number, string]>(
      'INSERT INTO invoices (customer, customer_phone, items, total, gst_total, date) VALUES (?,?,?,?,?,?)',
    ),
    getInvoices: db.prepare<[], Invoice>(
      'SELECT * FROM invoices ORDER BY id DESC',
    ),
  }
}

function getStatements(): ReturnType<typeof prepareStatements> {
  if (!statements) {
    throw new Error('Database has not been initialized')
  }
  return statements
}

export function initDB(): void {
  db.exec(`
    CREATE TABLE IF NOT EXISTS products (
      id INTEGER PRIMARY KEY AUTOINCREMENT,
      name TEXT, hsn TEXT, price REAL, gst REAL, stock INTEGER
    );
    CREATE TABLE IF NOT EXISTS customers (
      phone TEXT PRIMARY KEY,
      name TEXT NOT NULL, 
      gstin TEXT, 
      address TEXT
    );
    CREATE TABLE IF NOT EXISTS invoices (
      id INTEGER PRIMARY KEY AUTOINCREMENT,
      customer TEXT, customer_phone TEXT, items TEXT, total REAL, gst_total REAL, date TEXT
    );
  `)
  statements = prepareStatements()
}

export function getProducts(): Product[] {
  return getStatements().getProducts.all()
}

export function addProduct(product: NewProduct) {
  return getStatements().addProduct.run(
    product.name,
    product.hsn,
    product.price,
    product.gst,
    product.stock,
  )
}

export function deleteProduct(id: number) {
  return getStatements().deleteProduct.run(id)
}

export function getCustomers(): Customer[] {
  return getStatements().getCustomers.all()
}

export function addCustomer(customer: Customer) {
  if (!customer.phone || !customer.name) {
    throw new Error('Phone and Name required')
  }
  return getStatements().addCustomer.run(
    customer.phone,
    customer.name,
    customer.gstin || '',
    customer.address || '',
  )
}

export function searchCustomers(query: string): Customer[] {
  return getStatements().searchCustomers.all(`%${query}%`, `%${query}%`)
}

export function createInvoice(invoice: NewInvoice) {
  return getStatements().createInvoice.run(
    invoice.customer,
    invoice.customer_phone,
    JSON.stringify(invoice.items),
    invoice.total,
    invoice.gst_total,
    new Date().toISOString(),
  )
}

export function getInvoices(): Invoice[] {
  return getStatements().getInvoices.all()
}