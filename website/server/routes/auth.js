const {Router} = require('express');
const router = Router();

/**
 * To auto login if user session token expired
 */
router.use('/', function (req, res, next) {
  // Already doing authenticate, can ignore
  if (!req.url.startsWith('/authenticate') && req.cookies.IdToken) {
    const expirationTime = req.cookies.IdToken && JSON.parse(req.cookies.IdToken).expirationTime
    if (new Date(expirationTime).getTime() < new Date().getTime()) {
      return res.redirect('/authenticate?redirect=' + encodeURIComponent(req.url))
    }
  }
  return next()
});


router.use('/profile', function (req, res, next) {
  if (req.cookies.IdToken) return next()
  return res.redirect('/authenticate')
})

module.exports = router;
