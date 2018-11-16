#!/usr/bin/env bash

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
