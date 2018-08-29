FROM debian:strech-slim

ENV JAVA_HOME /usr/lib/jvm/java-8-oracle

# add webupd8 repository
RUN \
    echo "===> add webupd8 repository..."  && \
    echo "deb http://ppa.launchpad.net/webupd8team/java/ubuntu trusty main" | tee /etc/apt/sources.list.d/webupd8team-java.list  && \
    echo "deb-src http://ppa.launchpad.net/webupd8team/java/ubuntu trusty main" | tee -a /etc/apt/sources.list.d/webupd8team-java.list  && \
    apt-key adv --keyserver keyserver.ubuntu.com --recv-keys EEA14886  && \
    apt-get update  && \
    \
    \
    echo "===> install Java & Git & Maven"  && \
    echo debconf shared/accepted-oracle-license-v1-1 select true | debconf-set-selections  && \
    echo debconf shared/accepted-oracle-license-v1-1 seen true | debconf-set-selections  && \
    DEBIAN_FRONTEND=noninteractive  apt-get install -y --force-yes \
        oracle-java8-installer \
        oracle-java8-set-default \
        git \
        maven && \
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
    apt-get remove  -y --purge maven git && \
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

# COPY ./log4j2.xml /opt/aios-dist/

EXPOSE 8118

ENTRYPOINT ["/docker-entrypoint.sh"]

CMD ["aios"]

