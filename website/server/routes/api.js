const {Router} = require('express');
const router = Router();

const service = require('axios').create({
  baseURL: process.env.API_MUNCH_APP
});

service.interceptors.response.use(function (response) {
  return response;
}, function (error) {
  let message = error.response &&
    error.response.data &&
    error.response.data.meta &&
    error.response.data.meta.error &&
    error.response.data.meta.error.message
  if (message) {
    return Promise.reject(new Error(message))
  }
  return Promise.reject(error);
});

/**
 * Request:
 * ['task-name-1', 'task-nane-2']
 * Response:
 * {
 * ...}
 */
router.use('/api', function (req, res, next) {
  const localTime = req.headers['User-Local-Time']
  const latLng = req.headers['User-Lat-Lng']
  const headers = {}
  if (localTime) headers['User-Local-Time'] = localTime
  if (latLng) headers['User-Lat-Lng'] = latLng

  service.request({
    url: req.url.replace(/^\/api/, ''),
    params: req.query,
    method: req.method,
    headers: headers,
    data: req.body
  }).then(({data}) => {
    res.json(data);
  }).catch(next)
});

module.exports = router;
