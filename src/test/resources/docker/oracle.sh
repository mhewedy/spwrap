#!/usr/bin/env bash

# to be called from .travis.yml before running the test cases

docker run --name oracle \
    -e 'ORACLE_ALLOW_REMOTE=true' \
    -p 1521:1521 \
    -d wnameless/oracle-xe-11g
