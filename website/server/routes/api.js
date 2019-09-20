// By doing this, instead for CORS, avoid preflight request, cheaper deployment.
const {Router} = require('express')
const router = Router()

const service = require('axios').create({
  baseURL: process.env.API_MUNCH_APP || 'https://api.munch.app/'
});

function getHeaders(req) {
  const latLng = req.headers['local-lat-lng']
  const zoneId = req.headers['local-zone-id']
  const authentication = req.headers['authorization']
  const contentType = req.headers['content-type']
  const headers = {}

  if (latLng) headers['Local-Lat-Lng'] = latLng
  if (zoneId) headers['Local-Zone-Id'] = zoneId
  if (authentication) headers['Authorization'] = authentication
  if (contentType) headers['Content-Type'] = contentType
  return headers
}

function route(req, res, next, options) {
  const optionalHeaders = options && options.headers || {}
  const optionalData = options && options.data

  service.request({
    url: req.url.replace(/^\/api/, ''),
    params: req.query,
    method: req.method,
    headers: {...getHeaders(req), ...optionalHeaders},
    data: optionalData || req.body
  }).then(({data, status}) => {
    res.status(status)
    res.json(data)
  }).catch(reason => {
    if (reason.response) {
      const {status, data} = reason.response
      res.status(status)
      res.json(data)
    } else {
      next(reason)
    }
  })
}

router.use('/api', function (req, res, next) {
  route(req, res, next)
})

module.exports = router
