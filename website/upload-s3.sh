#!/usr/bin/env bash

aws s3 sync /usr/src/app/_nuxt/ s3://www.munch.app/_nuxt/ --acl "public-read" --cache-control "public, max-age=31536000"
