#!/bin/bash

bin_dir=$(dirname $0)
project_dir=$(cd $bin_dir/..; pwd)
source $project_dir/config.sh

rm -fR ${build_dir}


