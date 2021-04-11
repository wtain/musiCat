module.exports = {
	entry: [
		'webpack-dev-server/client/?http://localhost:8080',
		'./frontend/src/index.jsx'
	]
	output: {
		publicPath: 'frontend/js/'
		path: __dirname + '/frontend/js',
		filename: 'bundle.js'
	},
	devtool: '#sourcemap',
	module: {
		loaders: [
			{ test: /\.css$/, loader: 'style-loader!css-loader' },
			{
				test: /\.jsx?$/,
				exclude: /(node-modules)/,
				loaders: ['react-hot-loader', 'babel-loader']
			}
		]
	},
	devServer: {
		hot: true
	},
	plugins: [new webpack.HotModuleReplacementPlugin()]
}