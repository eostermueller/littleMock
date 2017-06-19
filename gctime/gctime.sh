#!/bin/sh
dir=$(dirname "$0")

#This script takes 1 parameter -- the process ID of the Java process for which you want to monitor GC
export PID=$1
jstat -gc $PID 1s | awk -f $dir/gctime.awk
