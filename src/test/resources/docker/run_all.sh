#!/usr/bin/env bash

`dirname $0`/.run_mysql.sh
`dirname $0`/.run_oracle.sh
`dirname $0`/.run_sqlserver.sh

docker ps
