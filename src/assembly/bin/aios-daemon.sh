#!/bin/sh
#
# /etc/init.d/aios-server -- startup script for Waia
#
# Written by Robert Kania <robert.kania@thinkbaer.de>.
#
# !!Attention!!: This script must be placed under AIOS_PATH/bin
# and a symbolic link can be created unter /etc/init.d/aios-server
#
### BEGIN INIT INFO
# Provides:          waia
# Required-Start:    $all
# Required-Stop:     $network $remote_fs $named
# Default-Start:     2 3 4 5
# Default-Stop:      0 1 6
# Short-Description: Starts aios-server
# Description:       Starts aios-server using start-stop-daemon
### END INIT INFO

PATH=/bin:/usr/bin:/sbin:/usr/sbin
NAME=aios
DESC="Aios server"



CURRENT_SCRIPT=$(readlink -f "$0")
CURRENT_DIR=$(dirname "$CURRENT_SCRIPT")
CURRENT_DATE=`date +'%Y%m%d'`

. /lib/lsb/init-functions

if [ -r /etc/default/rcS ]; then
	. /etc/default/rcS
fi


if [ "$(id -u)" != "0" ]; then
   echo "This script must be run as root" 1>&2
   exit 1
fi


# Java home
JDK_DIRS="/usr/lib/jvm/java-8-oracle /usr/lib/jvm/java-8-openjdk /usr/lib/jvm/java-8-openjdk-amd64 /usr/lib/jvm/java-8-openjdk-armhf /usr/lib/jvm/java-8-openjdk-i386"

JAVA_HOME=""
# Look for the right JVM to use
for jdir in $JDK_DIRS; do
    if [ -r "$jdir/bin/java" -a -z "${JAVA_HOME}" ]; then
        JAVA_HOME="$jdir"
    fi
done

export JAVA_HOME

checkJava() {
	if [ -x "$JAVA_HOME/bin/java" ]; then
		JAVA="$JAVA_HOME/bin/java"
	else
		JAVA=`which java`
	fi

	if [ ! -x "$JAVA" ]; then
		echo "Could not find any executable java binary. Please install java in your PATH or set JAVA_HOME"
		exit 1
	fi
}


if [ -z "$AIOS_USER" ]; then
    AIOS_USER=root
fi

if [ -z "$AIOS_GROUP" ]; then
    AIOS_GROUP=root
fi



USER_DIR=$CURRENT_DIR/..
USER_DIR=`cd "$USER_DIR"; pwd`
LIB_PATH=$USER_DIR/libs
AIOS_CLASSPATH=""

for jar in $LIB_PATH/*.jar
do
  if [ "X$AIOS_CLASSPATH" = "X" ]; then
    AIOS_CLASSPATH=$jar
  else
    AIOS_CLASSPATH=$jar:$AIOS_CLASSPATH
  fi
done

MAIN_CLASS="de.thinkbaer.aios.server.ServerMain"


SYS_ARGS=""
ARGS=""


for i in $@
do    
    if echo "$i" | grep -q "^\-X"; then
	SYS_ARGS="$SYS_ARGS $i"
    else
	if echo "$i" | grep -q "^\-D"; then
	    SYS_ARGS="$SYS_ARGS $i"
	else
	    ARGS="$ARGS $i"
	fi
    fi
done

JAVA_OPTS=""
JAVA_OPTS="$JAVA_OPTS -Xms32m"
JAVA_OPTS="$JAVA_OPTS -Xmx512m"
JAVA_OPTS="$JAVA_OPTS -XX:+UseParallelGC"
JAVA_OPTS="$JAVA_OPTS -XX:MaxHeapFreeRatio=75 -XX:MinHeapFreeRatio=25"
JAVA_OPTS="$JAVA_OPTS -XX:+HeapDumpOnOutOfMemoryError"
JAVA_OPTS="$JAVA_OPTS -Daios.dir=$USER_DIR $SYS_ARGS "


JAVA_EXEC=$JAVA_HOME/bin/java
AIOS_ARGS="$JAVA_OPTS -classpath $AIOS_CLASSPATH  $MAIN_CLASS $ARGS"
PID_FILE=/var/run/$NAME.pid


echo "$JAVA_EXEC $AIOS_ARGS"
# $JAVA_EXEC $JAVA_OPTS -classpath $AIOS_CLASSPATH  $MAIN_CLASS $ARGS


case "$1" in
  start)
	checkJava

	log_daemon_msg "Starting $DESC"

	pid=`pidofproc -p $PID_FILE $NAME`
	if [ -n "$pid" ] ; then
		log_begin_msg "Already running."
		log_end_msg 0
		exit 0
	fi

	# Start Daemon
        start-stop-daemon --start -b -m --user $AIOS_USER -c $AIOS_USER --chdir $USER_DIR --pidfile "$PID_FILE" --exec $JAVA_EXEC -- $AIOS_ARGS

	log_end_msg $?
	;;		
  stop)
	log_daemon_msg "Stopping $DESC"

	if [ -f "$PID_FILE" ]; then 
		start-stop-daemon --stop --pidfile "$PID_FILE" 	--user $AIOS_USER --retry=TERM/20/KILL/5 >/dev/null
		if [ $? -eq 1 ]; then
			log_progress_msg "$DESC is not running but pid file exists, cleaning up"
		elif [ $? -eq 3 ]; then
			PID="`cat $PID_FILE`"
			log_failure_msg "Failed to stop $DESC (pid $PID)"
			exit 1
		fi
		rm -f "$PID_FILE"
	else
		log_progress_msg "(not running)"
	fi
	log_end_msg 0
	;;
  status)
	status_of_proc -p $PID_FILE $NAME $NAME && exit 0 || exit $?
    ;;
  restart|force-reload)
	if [ -f "$PID_FILE" ]; then
		$0 stop
		sleep 2
	fi
	$0 start
	;;
  *)
	log_success_msg "Usage: $0 {start|stop|restart|force-reload|status}"
	exit 1
	;;
esac

exit 0
