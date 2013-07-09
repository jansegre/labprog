#!/bin/bash

if [ -z "$1" ]
then
	echo "needs an argument"
	exit 1
fi

cp -a skel q$1
