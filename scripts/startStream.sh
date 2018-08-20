#!/bin/bash
##
## script to start stream sample input data
##
PORT=4444
HOST='localhost'
SECONDS=1
DATA='../inputs/data.json'

##nc -l $HOST $PORT &

while [[ true ]] ; do
      cat inputs/data.json | nc  $HOST $PORT
      sleep $SECONDS
done
