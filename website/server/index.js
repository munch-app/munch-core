require('dotenv').config()
const express = require('express');
const app = express();

const bodyParser = require('body-parser');
const cookieParser = require('cookie-parser')

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: false}));
app.use(cookieParser())

// Import API Routes
app.use(require('./routes/api'))
app.use(require('./routes/auth'))
app.use(require('./routes/apple-maps'))

// To filter out css.map
app.use(/.*.css.map$/, function (req, res, next) {
  res.end('');
})

module.exports = {
  path: '/',
  handler: app
};
