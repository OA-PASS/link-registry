#!/bin/sh

set -v

psql -c "CREATE USER ${LINK_REGISTRY_DB_USER} WITH PASSWORD '${LINK_REGISTRY_DB_PASSWD}';"
createdb --username=postgres --owner=${LINK_REGISTRY_DB_USER} --encoding=UNICODE ${LINK_REGISTRY_DB_NAME}
sed -e "s:^#port = 5432:port = ${POSTGRES_DB_PORT}:" -i /var/lib/postgresql/data/postgresql.conf