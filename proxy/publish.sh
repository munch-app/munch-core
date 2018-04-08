#!/usr/bin/env bash

REMOTE="197547471367.dkr.ecr.ap-southeast-1.amazonaws.com/munch-core/proxy:production"

docker build -t munch-core/proxy .
docker tag munch-core/proxy:latest ${REMOTE}
docker push ${REMOTE}
