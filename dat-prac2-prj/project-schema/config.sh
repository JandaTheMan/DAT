
# Variables de configuració del projecte.
# IMPORTANT: La variable 'project_dir' es suposa prèviament definida.
#

#
# Ubicació de la biblioteca cgilib i utilitats de la base de dades
# (segons la configuració dels ordinadors del laboratori de DAT)
#
include_dir=/home/janda/Escritorio/ETSETB/DAT/dat-cgilib-0.3.3/src/lib/include
lib_dir=/home/janda/Escritorio/ETSETB/DAT/dat-cgilib-0.3.3/build/lib
lib_name=cgil
dbutil_dir=/home/janda/Escritorio/ETSETB/DAT/dat-cgilib-0.3.3/build/bin

# Flags del compilador depenents de l'arquitectura
ARCH_FLAGS=-m64

#
# Fonts del projecte
#
src_cpp_dir=src/cpp
src_www_dir=src/www

# Noms dels fitxers de codi font (sense el sufix .cpp)
src_basenames="mail model view"

#
# Sortida del projecte
#
build_dir=$project_dir/build
cgi_name=mail.cgi

#
# Directori on s'instal.len l'aplicació i els continguts estàtics
#
install_www_dir=/var/www/html

#
# Ubicació de la base de dades
#
db_root=$install_www_dir/mail-db


