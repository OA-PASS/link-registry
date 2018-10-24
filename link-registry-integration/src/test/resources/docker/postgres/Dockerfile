FROM postgres:10-alpine

ENV POSTGRES_DB_PORT=5432 \
    LINK_REGISTRY_DB_NAME=links \
    LINK_REGISTRY_DB_USER=links \
    LINK_REGISTRY_DB_PASSWD=moo

USER root

RUN apk --update --no-cache add postgresql-client postgresql-contrib

ADD init-link-registry.sh /docker-entrypoint-initdb.d

RUN chmod 555 /docker-entrypoint-initdb.d/init-link-registry.sh

USER postgres