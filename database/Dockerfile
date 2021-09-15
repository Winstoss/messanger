FROM postgres:latest

ENV POSTGRES_PASSWORD=password
ENV POSTGRES_DB=messanger

COPY ./dbScheme.sql /docker-entrypoint-initdb.d/init.sql

EXPOSE 5432