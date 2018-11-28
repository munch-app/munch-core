#!/usr/bin/env bash

# 1 Year
# _nuxt/ auto-versioned
aws s3 cp .nuxt/dist s3://www.munch.app/_nuxt \
                      --acl "public-read" \
                      --cache-control "public, max-age=31536000" \
                      --exclude '*' \
                      --include 'img/*' \
                      --include 'layouts/*' \
                      --include 'pages/*' \
                      --include 'app.*.js' \
                      --include 'manifest.*.js' \
                      --include 'vendor.*.js' \
                      --recursive

# 1 Year
# static/meta self-versioned
aws s3 cp static/meta s3://www.munch.app/meta \
                      --acl "public-read" \
                      --cache-control "public, max-age=31536000" \
                      --recursive

# 1 Day
# favicon.ico
# robots.txt
# browserconfig.xml
aws s3 cp static s3://www.munch.app \
                      --acl "public-read" \
                      --cache-control "public, max-age=86400" \
                      --exclude '*' \
                      --include 'favicon.ico' \
                      --include 'robots.txt' \
                      --include 'browserconfig.xml' \
                      --recursive
