const express = require('express');
const app = express();

const bodyParser = require('body-parser');
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: false}));


// Import API Routes
app.use(require('./routes/api'));

// To filter out css.map
app.use(/.*.css.map$/, function (req, res, next) {
  res.end('');
})

module.exports = {
  path: '/',
  handler: app
};
