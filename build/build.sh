#!/bin/bash

# run from the project directory (parent directory of 'test' and 'build')

javac -d ./build/ ./src/*
jar cfm ./build/jar/huffman_ADD_VERSION.jar ./build/MANIFEST.MF ./build/src/