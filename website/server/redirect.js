// Google's John Mueller said keep 301 for 1 year

// Contents is added in: 2019/08/26
import contents from "./redirects/contents";

const redirects = {
  ...contents
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
