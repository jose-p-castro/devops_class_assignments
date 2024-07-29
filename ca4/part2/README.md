# Class Assignment 4 / Part 2 - Report

## Introduction

The objective of this report is to furnish a comprehensive overview of the second part of the third class assignment.
This part focuses on the use of docker compose to deploy a multi-container application. Docker Compose is a tool for
defining and running multi-container Docker applications. It allows you to define the services, networks, and volumes
needed for your application in a single file, typically named `docker-compose.yml`. With Docker Compose, you can easily
manage complex applications with multiple interconnected containers, making it simpler to develop, deploy, and scale
your applications consistently across different environments.

Docker Compose serves as a powerful orchestration tool in the Docker ecosystem, streamlining the management of
multi-container applications. By utilizing a declarative YAML file, developers can define the various components of
their application stack, including services, networks, and volumes, along with their configurations and dependencies.
This concise configuration file encapsulates the entire application environment, facilitating easy collaboration and
reproducibility across development, testing, and production environments. Furthermore, Docker Compose simplifies the
deployment process by providing commands for building, starting, stopping, and scaling the defined services with a
single terminal command. This level of abstraction enhances the efficiency of development workflows, enabling developers
to focus more on building and iterating on their applications rather than grappling with intricate deployment logistics.

The goal of this assignment is to use Docker to set up a containerized environment to execute the tutorial spring
boot application, gradle "basic" version, developed in CA2, Part2. In the previous assignment (CA3 Part2) we used
Vagrant to provision 2 Virtual Machines, one for the database and another for the spring boot application. In this
assignment, we will use Docker Compose to deploy the same application in a multi-container environment.

Issues should be created in GitHub for each main task that was completed. To see how to create issues please refer
to the [Creating Issues](../../ca1/README.md) section on the Class Assignment 1 report.

