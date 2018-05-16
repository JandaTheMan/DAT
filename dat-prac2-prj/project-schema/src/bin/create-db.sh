#!/bin/bash

bin_dir=$(dirname $0)
project_dir=$(cd $bin_dir/..; pwd)
source $project_dir/config.sh

#
# Aquest script crea la base de dades amb les següents taules i inicialitzades amb els següents valors:
#       Taula           Atributs (columnes) / Valors
#       ============    ================================
#       users           id      name            password
#                       --------------------------------
#                       1       usuari1         clau1
#                       2       usuari2         clau2
#                       3       usuari3         clau3
#       ============    ================================
#       messages        id      from    to      date    subject content
#                       -----------------------------------------------
#       ============    ================================
#       msg-seq         value
#                       -----
#                       0
#       ============    ================================
#

$dbutil_dir/db_create $db_root

$dbutil_dir/db_admin $db_root create-table users     id name password                || exit $?
$dbutil_dir/db_admin $db_root create-table messages  id from to date subject content || exit $?
$dbutil_dir/db_admin $db_root create-table msg-seq   value                           || exit $?

$dbutil_dir/db_admin $db_root insert       msg-seq   0               || exit $?

$dbutil_dir/db_admin $db_root insert       users     1 usuari1 clau1 || exit $?
$dbutil_dir/db_admin $db_root insert       users     2 usuari2 clau2 || exit $?
$dbutil_dir/db_admin $db_root insert       users     3 usuari3 clau3 || exit $?

