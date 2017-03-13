#!/usr/bin/env bash

# to be called from .travis.yml before running the test cases

docker run --name mysql \
    -e 'MYSQL_ALLOW_EMPTY_PASSWORD=true' -e 'MYSQL_DATABASE=test' \
    -p 3306:3306 \
    -d mysql:latest
