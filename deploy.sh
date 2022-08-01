#!/bin/bash

git checkout master
git fetch
git pull

docker stop call-request-service
docker build -t call-request-service:1.0-SNAPSHOT .
docker run --name call-request-service -d -p 8080:8080 call-request-service:1.0-SNAPSHOT