#!/usr/bin/env bash

# to be called from .travis.yml before running the test cases

docker run --name my-sqlserver \
    -e 'ACCEPT_EULA=Y' -e 'SA_PASSWORD=yourStrong(!)Password' \
    -p 1433:1433 -d microsoft/mssql-server-linux