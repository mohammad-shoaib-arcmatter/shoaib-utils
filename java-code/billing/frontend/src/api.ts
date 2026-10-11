import type {
  Customer,
  Invoice,
  NewInvoice,
  NewProduct,
  Product,
} from '../../shared/types'

async function request<T>(
  path: string,
  init?: Parameters<typeof fetch>[1],
): Promise<T> {
  const response = await fetch(`/api${path}`, {
    ...init,
    headers: {
      'Content-Type': 'application/json',
      ...init?.headers,
    },
  })
  const responseBody = await response.text()

  if (!response.ok) {
    let message = responseBody || `Request failed with status ${response.status}`
    try {
      const error = JSON.parse(responseBody) as { message?: string; error?: string }
      message = error.message || error.error || message
    } catch {
      // Keep the response text when the server does not return JSON.
    }
    throw new Error(message)
  }

  return responseBody ? (JSON.parse(responseBody) as T) : (undefined as T)
}

export const billingApi = {
  getProducts: () => request<Product[]>('/products'),
  addProduct: (product: NewProduct) =>
    request<void>('/products', {
      method: 'POST',
      body: JSON.stringify(product),
    }),
  deleteProduct: (id: number) =>
    request<void>(`/products/${id}`, { method: 'DELETE' }),
  getCustomers: () => request<Customer[]>('/customers'),
  addCustomer: (customer: Customer) =>
    request<void>('/customers', {
      method: 'POST',
      body: JSON.stringify(customer),
    }),
  searchCustomers: (query: string) =>
    request<Customer[]>(`/customers/search?query=${encodeURIComponent(query)}`),
  createInvoice: (invoice: NewInvoice) =>
    request<void>('/invoices', {
      method: 'POST',
      body: JSON.stringify(invoice),
    }),
  getInvoices: () => request<Invoice[]>('/invoices'),
}
