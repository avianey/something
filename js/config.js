import process from 'process'

export default {
  isProduction: process.env.NODE_ENV !== 'development',
  root: rootConfiguration()
}

function rootConfiguration () {
  return {
    apiPath: '/api/v1',
    port: 8000,
    uiPath: ''
  }
}
