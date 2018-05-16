#!/bin/bash

bin_dir=$(dirname $0)
project_dir=$(cd $bin_dir/..; pwd)
source $project_dir/config.sh

# Opcions del compilador/enllaçador de GNU/Linux (programa c++):
# -Wall         Notifica tots els avisos
# -std=c++11    Usa l'estàndar C++11 (IMPORTANT)
# -m64          Genera per a arquitectura de 64bits
# -Idir         Directori on busca els 'includes' (fitxers de capçalera C/C++)
# -Ldir         Directori on busca les biblioteques
# -lname        Nom de biblioteca
# -c            No enllaça (només compila)
# -o file       Fitxer de sortida
CXXFLAGS="-Wall -std=c++11 -I$include_dir $ARCH_FLAGS"
LDFLAGS="-L$lib_dir $ARCH_FLAGS"
LDLIBS="-l$lib_name"

# Compila fonts
test -d $build_dir/obj || mkdir -p $build_dir/obj
OBJS=
for n in $src_basenames; do
    out=$build_dir/obj/$n.o
    c++ $CXXFLAGS -c -o $out $src_cpp_dir/$n.cpp || exit $?
    OBJS="$OBJS $out"
done

# Enllaça compilats i biblioteca
test -d $build_dir/bin || mkdir -p $build_dir/bin
c++ $LDFLAGS -o $build_dir/bin/$cgi_name $OBJS $LDLIBS


