import { contextBridge, ipcRenderer } from 'electron'
import type { BillingApi } from './shared/types'

const api: BillingApi = {
  getProducts: () => ipcRenderer.invoke('get-products'),
  addProduct: (product) => ipcRenderer.invoke('add-product', product),
  deleteProduct: (id) => ipcRenderer.invoke('delete-product', id),
  getCustomers: () => ipcRenderer.invoke('get-customers'),
  addCustomer: (customer) => ipcRenderer.invoke('add-customer', customer),
  searchCustomers: (query) => ipcRenderer.invoke('search-customers', query),
  createInvoice: (invoice) => ipcRenderer.invoke('create-invoice', invoice),
  getInvoices: () => ipcRenderer.invoke('get-invoices'),
}

contextBridge.exposeInMainWorld('api', api)