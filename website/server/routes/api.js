// By doing this, instead for CORS, avoid preflight request, cheaper deployment.
const {Router} = require('express')
const router = Router()

// TODO(fuxing): Memory Storage needs to be optimise for massive deployment
const multer = require('multer')
const upload = multer({storage: multer.memoryStorage()})
const FormData = require('form-data')

const service = require('axios').create({
  baseURL: process.env.API_MUNCH_APP
});

service.interceptors.response.use(response => {
  return response
}, error => {
  console.log(error)
  // Error response should be handled by ~/plugins/axios.js
  return error.response
})

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
  }).then(({data}) => {
    res.json(data)
  }).catch(next)
}

['/api/creators/:creatorId/contents/:contentId/images'].forEach(path => {
  router.post(path, upload.single('file'), function (req, res, next) {
    const form = new FormData()
    const file = req.file
    form.append('file', file.buffer, file.originalname);

    route(req, res, next, {headers: form.getHeaders(), data: form})
  })
})

router.post('/api/places/:placeId/suggest/multipart', upload.array('images', 8), function (req, res, next) {
  const form = new FormData()
  const files = req.files

  form.append('json', req.body.json)
  files.forEach(file => {
    form.append('images', file.buffer, file.originalname);
  });

  route(req, res, next, {headers: form.getHeaders(), data: form})
})


router.use('/api', function (req, res, next) {
  route(req, res, next)
})

module.exports = router
