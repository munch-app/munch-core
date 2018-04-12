#!/usr/bin/env bash

REMOTE="197547471367.dkr.ecr.ap-southeast-1.amazonaws.com/munch-core/website:production"

docker build -t munch-core/website .
docker tag munch-core/website:latest ${REMOTE}
docker push ${REMOTE}
