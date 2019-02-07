// By doing this, instead for CORS, avoid preflight request, cheaper deployment.
const {Router} = require('express')
const router = Router()
const fs = require('fs')

const multer = require('multer')

// TODO:  Memory Storage needs to be optimise to massive deployment
const upload = multer({storage: multer.memoryStorage()})
const FormData = require('form-data')

const service = require('axios').create({
  baseURL: process.env.API_MUNCH_APP
})

service.interceptors.response.use(response => {
  return response
}, error => {
  // Error response should be handled by ~/plugins/axios.js
  return error.response
})

function getHeaders(req, headers) {
  const authentication = req.headers['authorization']
  if (authentication) headers['Authorization'] = authentication
  return headers
}

router.post('/files/creators/:creatorId/images', upload.single('file'), function (req, res, next) {
  const form = new FormData()
  const file = req.file
  form.append('file', file.buffer, file.originalname);

  service.request({
    url: req.url.replace(/^\/files/, ''),
    params: req.query,
    headers: getHeaders(req, form.getHeaders()),
    method: 'post',
    data: form
  }).then(({data}) => {
    res.json(data)
  }).catch(next)
})

module.exports = router


