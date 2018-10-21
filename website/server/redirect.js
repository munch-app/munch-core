const redirects = {
  '/feed': '/feed/images',
  '/support/terms-of-use': '/support/terms',
  '/support/privacy-policy': '/support/privacy',
}

module.exports = function (req, res, next) {
  const to = redirects[req.url]
  if (to) {
    res.writeHead(301, {Location: to})
    res.end()
  } else {
    next()
  }
}
