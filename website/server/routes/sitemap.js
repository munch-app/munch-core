const {Router} = require('express');
const router = Router();
const request = require('request');

/**
 * Request:
 * ['task-name-1', 'task-nane-2']
 * Response:
 * {
 * ...}
 */
router.get('/sitemap/:file(*)', function (req, res, next) {
  const file = req.params.file
  const url = `https://s3.dualstack.ap-southeast-1.amazonaws.com/www.munch.app-sitemap/${file}`
  request.get(url).pipe(res)
});

module.exports = router;
