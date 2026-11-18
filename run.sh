#!/bin/sh

nginx -g 'daemon off;' &
java -jar calypso-1.0-SNAPSHOT.jar &

wait -n
exit $?

# This scripts starts both python and nginx. If one exits, it kills the other
# one and exits, stopping the container (which could then be restarted)
# I copy what Rasmus wrote
