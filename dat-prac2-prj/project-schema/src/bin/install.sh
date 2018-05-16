#!/bin/bash

bin_dir=$(dirname $0)
project_dir=$(cd $bin_dir/..; pwd)
source $project_dir/config.sh

if test ! -f $build_dir/bin/$cgi_name; then
    echo "El fitxer '$build_dir/bin/$cgi_name' no existeix. Cal compilar abans."
    exit 1
fi

test -d $install_www_dir || mkdir -p $install_www_dir
chmod 755 $install_www_dir

cp -puRd $src_www_dir/* $install_www_dir
cp -u $build_dir/bin/$cgi_name $install_www_dir
chmod 755 $install_www_dir/$cgi_name


