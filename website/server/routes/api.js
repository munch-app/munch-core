const {Router} = require('express');
const router = Router();

const service = require('axios').create({
  baseURL: process.env.API_MUNCH_APP
});

service.interceptors.response.use(function (response) {
  return response;
}, function (error) {
  let metaError = error.response &&
    error.response.data &&
    error.response.data.meta &&
    error.response.data.meta.error
  if (metaError && metaError.type) {
    return Promise.reject(new Error(`${metaError.type}: ${metaError.message || ''}`))
  }
  return Promise.reject(error);
});

function getHeaders(req) {
  const localTime = req.headers['user-local-time']
  const latLng = req.headers['user-lat-lng']
  const authentication = req.headers['authorization']
  const headers = {}

  if (localTime) headers['User-Local-Time'] = localTime
  if (latLng) headers['User-Lat-Lng'] = latLng
  if (authentication) headers['Authorization'] = authentication

  return headers
}

/**
 * Request:
 * ['task-name-1', 'task-nane-2']
 * Response:
 * {
 * ...}
 */
router.use('/api', function (req, res, next) {
  service.request({
    url: req.url.replace(/^\/api/, ''),
    params: req.query,
    method: req.method,
    headers: getHeaders(req),
    data: req.body
  }).then(({data}) => {
    res.json(data);
  }).catch(next)
});

module.exports = router;
