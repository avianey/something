// This file is loaded directly by webpack during the build command,
// so it needs the babel suffix in its filename to be written in ES2016
import HtmlWebpackPlugin from 'html-webpack-plugin'
import { join as joinPaths } from 'path'
import process from 'process'
import { DefinePlugin, optimize } from 'webpack'

export default {
  devtool: 'cheap-module-source-map', // For production: limit size of generated bundle by moving dev information to map files
  entry: ['babel-polyfill', './js/ui/index.js'],
  output: {
    filename: 'bundle.js',
    path: joinPaths(__dirname, '/./packaged'),
    publicPath: '/'
  },
  plugins: [
    // Generate automatically the index.html file that will import our javascript
    new HtmlWebpackPlugin({
      favicon: './js/ui/assets/images/favicon_24px.png',
      template: './js/ui/assets/templates/index.ejs'
    }),
    new DefinePlugin({
      'process.env': {
        'NODE_ENV': JSON.stringify(process.env.NODE_ENV)
      }
    }),
    new optimize.UglifyJsPlugin({
      compress: { warnings: false }
    })
  ],
  module: {
    rules: [
      {
        test: /\.js$/,
        loader: 'babel-loader',
        exclude: /node_modules/,
        query: {
          // This configuration is used by webpack only, i.e. all UI scripts
          // The configuration used for the server scripts is the one inside the .babelrc file
          presets: [ 'es2015', 'react' ]
        }
      },
      {
        test: /\.json$/,
        exclude: /node_modules/,
        loaders: [ 'json-loader', 'strip-json-comments-loader' ]
      },
      {
        test: /\.scss$/,
        loaders: ['style-loader', 'css-loader', 'sass-loader'],
        exclude: /node_modules/
      },
      {
        test: /\.(?:png|gif|jpg|ico|woff2?|svg)$/,
        loader: 'url-loader?limit=8192'
      },
      {
        test: /\.(?:ttf|eot)$/,
        loader: 'file-loader'
      }
    ]
  }
}
