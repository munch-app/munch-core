const {Router} = require('express')
const router = Router()


/**
 * To auto login if user session token expired
 */
router.use('/', function (req, res, next) {
  // Regardless, /me/auth & /me/signout must go through
  if (req.path === '/me/auth') return next()
  if (req.path === '/me/signout') return next()


  // If not logged in
  if (!req.cookies.IdToken) {
    // Me is protected
    if (req.path.startsWith('/me')) {
      return res.redirect('/me/auth?redirect=' + encodeURIComponent(req.url))
    }
    return next()
  }

  // Attempt to check if IdToken has expired
  const expirationTime = JSON.parse(req.cookies.IdToken).expirationTime
  if (new Date(expirationTime).getTime() < new Date().getTime()) {
    return res.redirect('/me/auth?redirect=' + encodeURIComponent(req.url))
  }
  return next()
});

module.exports = router
