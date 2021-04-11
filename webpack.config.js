module.exports = {
	entry: './frontend/src/index.jsx',
	output: {
		path: __dirname + '/frontend/js',
		filename: 'bundle.js'
	},
	devtool: '#sourcemap',
	module: {
		rules: [
	      {
	        test: /\\.css$/,
	        use: [
	          // [style-loader](/loaders/style-loader)
	          { loader: 'style-loader' },
	          // [css-loader](/loaders/css-loader)
	          {
	            loader: 'css-loader',
	            options: {
	              modules: true
	            }
	          },
	          // [sass-loader](/loaders/sass-loader)
	          { loader: 'sass-loader' }
	        ]
	      },
	      {
		      test: /\.js$|jsx/,
		      exclude: /(node_modules|bower_components)/,
		      use: {
		        loader: 'babel-loader',
		        options: {
		          presets: ['@babel/preset-env', "es2015", "react"]
		        }
		      }
		    }
	    ]
	}
}