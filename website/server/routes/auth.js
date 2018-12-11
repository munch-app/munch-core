const {Router} = require('express');
const router = Router();

/**
 * To auto login if user session token expired
 */
// Disabled
// router.use('/', function (req, res, next) {
//   if (!req.cookies.IdToken) return next()
//   if (req.path === '/authenticate') return next()
//
//   const expirationTime = JSON.parse(req.cookies.IdToken).expirationTime
//   if (new Date(expirationTime).getTime() < new Date().getTime()) {
//     return res.redirect('/authenticate?redirect=' + encodeURIComponent(req.url))
//   }
//   return next()
// });


/**
 * Paths that require authentication
 */
['/profile'].forEach(path => {
  router.use(path, function (req, res, next) {
    if (req.cookies.IdToken) return next()
    return res.redirect('/authenticate?redirect=' + encodeURIComponent(req.url))
  })
})

module.exports = router;
