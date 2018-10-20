const redirects = {
  '/feed': '/feed/images'
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
