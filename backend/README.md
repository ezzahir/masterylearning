# Backend Documentation

This is the backend of this e-learning platform. It is a web-based
Spring Boot application and its main functionality is to provide
RESTful endpoint to be used by the frontend (client) application.

# Architecture

This project uses the core Spring Framework library and Spring Boot
for bootstrapping and configuration. Additionally the following
list gives an overview of the other dependencies used:

 * Spring Security for Basic authentication and securing of REST
   endpoints.
 * Spring Data JPA and Hibernate for persistence
 * Mysql JDBC driver to connect with MySQL
 * Log4j2 for logging

We also use the Spring Web MVC layer for our REST endpoints and Jackson
for data serialization.

This application is containerized for docker with the
`docker-maven-plugin`.

# Configuration and Preparation

The default `datasource` configuration for the MySQL
database is stored in the 
[application.properties](src/main/resources/application.properties)
file and can be overridden by using the `-D` option when starting
the executable `jar` file.

In order to run this application a mysql database must be installed
and the user configured in the `datasource` configuration must have
access to an existing database named `masterylearning`.

I use a docker `mysql` instance to run the database, alternatively it is
possible to install MySQL directly on your the system. 

To create and run a new docker container with a MySQL database 
use the following command:

    docker network create mlnet

    docker run --name masterylearning-mysql --net mlnet --net-alias mysql -p 4000:3306 \
               --env MYSQL_ROOT_PASSWORD=masterylearning \
               --env MYSQL_DATABASE=masterylearning \
               -v /srv/masterylearning:/var/lib/mysql -d mysql:latest

If you use an alternative password instead of `masterylearning` you
should overwrite the password datasource option when starting the
backend. The above command also creates a default database named
`masterylearning`.

# Start and Deployment

After you have [built this application](../README.md#testing-building-installing),
there are two ways to run this application in a production environment:

 1. Directly execute the executable jar file found in `target/` which is 
    named `backend-<version>.jar`.
 2. Use docker and start the docker container named `masterylearning/backend`. 

## Run Executable `jar`-File

To run the executable `jar`-file directly use the following
command:

    java -jar target/main.backend-0.1.jar

You can define additional properties with the `-D` option. Use this
 if you need to override some of the properties defined in
 [application.properties](src/main/resources/application.properties).
 For example to override the *data source URL* use:

    java -jar target/main.backend-0.1.jar -Dspring.datasource.url=<some url>

See [Configuration and preparation](#configuration-and-preparation) 
for configuration details for the data source.

## Start a Docker Container

Alternatively you can create and start a new docker container:

    docker create --name masterylearning-backend --net mlnet --net-alias=backend -p 8080:8080 <imageId>
    docker start masterylearning-backend

This has the following effect, the name for our new container is
`backend`, it is linked with an existing mysql container named `masterylearning-mysql`,
and the port 8080 from inside the container is mapped to the port
8080 on the host.

You can check if everything works by running

    docker logs <containerId>

The REST endpoints should now be available on the host through
`http://localhost:8080`.

## Swagger Documentation

REST endpoints are documented with Swagger using the Springfox libraries.
After starting the backend service you can navigate to `${swagger-host}/${swagger-path}/swagger-ui.html`
by default `${swagger-host}` and `${swagger-path}` resolve to the empty string,
which makes the Swagger UI accessible from `/swagger-ui.html`. By starting
the backend module with these properties you can customize the location of
the swagger-ui in case the application runs behind a *reverse-proxy*.

For example, set `swagger.path=/api`, to access the Swagger UI via
`/api/swagger-ui.html`. 

## Developing with E-Mail Support

For development we recommend to setup a Docker container that runs `mailhog`.
To start a MailHog container use:

    docker run -d --name masterylearning-mailhog mailhog

The default Docker container runs an SMTP server on 1025 and a
simple web frontend on port 8025.

By setting the properties `spring.mail.host=localhost` and `spring.mail.port=1025`
you can enable mail support in the backend and check if you received mail
from the backend via `http://localhost:8025`.

## Database Migrations

The backend uses the [Flyway library](https://flywaydb.org/) to perform
automated database migrations and schema validatiosn at startup.

Additional notes concerning database migrations and how to create
corresponding migration scripts can be found in the
[migration/Readme.md](src/main/resources/db/migration/Readme.md).
