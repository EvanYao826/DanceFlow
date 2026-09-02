import 'vue-router'

declare module 'vue-router' {
  interface RouteMeta {
    /** 页面标题，用于 document.title */
    title?: string
    requiresAuth?: boolean
    requiresAdmin?: boolean
    guestOnly?: boolean
    portal?: 'user' | 'admin'
  }
}
