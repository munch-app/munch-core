const express = require('express');
const app = express();

// Import API Routes
app.use(require('./routes/api'));

module.exports = {
  path: '/',
  handler: app
};
