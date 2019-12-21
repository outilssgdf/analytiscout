#!/bin/sh

if [ -z "$JAVA_HOME" ] ; then
  JAVACMD=`which java`
else
  JAVACMD="$JAVA_HOME/bin/java"
fi

if [ ! -f "$JAVACMD" ] ; then
  JAVACMD=/Library/Internet\ Plug-Ins/JavaAppletPlugin.plugin/contents/Home/bin/java
fi

if [ ! -f "$JAVACMD" ] ; then
  echo "java non detecte" >&2
  exit 1
fi

## resolve links - $0 may be a link to Maven's home
PRG="$0"

# need this for relative symlinks
while [ -h "$PRG" ] ; do
  ls=`ls -ld "$PRG"`
  link=`expr "$ls" : '.*-> \(.*\)$'`
  if expr "$link" : '/.*' > /dev/null; then
    PRG="$link"
  else
    PRG="`dirname "$PRG"`/$link"
  fi
done

saveddir=`pwd`

ANALYTISCOUT_HOME=`dirname "$PRG"`

# make it fully qualified
MAVEN_HOME=`cd "$MAVEN_HOME" && pwd`

cd "$saveddir"

ANALYTISCOUT_JAR=$ANALYTISCOUT_HOME/app/ANALYTISCOUT-gui.jar
ANALYTISCOUT_LAUNCHER=org.leplan73.analytiscout.gui.GuiCmd

# detection de l'OS
unameOut="$(uname -s)"
case "${unameOut}" in
    Linux*)     $JAVACMD -cp $ANALYTISCOUT_JAR $ANALYTISCOUT_LAUNCHER $@;;
    Darwin*)    $JAVACMD -Dapple.laf.useScreenMenuBar=true -Xdock:name="outilsgdf" -cp $ANALYTISCOUT_JAR $ANALYTISCOUT_LAUNCHER $@;;
esac
