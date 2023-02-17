FROM openjdk:11

MAINTAINER yousj "youshaojunde@163.com"

ENV TZ=Asia/Shanghai
RUN set -eux; \
    ln -snf /usr/share/zoneinfo/$TZ /etc/localtime; \
    echo $TZ > /etc/timezone

USER java-app
EXPOSE 8080 5005

CMD exec java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar ${app}
