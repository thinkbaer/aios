#!/bin/bash
set -e

if [ "$1" = 'aios' ]; then
    echo "Start aios"
    ./opt/aios-dist/bin/aios.sh -Daios.host=0.0.0.0 -Daios.port=8118
fi

exec "$@"
