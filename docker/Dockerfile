FROM maven:3.6.1-jdk-11

MAINTAINER Robert Kania<cezaryrk@yahoo.de>

RUN apt-get update -qq && \
    apt-get install -q -y apt-utils gnupg curl man && \
    \
    \
    echo "===> install Java & Git & Maven"  && \
    echo debconf shared/accepted-oracle-license-v1-1 select true | debconf-set-selections  && \
    echo debconf shared/accepted-oracle-license-v1-1 seen true | debconf-set-selections  && \
    set -ex; \
    \
    # deal with slim variants not having man page directories (which causes "update-alternatives" to fail)
    if [ ! -d /usr/share/man/man1 ]; then \
    	mkdir -p /usr/share/man/man1; \
    fi; \
    \
    \
    echo "===> clone Aios from https://github.com/thinkbaer/aios.git"  && \
    mkdir -p /opt/aios && \
    mkdir -p /data/aios && \
    mkdir -p /data/java/driver && \
    cd /opt/aios && \
    git clone https://github.com/thinkbaer/aios.git . && \
    \
    \
    echo "===> build Aios project with maven"  && \
    cd /opt/aios && \
    mvn clean package && \
    \
    \
    mv /opt/aios/build/aios-dist /opt/ && \
    rm -R /opt/aios && \
    chmod +x /opt/aios-dist/bin/*.sh && \
    \
    \
    echo "===> clean up..." && \
    apt-get remove  -y --purge maven git curl gnupg man && \
    apt-get autoremove -y --purge  && \
    apt-get -y autoclean && \
    rm -rf /var/lib/apt/lists/* && \
    rm -rf "/$JAVA_HOME/"*src.zip && \
    rm -rf "$JAVA_HOME/lib/missioncontrol" \
           "$JAVA_HOME/lib/visualvm" \
           "$JAVA_HOME/lib/"*javafx* \
           "$JAVA_HOME/jre/lib/plugin.jar" \
           "$JAVA_HOME/jre/lib/ext/jfxrt.jar" \
           "$JAVA_HOME/jre/bin/javaws" \
           "$JAVA_HOME/jre/lib/javaws.jar" \
           "$JAVA_HOME/jre/lib/desktop" \
           "$JAVA_HOME/jre/plugin" \
           "$JAVA_HOME/jre/lib/"deploy* \
           "$JAVA_HOME/jre/lib/"*javafx* \
           "$JAVA_HOME/jre/lib/"*jfx* \
           "$JAVA_HOME/jre/lib/amd64/libdecora_sse.so" \
           "$JAVA_HOME/jre/lib/amd64/"libprism_*.so \
           "$JAVA_HOME/jre/lib/amd64/libfxplugins.so" \
           "$JAVA_HOME/jre/lib/amd64/libglass.so" \
           "$JAVA_HOME/jre/lib/amd64/libgstreamer-lite.so" \
           "$JAVA_HOME/jre/lib/amd64/"libjavafx*.so \
           "$JAVA_HOME/jre/lib/amd64/"libjfx*.so \
           "${JAVA_HOME}/jre/lib/security/README.txt"

COPY driver/* /data/java/driver/
COPY ./docker-entrypoint.sh /

EXPOSE 8118

ENTRYPOINT ["/docker-entrypoint.sh"]

CMD ["aios"]


