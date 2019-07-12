#!/bin/sh

die () {
    echo >&2 "$@"
    exit 1
}

dir_resolve()
{
    cd "$1" 2>/dev/null || return $?  
    echo "`pwd -P`" 
}


CURRENT_SCRIPT=$(readlink -f "$0")
CURRENT_DIR=$(dirname "$CURRENT_SCRIPT")
CURRENT_DATE=`date +'%Y%m%d'`


# Java home
# JDK_DIRS="/usr/lib/jvm/java-8-oracle /usr/lib/jvm/java-8-openjdk /usr/lib/jvm/java-8-openjdk-amd64 /usr/lib/jvm/java-8-openjdk-armhf /usr/lib/jvm/java-8-openjdk-i386 /usr/local/openjdk-11"

JAVA=`which java`

if [ -n "$JAVA_HOME" ]; then
  JAVA_HOME=`dirname $JAVA`
  JAVA_HOME=`dirname $JAVA_HOME`
# # Look for the right JVM to use
# for jdir in $JDK_DIRS; do
#    if [ -r "$jdir/bin/java" -a -z "${JAVA_HOME}" ]; then
#        JAVA_HOME="$jdir"
#    fi
# done
  export JAVA_HOME
fi

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

checkJava

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

JAVA_OPTS="$JAVA_OPTS"

if [ -v $JAVA_XMS ]; then
    JAVA_OPTS="$JAVA_OPTS -Xms$JAVA_XMS"
else
    JAVA_OPTS="$JAVA_OPTS -Xms32m"
fi

if [ -v $JAVA_XMX ]; then
    JAVA_OPTS="$JAVA_OPTS -Xmx$JAVA_XMX"
else
    JAVA_OPTS="$JAVA_OPTS -Xmx512m"
fi

JAVA_OPTS="$JAVA_OPTS -XX:+UseParallelGC"
JAVA_OPTS="$JAVA_OPTS -XX:MaxHeapFreeRatio=85 -XX:MinHeapFreeRatio=15"
JAVA_OPTS="$JAVA_OPTS -XX:-HeapDumpOnOutOfMemoryError"
JAVA_OPTS="$JAVA_OPTS -Daios.dir=$USER_DIR $SYS_ARGS "


JAVA_EXEC=$JAVA_HOME/bin/java

# echo "$JAVA_EXEC $JAVA_OPTS -classpath $AIOS_CLASSPATH  $MAIN_CLASS $ARGS"
$JAVA_EXEC $JAVA_OPTS -classpath $AIOS_CLASSPATH  $MAIN_CLASS $ARGS
