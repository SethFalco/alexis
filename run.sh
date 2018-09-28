#!/usr/bin/env bash
docker build -t alexis -f Dockerfile ./

docker run -it alexis