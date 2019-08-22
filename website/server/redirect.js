// Google's John Mueller said 1 year
const redirects = {
  // E.g. '/from': '/to'
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
