import _ from 'lodash'
import webpack from 'webpack'

import productionConfig from './webpack.config.prod.babel'

export default devConfig()

function devConfig () {
  let config = Object.assign({},
    // Doing a deep clone because we are going to push items in some array properties
    _.cloneDeep(productionConfig),
    // Easier debugging
    {devtool: 'cheap-module-eval-source-map'}
  )

  config.entry.push('webpack-hot-middleware/client')

  // Hot module reload for react needs a custom preset
  config.module.rules.find(item => item.loader === 'babel-loader').query.presets.push('react-hmre')

  config.plugins.push(
    new webpack.HotModuleReplacementPlugin(),
    new webpack.NoErrorsPlugin()
  )
  return config
}
