import express from 'express'
import fs from 'fs'
import path from 'path'
import logger from 'winston'

import config from '../config'

export default (() => {
  return new Promise((resolve, reject) => {
    try {
      const app = express()

      if (config.isProduction) {
        const resourcesPath = path.resolve(__dirname, '../../target/build')
        if (!existsSync(resourcesPath)) {
          logger.error(`The resources directory ${resourcesPath} does not exist.`)
          throw new Error('Resources directory not found. Did you think of building the module before starting it ?')
        }
        app.use(config.root.uiPath, express.static(resourcesPath))
      } else {
        // es2015 import/export statements must be at top-level of the script and I couldn't find a working babel plugin for System.import(...).
        // Using require(...) here instead allows to load the two webpack middlewares only in development mode
        // (the dependencies are not even installed in node_modules in production mode).
        const webpack = require('webpack')
        const webpackDevMiddleware = require('webpack-dev-middleware')
        const webpackHotMiddleware = require('webpack-hot-middleware')

        const webpackConfig = require('../../build/webpack.config.dev').default

        const compiler = webpack(webpackConfig)

        // webpack-dev-middleware compiles and serves assets in memory -- nothing is written to the file system.
        app.use(webpackDevMiddleware(compiler, {
          noInfo: true,
          publicPath: '/',
          lazy: false,
          stats: {
            colors: true
          }
        }))
        // Sits on top of webpack-dev-middleware and adds hot-reloading
        app.use(webpackHotMiddleware(compiler))

        app.use('*', function (req, res, next) {
          const filename = path.join(compiler.outputPath, 'index.html')
          compiler.outputFileSystem.readFile(filename, function (err, result) {
            if (err) {
              return next(err)
            }
            res.set('content-type', 'text/html')
            res.send(result)
            res.end()
          })
        })
      }

      app.listen(config.root.port, (err) => {
        if (err) {
          logger.error('server failed to start', err)
          reject(err)
        } else {
          logger.info(`server started successfully. Please connect to http://localhost:${config.root.port}${config.root.uiPath}/index.html`)
          resolve()
        }
      })
    } catch (err) {
      reject(err)
    }
  })
})()

function existsSync (path) {
  try {
    fs.lstatSync(path)
    return true
  } catch (e) {
    return false
  }
}