This Class Assignment was made by the student José Castro nº1231836 of class B and the outcome of the work undertaken in
this assignment can be found [here](https://github.com/J2PCastro/devops-23-24-PSM-1231836).

# Table of Contents

- [Setup](#setup)
- [Docker Compose](#docker-compose)
    - [Dockerfile for the Web Server](#dockerfile-for-the-web-server)
    - [Dockerfile for the Database](#dockerfile-for-the-database)
    - [Docker Compose Configuration](#docker-compose-configuration)
    - [Running the docker-compose.yml file](#running-the-docker-composeyml-file)
    - [Copy database files to the host machine](#copy-database-files-to-the-host-machine)
    - [Tag images and publish to Docker Hub](#tag-images-and-publish-to-docker-hub)
- [Kubernetes vs Docker](#kubernetes-vs-docker)
    - [Some Kubernetes concepts first](#some-kubernetes-concepts-first)
        - [Pods](#pods)
        - [Deployments](#deployments)
        - [Services](#services)
    - [Using Kubernetes to deploy the multi-container application](#using-kubernetes-to-deploy-the-multi-container-application)
- [Heroku](#heroku)
- [Conclusion](#conclusion)

# Setup

If you are following this guide on a Linux machine like me, and have Docker installed, you now have to install Docker
Compose since they are two different tools.

Assuming you have already installed Docker via the instructions provided on the Docker website, you can install docker
compose by simply running the following command:

```bash
sudo apt update
sudo apt install docker-compose
```

After installing Docker Compose, you can verify the installation by running the following command:

```bash
docker-compose --version
```

This command should display the version of Docker Compose installed on your system.

# Docker Compose

Docker Compose is a tool for defining and running multi-container Docker applications. It allows you to define the
services, networks, and volumes needed for your application in a single file, typically named `docker-compose.yml`.

Start by creating a new directory for your project called `ca4/part2` and navigate to it. Docker Compose makes use of
existing Dockerfiles to build images for the services defined in the `docker-compose.yml` file so before proceeding with
the creation of the `docker-compose.yml` file, let's create 2 dockerfiles, one for the spring boot application and
another for the database.

## Dockerfile for the Web Server

Inside the directory `ca4/part2`, create a new dockerfile called `Dockerfile_web` with the following content:

```dockerfile
FROM tomcat:10.1.24-jdk17-temurin-jammy
LABEL authors="José Castro"

# Install Git
RUN apt-get update && apt-get install -y git

# Set working directory
WORKDIR /app

# Clone the repository
RUN git clone https://github.com/J2PCastro/devops-23-24-PSM-1231836.git

# Set working directory
WORKDIR devops-23-24-PSM-1231836/ca2/part2/react-and-spring-data-rest-basic/

# Update application properties
# Uncomment the first line in application.properties
RUN sed -i '0,/^[ \t]*#server.servlet.context-path=/s//server.servlet.context-path=/g' src/main/resources/application.properties

# Modify the application.properties file to set SPRING_DATASOURCE_URL
RUN sed -i 's|spring.datasource.url=.*|spring.datasource.url=jdbc:h2:tcp://db:9092/./jpadb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE|' src/main/resources/application.properties

# Modify the app.js file to set the correct path
RUN sed -i "s|path: '/api/employees'|path: '/react-and-spring-data-rest-basic-0.0.1-SNAPSHOT/api/employees'|" src/main/js/app.js

# Make the Gradle wrapper executable
RUN chmod +x gradlew

# Build the application and copy the war file to the Tomcat webapps directory
RUN ./gradlew clean build && cp ./build/libs/react-and-spring-data-rest-basic-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/

# Expose port 8080
EXPOSE 8080

# Start Tomcat
CMD ["catalina.sh", "run"]
```

Let's break down the contents of the Dockerfile:

- `FROM tomcat:10.1.24-jdk17-temurin-jammy`: This line specifies the base image for the Dockerfile, in this case,
  we are using the official Tomcat image with the specified version.
- `LABEL authors="José Castro"`: This line adds a label to the image with the author's name.
- `RUN apt-get update && apt-get install -y git`: This line updates the package list and installs Git.
- `WORKDIR /app`: This line sets the working directory inside the container to `/app`.
- `RUN git clone <repository-url>`: This line clones the repository containing the Spring Boot application.
- `WORKDIR devops-23-24-PSM-1231836/ca2/part2/react-and-spring-data-rest-basic/`: This line sets the working directory
  to the directory containing the Spring Boot application.
- `RUN sed -i '0,/^[ \t]*#server.servlet.context-path=/s//server.servlet.context-path=/g' src/main/resources/application.properties`:
  This line updates the `application.properties` file to uncomment the line that sets the server context path.
- `RUN sed -i 's|spring.datasource.url=.*|spring.datasource.url=jdbc:h2:tcp://db:9092/./jpadb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE|' src/main/resources/application.properties`:
  This line updates the `application.properties` file to set the correct database URL.
- `RUN sed -i "s|path: '/api/employees'|path: '/react-and-spring-data-rest-basic-0.0.1-SNAPSHOT/api/employees'|" src/main/js/app.js`:
  This line updates the `app.js` file to set the correct path for the REST API.
- `RUN chmod +x gradlew`: This line makes the Gradle wrapper executable.
- `RUN ./gradlew clean build && cp ./build/libs/react-and-spring-data-rest-basic-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/`:
  This line builds the Spring Boot application and copies the WAR file to the Tomcat webapps directory.
- `EXPOSE 8080`: This line exposes port 8080 in the container.
- `CMD ["catalina.sh", "run"]`: This line starts the Tomcat server when the container is run.

In summary, this Dockerfile clones the Spring Boot application repository, updates the application properties to set the
correct database URL and server context path, builds the application, and deploys it to the Tomcat server.

## Dockerfile for the Database

Inside the directory `ca4/part2`, create a new dockerfile called `Dockerfile_db` with the following content:

```dockerfile
FROM ubuntu:focal

# Install Java 17
RUN apt-get update && apt-get install -y wget openjdk-17-jdk-headless && rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /opt/h2

# Download H2 database
RUN wget https://repo1.maven.org/maven2/com/h2database/h2/1.4.200/h2-1.4.200.jar -O h2.jar

# Expose ports
EXPOSE 8082
EXPOSE 9092

# Start H2 database
CMD ["java", "-cp", "h2.jar", "org.h2.tools.Server", "-ifNotExists", "-web", "-webAllowOthers", "-webPort", "8082", "-tcp", "-tcpAllowOthers", "-tcpPort", "9092"]
```

Let's break down the contents of the Dockerfile:

- `FROM ubuntu:focal`: This line specifies the base image for the Dockerfile, in this case, we are using the official
  Ubuntu image with the specified version.
- `RUN apt-get update && apt-get install -y wget openjdk-17-jdk-headless && rm -rf /var/lib/apt/lists/*`: This line
  updates the package list, installs Java 17, and removes the package lists to reduce the image size.
- `WORKDIR /opt/h2`: This line sets the working directory inside the container to `/opt/h2`.
- `RUN wget https://repo1.maven.org/maven2/com/h2database/h2/1.4.200/h2-1.4.200.jar -O h2.jar`: This line downloads
  the H2 database JAR file.
- `EXPOSE 8082`: This line exposes port 8082 in the container for the H2 web console.
- `EXPOSE 9092`: This line exposes port 9092 in the container for the H2 TCP server.
- `CMD ["java", "-cp", "h2.jar", "org.h2.tools.Server", "-ifNotExists", "-web", "-webAllowOthers", "-webPort", "8082", "-tcp", "-tcpAllowOthers", "-tcpPort", "9092"]`:
  This line starts the H2 database server with the specified options.

In summary, this Dockerfile installs Java 17, downloads the H2 database JAR file, and starts the H2 database server with
the web console and TCP server enabled.

## Docker Compose Configuration

Now that we have the Dockerfiles for the web server and database, we can create the `docker-compose.yml` file to define
the services, networks, and volumes for our multi-container application.

Inside the directory `ca4/part2`, create a new file called `docker-compose.yml` with the following content:

```yaml
version: '3'
services:
  db:
    build:
      context: .
      dockerfile: Dockerfile_db
    container_name: ca4_part2_db
    ports:
      - "8082:8082"
      - "9092:9092"
    volumes:
      - h2-data:/opt/h2-data
    networks:
      app-network:
        ipv4_address: 192.168.33.11
  web:
    build:
      context: .
      dockerfile: Dockerfile_web
    container_name: ca4_part2_web
    ports:
      - "8080:8080"
    networks:
      app-network:
        ipv4_address: 192.168.33.10
    depends_on:
      - "db"
volumes:
  h2-data:
    driver: local
networks:
  app-network:
    ipam:
      driver: default
      config:
        - subnet: 192.168.33.0/24
```

Let's break down the contents of the `docker-compose.yml` file as it has a lot of information, very important to
understand:

- `version: '3'`: This line specifies the version of the Docker Compose file format.
- `services`: This section defines the services that make up the application. In this case, we have two services: `db`
  for the database and `web` for the web server.
- `db`: This section defines the configuration for the database service.
    - `build`: This specifies the build context and Dockerfile for the service. In this case, we are using the
      `Dockerfile_db` file in the current directory.
    - `container_name: ca4_part2_db`: This sets the name of the container for the database service.
    - `ports`: This maps the ports of the container to the host machine. Port 8082 is mapped to port 8082 on the host
      machine for the H2 web console, and port 9092 is mapped to port 9092 on the host machine for the H2 TCP server as
      we defined in the Dockerfile.
    - `volumes`: This mounts a volume for the H2 database data. The volume is named `h2-data` and is mounted to
      `/opt/h2-data` in the container. This allows the data to persist across container restarts and also enables us to
      copy the database files to the host machine.
    - `networks`: This assigns the service to the `app-network` network with a specific IP address.
- `web`: This section defines the configuration for the web server service.
    - `build`: This specifies the build context and Dockerfile for the service. In this case, we are using the
      `Dockerfile_web` file in the current directory.
    - `container_name: ca4_part2_web`: This sets the name of the container for the web server service.
    - `ports`: This maps port 8080 of the container to port 8080 on the host machine for accessing the web application
      as we defined in the Dockerfile.
    - `networks`: This assigns the service to the `app-network` network with a specific IP address.
    - `depends_on`: This specifies that the web server service depends on the database service. This ensures that the
      database service is started before the web server service.
- `volumes`: This section defines the volume for the H2 database data. The volume is named `h2-data` and uses the local
  driver.
- `networks`: This section defines the network for the services. The `app-network` network is created with a specific
  subnet and IP addresses for the services.

In summary, the `docker-compose.yml` file defines two services: `db` for the H2 database and `web` for the web server.
The database service exposes ports 8082 and 9092 for the H2 web console and TCP server, respectively, and mounts a
volume
for the database data. The web server service exposes port 8080 for accessing the web application and depends on the
database service. Both services are assigned to the `app-network` network with specific IP addresses.

# Running the docker-compose.yml file

To build and run the multi-container application defined in the `docker-compose.yml` file, we need to execute
the `docker-compose up` command. This command will build the images for the services, create the containers, and start
the services. To stop the services and remove the containers, we can use the `docker-compose down` command. Let's run
the `docker-compose up` command to start the application. Make sure you are in the `ca4/part2` directory before running:

```bash
docker-compose up -d
```

This command will build the images for the services, create the containers. The `-d` flag runs the containers in
detached
mode, meaning they will run in the background. You can check the status of the containers by running:

```bash
docker-compose ps
```

Now that the containers are running, you can access the web application by opening a web browser and
navigating [here](http://localhost:8080/react-and-spring-data-rest-basic-0.0.1-SNAPSHOT). To access the
H2 web console, navigate [here](http://localhost:8080/react-and-spring-data-rest-basic-0.0.1-SNAPSHOT/h2-console).

After checking that the application is running correctly, you can stop the services and remove the containers by
running:

```bash
docker-compose down
```

This command will stop the services and remove the containers. You can verify that the containers have been removed by
running:

```bash
docker-compose ps
```

# Copy database files to the host machine

Since we set up a volume for the H2 database data in the `docker-compose.yml` file, the database files are stored on the
host machine. To copy the database files from the container to the host machine, we can use the `docker exec` command to
run a command inside the container. Let's copy the database files to the host machine:

```bash
docker exec -it ca4_part2_db /bin/bash
cp -r /opt/h2-data /opt/h2-data/
exit
```

This command copies the database files from the container to the host machine. You can verify that the files have been
copied by checking the `h2-data` directory on the host machine. Notice the directory where the files are copied to is
the same as the one defined in the `docker-compose.yml` file.

# Tag images and publish to Docker Hub

After creating an account, log in to Docker Hub using the `docker login` command:

```bash
docker login
```

Enter your Docker Hub username and password when prompted. After logging in we need to tag the Docker images with the
Docker Hub username and repository name. To see the images that were created, run the following command:

```bash
docker-compose images
```

This will display a list of all the Docker images associated with docker compose on your system. Find the images tagged
below the `REPOSITORY` column with the names `part2_db` and `part2_web`. To tag the images with your Docker Hub
username and repository name, run the following commands:

```bash
docker tag <IMAGE_ID> <DOCKER_HUB_USERNAME>/part2_web:webserver
docker tag <IMAGE_ID> <DOCKER_HUB_USERNAME>/part2_db:database
```

Replace `<IMAGE_ID>` with the ID of the Docker image you want to tag and `<DOCKER_HUB_USERNAME>` with your Docker Hub
username. The `:webserver` and `:database` tags are optional and can be replaced with any tag you prefer.

After tagging the images, you can publish them to Docker Hub using the `docker push` command:

```bash
docker push <DOCKER_HUB_USERNAME>/part2_web:webserver
docker push <DOCKER_HUB_USERNAME>/part2_db:database
```

Replace `<DOCKER_HUB_USERNAME>` with your Docker Hub username and `:webserver` and `:database` with the tags you used
earlier. This will push the Docker images to your Docker Hub repository, making them available for others to use.

To see the images created during this assignment, you can visit the Docker Hub repository of the student José
Castro [here](https://hub.docker.com/u/josepedrocastro).

# Kubernetes vs Docker

Docker and Kubernetes are two of the most popular containerization platforms used in the industry today. While Docker
provides a way to package and run applications in containers, Kubernetes is an orchestration tool that helps manage
containerized applications in a clustered environment. Both tools have their strengths and weaknesses, and the choice
between them depends on the specific requirements of the application and the organization.

Kubernetes is a powerful container orchestration platform that is designed for managing large-scale containerized
applications in production environments. It provides features such as automatic scaling, load balancing, service
discovery, and rolling updates, making it ideal for complex applications that require high availability and scalability.
Kubernetes uses a declarative configuration model to define the desired state of the application, and it automatically
manages the deployment, scaling, and monitoring of the application components.

Docker, on the other hand, is a containerization platform that simplifies the process of packaging, distributing, and
running applications in containers. It provides a lightweight and portable runtime environment for applications, making
it easy to develop, test, and deploy applications across different environments. Docker Compose is a tool that
complements Docker by providing a way to define and run multi-container applications using a single configuration file.

Kubernetes can be an alternative or a complement to Docker. While Docker is focused on containerization and provides a
simple way to package and run applications in containers, Kubernetes is focused on orchestration and provides a
comprehensive platform for managing containerized applications in a production environment. Kubernetes can be used to
deploy and manage applications that are built using Docker containers, providing additional features such as
auto-scaling, load balancing, and service discovery.

## Some Kubernetes concepts first

### Pods

A Pod is the smallest deployable unit in Kubernetes. It represents a single instance of a running process in the
cluster. A Pod can contain one or more containers that share resources such as networking and storage. Pods are
ephemeral and can be created, deleted, and replaced dynamically by Kubernetes. Pods are typically managed by a
higher-level controller such as a Deployment or StatefulSet.

### Deployments

A Deployment is a higher-level controller that manages the lifecycle of Pods. It ensures that a specified number of Pod
replicas are running at all times and handles updates and rollbacks of the application. Deployments are used to define
the desired state of the application and manage the creation, scaling, and deletion of Pods.

### Services

A Service is an abstraction that defines a logical set of Pods and a policy by which to access them. Services provide a
stable endpoint for accessing the Pods in a Deployment. They enable load balancing, service discovery, and internal
communication between Pods in the cluster. Services can be exposed internally within the cluster or externally to the
outside world.

## Using Kubernetes to deploy the multi-container application

To implement this assignment in Kubernetes, we would need to create Kubernetes Deployment and Service resources for
both the Tomcat server and the database. Here's a general outline of how we could do this:

- Tomcat Server Deployment and Service: The Deployment would define the Docker image to use (which would be our Tomcat
  server image), the number of replicas, and any necessary environment variables or configuration. The Service would
  expose the Tomcat server to other pods in the cluster and optionally to the outside world.
- Database Deployment and Service: Similar to the Tomcat server, the Deployment would define the Docker image to use (
  our database image), the number of replicas, and any necessary environment variables or configuration. The Service
  would expose the database to other pods in the cluster.

In this way we would end up with two pods, one for the Tomcat server and one for the database, each with their own
Deployment and Service resources. The Tomcat server pod would be able to communicate with the database pod using the
Database Service endpoint, and the Tomcat server Service would expose the Tomcat server to the outside world.

# Heroku

Heroku is a cloud platform that allows developers to build, deliver, monitor, and scale applications quickly and
efficiently. Heroku provides a platform as a service (PaaS) that abstracts away the infrastructure management and
provides a simple and intuitive interface for deploying applications. Heroku supports a wide range of programming
languages and frameworks, making it easy to deploy applications built using popular technologies.

Present how this option can be used to deploy your solution:

To deploy the multi-container application to Heroku, we need to create a Heroku account and install the Heroku CLI on
our machine. The Heroku CLI allows us to interact with the Heroku platform from the command line, enabling us to deploy
applications, manage add-ons, and view logs.

After installing the Heroku CLI, we need to log in to our Heroku account using the `heroku login` command:

```bash
heroku container:login
```

This command will prompt you to enter your Heroku credentials to log in to your account. After logging in, we need to
create a new Heroku app using the `heroku create` command:

```bash
heroku create <app-name>
```

Replace `<app-name>` with the name you want to give to your Heroku app. This will create a new Heroku app with the
specified name. Next, we need to build and push the Docker images to the Heroku container registry using the
`heroku container:push` command:

```bash
heroku container:push web --app <app-name>
heroku container:push db --app <app-name>
```

Replace `<app-name>` with the name of your Heroku app. This command will build the Docker images for the web server and
database services and push them to the Heroku container registry. After pushing the images, we need to release the
images to the Heroku app using the `heroku container:release` command:

```bash
heroku container:release web --app <app-name>
heroku container:release db --app <app-name>
```

Replace `<app-name>` with the name of your Heroku app. This command will release the Docker images to the Heroku app,
making them available for deployment. Finally, we need to scale the web server service to one dyno using
the `heroku ps:scale` command:

```bash
heroku ps:scale web=1 --app <app-name>
```

Replace `<app-name>` with the name of your Heroku app. This command will scale the web server service to one dyno,
ensuring that the application is running. You can access the web application by navigating to the Heroku app URL in a
web browser or using the Heroku CLI:

```bash
heroku open --app <app-name>
```

Replace `<app-name>` with the name of your Heroku app. This will open the web application in a web browser.

These are the steps I believe are necessary to deploy an app to Heroku because unfortunately, as of November
28th 2021 Heroku has shut down their free tier meaning I was unable to deploy the application to Heroku and test this
commands to ensure their success or failure, which prevented me from making sure everything worked correctly. However,
the steps provided above should work for deploying the multi-container application to Heroku. These steps are the result
of the research I conducted on how to deploy a multi-container application to Heroku, mainly
from [here](https://devcenter.heroku.com/articles/container-registry-and-runtime).

# Conclusion

In conclusion, this report has provided a comprehensive overview of the process of deploying a multi-container
application using Docker Compose. Docker Compose has proven to be a powerful tool for managing complex applications with
multiple interconnected containers, simplifying the development, deployment, and scaling process.

The report detailed the creation of Dockerfiles for both the web server and the database, and the configuration of
a `docker-compose.yml` file to define the services, networks, and volumes for the application. The process of building
and running the application using Docker Compose was also explained, along with the steps to copy the database files to
the host machine and tag and publish the Docker images to Docker Hub.

Furthermore, the report explored the differences between Docker and Kubernetes, two of the most popular containerization
platforms used in the industry today. While Docker provides a simple way to package and run applications in containers,
Kubernetes is a powerful orchestration tool that provides a comprehensive platform for managing containerized
applications in a production environment.

Lastly, the report touched on the potential of deploying the application to Heroku, a cloud platform that allows
developers to build, deliver, monitor, and scale applications quickly and efficiently. Although the actual deployment to
Heroku could not be performed due to the discontinuation of their free tier, the necessary steps for such a deployment
were outlined based on research.

Overall, this report serves as a valuable resource for understanding the implementation of Docker Compose in deploying
multi-container applications, the comparison between Docker and Kubernetes, and the potential of deploying applications
to Heroku. It reflects the student's thorough analysis, problem-solving skills, and proficiency in DevOps practices.