'use strict'
// Template version: 1.3.1
// see http://vuejs-templates.github.io/webpack for documentation.

var path = require('path')

var assetsRoot = path.resolve(__dirname, '../build/resources/main/static')// <-----1.add
module.exports = {
  dev: {
    // Paths
    assetsSubDirectory: 'assets',// <-----6.change
    assetsPublicPath: '/',
    proxyTable: {// <-----7.change
      '/bill/**':
        {
          // target: 'http://47.106.130.224:8886',//代理前台/api开头的请求，代理到8080端口，spring boot的访问端口
          target: 'http://127.0.0.1:8886',//代理前台/api开头的请求，代理到8080端口，spring boot的访问端口
          changeOrigin: true,
        }
    },

    // Various Dev Server settings
    host: '0.0.0.0', // can be overwritten by process.env.HOST
    port: 3000,// <-----5.change // can be overwritten by process.env.PORT, if port is in use, a free one will be determined
    autoOpenBrowser: false,
    errorOverlay: true,
    notifyOnErrors: true,
    poll: false, // https://webpack.js.org/configuration/dev-server/#devserver-watchoptions-

    
    /**
     * Source Maps
     */

    // https://webpack.js.org/configuration/devtool/#development
    devtool: 'cheap-module-eval-source-map',

    // If you have problems debugging vue-files in devtools,
    // set this to false - it *may* help
    // https://vue-loader.vuejs.org/en/options.html#cachebusting
    cacheBusting: true,

    cssSourceMap: true
  },

  build: {
    // Template for index.html
    index: path.resolve(assetsRoot, 'index.html'),// <-----2.change

    // Paths
    assetsRoot: assetsRoot,// <-----3.change
    assetsSubDirectory: 'assets',// <-----4.change
    assetsPublicPath: '/',

    /**
     * Source Maps
     */

    productionSourceMap: true,
    // https://webpack.js.org/configuration/devtool/#production
    devtool: '#source-map',

    // Gzip off by default as many popular static hosts such as
    // Surge or Netlify already gzip all static assets for you.
    // Before setting to `true`, make sure to:
    // npm install --save-dev compression-webpack-plugin
    productionGzip: false,
    productionGzipExtensions: ['js', 'css'],

    // Run the build command with an extra argument to
    // View the bundle analyzer report after build finishes:
    // `npm run build --report`
    // Set to `true` or `false` to always turn it on or off
    bundleAnalyzerReport: process.env.npm_config_report
  }
}
