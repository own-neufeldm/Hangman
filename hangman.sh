#!/bin/bash
#
# Run Hangman.
#
# https://stackoverflow.com/questions/238073/how-to-add-a-progress-bar-to-a-shell-script 
# https://stackoverflow.com/questions/16137713/how-do-i-run-a-java-program-from-the-command-line-on-windows
# https://stackoverflow.com/questions/7509295/noclassdeffounderror-wrong-name

# 
declare __DIR="$(cd "$(dirname "$(readlink -f "${0}")")" && pwd)"

# 
declare __NAME="$(basename "${0}")"

# 
declare SRC_PATH="${__DIR}/src"

# 
declare PACKAGE_NAME="test"

#
declare FILE_NAME="Sandbox"

# ------------------------------------------------------------------------------------------------ #

cd "${SRC_PATH}"
javac "${PACKAGE_NAME}/${FILE_NAME}.java"

while IFS='' read -r LINE; do
  printf 'java: %s\n' "${LINE}"
done < <(java "${PACKAGE_NAME}/${FILE_NAME}")
echo "*** JAVA END ***"

rm "${PACKAGE_NAME}/${FILE_NAME}.class"
