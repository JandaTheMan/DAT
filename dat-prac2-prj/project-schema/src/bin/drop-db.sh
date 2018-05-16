#!/bin/bash

bin_dir=$(dirname $0)
project_dir=$(cd $bin_dir/..; pwd)
source $project_dir/config.sh

#
# Aquest script elimina la base de dades (i totes les seves taules) creada amb el script 'create-db.sh'.
#

$dbutil_dir/db_admin $db_root drop-table   users
$dbutil_dir/db_admin $db_root drop-table   messages
$dbutil_dir/db_admin $db_root drop-table   msg-seq

$dbutil_dir/db_drop $db_root


