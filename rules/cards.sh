#!/bin/sh

for f in `grep [^list] list`
do
	mv "$f.txt" "$f"
done