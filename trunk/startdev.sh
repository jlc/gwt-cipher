#!/bin/sh

vi -n `find src/ -name "*.java"` `find war/ -name "*.html"` `find war/ -name "*.php"` `find war/ -name "*.css"`

