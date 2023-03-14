const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const CopyPlugin = require("copy-webpack-plugin");
const { CleanWebpackPlugin } = require('clean-webpack-plugin');

module.exports = {
  optimization: {
    usedExports: true
  },
  entry: {
    indexPage: path.resolve(__dirname, 'src', 'pages', 'indexPage.js'),
    examplePage: path.resolve(__dirname, 'src', 'pages', 'examplePage.js'),
    recipeGuiPage: path.resolve(__dirname, 'src', 'pages', 'recipeGuiPage.js'),
    recipePage: path.resolve(__dirname, 'src', 'pages', 'recipePage.js'),
  },
  output: {
    path: path.resolve(__dirname, 'dist'),
    filename: '[name].js',
  },
  devServer: {
    https: false,
    port: 8080,
    open: true,
    openPage: 'http://localhost:8080',
    // disableHostChecks, otherwise we get an error about headers and the page won't render
    disableHostCheck: true,
    contentBase: 'packaging_additional_published_artifacts',
    // overlay shows a full-screen overlay in the browser when there are compiler errors or warnings
    overlay: true,
    proxy: [
      {
        context: [
          '/'
        ],
        target: 'http://localhost:5001'
      }
    ]
  },
  plugins: [
    new HtmlWebpackPlugin({
          template: './src/index.html',
          filename: 'index.html',
          inject: false
        }),
    new HtmlWebpackPlugin({
          template: './src/example.html',
          filename: 'example.html',
          inject: false
        }),
    new HtmlWebpackPlugin({
          template: './src/recipeGui.html',
          filename: 'recipeGui.html',
          inject: false
        }),
    new HtmlWebpackPlugin({
          template: './src/recipe.html',
          filename: 'recipe.html',
          inject: false
        }),
    new CopyPlugin({
      patterns: [
        {
          from: path.resolve('src/css'),
          to: path.resolve("dist/css")
        }
      ]
    }),
    new CleanWebpackPlugin()
  ]
}
