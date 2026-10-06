export interface Product {
  id: number
  name: string
  hsn: string
  price: number
  gst: number
  stock: number
}

export interface Customer {
  phone: string
  name: string
  gstin: string
  address: string
}

export interface CartItem {
  name: string
  price: number
  qty: number
  gst: number
  hsn: string
}

export interface Invoice {
  id: number
  customer: string
  customer_phone: string
  items: string
  total: number
  gst_total: number
  date: string
}

export interface NewProduct {
  name: string
  hsn: string
  price: number
  gst: number
  stock: number
}

export interface NewInvoice {
  customer: string
  customer_phone: string
  items: CartItem[]
  total: number
  gst_total: number
}

export interface BillingApi {
  getProducts(): Promise<Product[]>
  addProduct(product: NewProduct): Promise<unknown>
  deleteProduct(id: number): Promise<unknown>
  getCustomers(): Promise<Customer[]>
  addCustomer(customer: Customer): Promise<unknown>
  searchCustomers(query: string): Promise<Customer[]>
  createInvoice(invoice: NewInvoice): Promise<unknown>
  getInvoices(): Promise<Invoice[]>
}
