const jwt = require('jsonwebtoken')
const fs = require('fs')
const {Router} = require('express')
const router = Router()

const key = fs.readFileSync('./AuthKey_9YDAGN8AH2.p8')
const origin = process.env.ORIGIN

router.get('/apple-maps/token', function (req, res, next) {
  const payload = {
    iss: '7G635T5DK8',
    iat: Date.now() / 1000,
    exp: (Date.now() / 1000) + 30 * 60,
    origin
  }

  const header = {
    kid: '9YDAGN8AH2',
    typ: 'JWT',
    alg: 'ES256'
  }

  return res.end(jwt.sign(payload, key, {header}))
})

module.exports = router;
