const {Router} = require('express');
const router = Router();

router.use('/', function (req, res, next) {
  // Already doing authenticate, can ignore
  if (req.url !== '/authenticate' && req.cookies.IdToken) {
    const expirationTime = req.cookies.IdToken && JSON.parse(req.cookies.IdToken).expirationTime
    if (new Date(expirationTime).getTime() < new Date().getTime()) {
      // Expired
      return res.redirect('/authenticate')
    }
  }

  return next()
});

// Future: to server side session to allow longer then 1 hour app session

router.use('/profile', function (req, res, next) {
  if (req.cookies.IdToken) return next()
  return res.redirect('/authenticate')
})

module.exports = router;
