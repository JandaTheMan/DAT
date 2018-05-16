#!/bin/sh

val=$(cat nums)
echo "$(( $val +1 ))" > nums

vis=$(cat nums)

echo "Content-Type: text/html"
echo

echo "<HTML><HEAD>"
echo "<TITLE>Numero visites</TITLE>"
echo "</HEAD>"
echo "<BODY>"
echo "<H1>Numero visites : $vis "
echo "</H1>"
echo "</BODY>"
echo "</HTML>"
	


