import Database from 'better-sqlite3'
import type { Customer, Invoice, NewInvoice, NewProduct, Product } from './shared/types'

const db = new Database('billing.db')

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
}

export function getProducts(): Product[] {
  return db.prepare<[], Product>('SELECT * FROM products').all()
}

export function addProduct(product: NewProduct) {
  return db
    .prepare('INSERT INTO products (name,hsn,price,gst,stock) VALUES (?,?,?,?,?)')
    .run(product.name, product.hsn, product.price, product.gst, product.stock)
}

export function deleteProduct(id: number) {
  return db.prepare('DELETE FROM products WHERE id=?').run(id)
}

export function getCustomers(): Customer[] {
  return db.prepare<[], Customer>('SELECT * FROM customers ORDER BY name').all()
}

export function addCustomer(customer: Customer) {
  if (!customer.phone || !customer.name) {
    throw new Error('Phone and Name required')
  }
  return db
    .prepare(
      'INSERT OR REPLACE INTO customers (phone, name, gstin, address) VALUES (?,?,?,?)',
    )
    .run(
      customer.phone,
      customer.name,
      customer.gstin || '',
      customer.address || '',
    )
}

export function searchCustomers(query: string): Customer[] {
  return db
    .prepare<[string, string], Customer>(
      'SELECT * FROM customers WHERE name LIKE ? OR phone LIKE ?',
    )
    .all(`%${query}%`, `%${query}%`)
}

export function createInvoice(invoice: NewInvoice) {
  return db
    .prepare(
      'INSERT INTO invoices (customer, customer_phone, items, total, gst_total, date) VALUES (?,?,?,?,?,?)',
    )
    .run(
      invoice.customer,
      invoice.customer_phone,
      JSON.stringify(invoice.items),
      invoice.total,
      invoice.gst_total,
      new Date().toISOString(),
    )
}

export function getInvoices(): Invoice[] {
  return db.prepare<[], Invoice>('SELECT * FROM invoices ORDER BY id DESC').all()
}