#!/bin/bash

git checkout master
git fetch
git pull

docker stop call-request-service
docker build -t call-request-service:1.0-SNAPSHOT .
docker run -d call-request-service:1.0-SNAPSHOT --name call-request-service