const {Router} = require('express');
const router = Router();

const service = require('axios').create({
  // TODO: Change to internal routing to save cost
  baseURL: 'https://api.munch.app/v0.12.0'
});

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
    data: req.body
  }).then(({data}) => {
    res.json(data);
  }).catch(next)
});

module.exports = router;
