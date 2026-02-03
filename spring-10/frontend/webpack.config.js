const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');

module.exports = (env, argv) => {
    const isProd = argv.mode === 'production';

    return {
        entry: './src/index.js',

        output: {
            path: path.resolve(__dirname, '../src/main/resources/public'),
            filename: isProd ? 'bundle.[contenthash].js' : 'bundle.js',
            clean: true,
        },

        resolve: {
            extensions: ['.js'],
        },

        module: {
            rules: [
                {
                    test: /\.(js|jsx)$/,
                    exclude: /node_modules/,
                    use: 'babel-loader',
                },
                {
                    test: /\.css$/,
                    use: [
                        'style-loader',
                        'css-loader'
                    ],
                },
            ],
        },

        plugins: [
            new HtmlWebpackPlugin({
                template: './src/index.html',
                filename: 'index.html',
            }),
        ],

        devServer: {
            port: 9000,
            open: true,
            hot: true,
            proxy: {
                '/api': {
                    target: 'http://localhost:8080',
                    changeOrigin: true,
                },
            },
        },

        devtool: isProd ? false : 'source-map',
    };
};
