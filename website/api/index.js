// TODO(fuxing): Vyro uses this, see if there is any improvements made and migrate, should also move /server to this directory
const service = require('axios').create({
  baseURL: process.env.VYRO_API_URL
})

service.interceptors.response.use(response => {
  return response
}, error => {
  console.log(error)
  // Error response should be handled by ~/plugins/axios.js
  return error.response
})

function getHeaders(req) {
  const authorization = req.headers['authorization']
  const headers = {}

  if (authorization) headers['Authorization'] = authorization
  return headers
}

export default function(req, res, next) {
  service.request({
    url: req.url.replace(/^\/api/, ''),
    params: req.query,
    method: req.method,
    headers: getHeaders(req),
    data: req.body
  }).then(({ data }) => {
    res.json(data)
  }).catch(next)
}
