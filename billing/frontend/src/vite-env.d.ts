import type { BillingApi } from '../../shared/types'

declare global {
  interface Window {
    api: BillingApi
  }
}

export {}
