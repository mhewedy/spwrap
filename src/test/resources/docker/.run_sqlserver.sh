#!/usr/bin/env bash

# to be called from .travis.yml before running the test cases

docker run --name sqlserver \
    -e 'ACCEPT_EULA=Y' -e 'SA_PASSWORD=yourStrong(!)Password' \
    -p 1434:1433 \
    -d microsoft/mssql-server-linux