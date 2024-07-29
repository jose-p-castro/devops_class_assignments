# Class Assignment 4 / Part 1 - Report

## Introduction

The objective of this report is to furnish a comprehensive overview of the second part of the third class assignment.
This part focuses on the use of Docker. Docker is a platform that allows developers to automate the deployment, scaling,
and management of applications using containerization. Containers are lightweight, portable units that bundle an
application with its dependencies and configurations, ensuring that it runs consistently across different environments.
Docker simplifies the process of setting up development environments, improves collaboration, and enhances the
efficiency of deploying applications. This report will provide a detailed explanation of the Dockerfiles used to fulfill
the assignment requirements, as well as the commands used to build and run the containers.

Docker is a platform that simplifies the development, deployment, and management of applications through
containerization. Containers are lightweight, portable units that package an application with all its dependencies,
ensuring consistent performance across different environments. Unlike virtual machines, containers share the host
system's operating system kernel, making them more efficient and faster to start.

By using Docker, developers can eliminate issues caused by differences in Operating System (OS) versions and system
libraries between development, testing, and production environments. Docker containers run in isolation, providing
security and allowing multiple containers to operate on the same host without interference.

The goal of the Part 1 of this assignment is to practice with Docker, creating docker images and running containers
using the chat application from Class Assignment 2.

Issues should be created in GitHub for each main task that was completed. To see how to create issues please refer
to the [Creating Issues](../../ca1/README.md) section on the Class Assignment 1 report.

This Class Assignment was made by the student José Castro nº1231836 of class B and the outcome of the work undertaken in
this assignment can be found [here](https://github.com/J2PCastro/devops-23-24-PSM-1231836).

# Table of Contents

- [Setup](#setup)
- [Dockerfiles](#dockerfiles)
    - [Dockerfile v1](#dockerfile-v1)
    - [Dockerfile v2](#dockerfile-v2)
- [Building the Docker Images](#building-the-docker-images)
- [Running the Docker Containers](#running-the-docker-containers)
- [Tag image and publish to Docker Hub](#tag-image-and-publish-to-docker-hub)
- [Conclusion](#conclusion)

# Setup

First and foremost, it is necessary to install Docker on the host machine. To do so navigate to
the [Docker website](https://www.docker.com/) and follow the steps to download and install the latest version of Docker
for your operating system.

# Dockerfiles

In order to fully explore the concept of docker images you should create two versions of the Dockerfile:

- One version that builds the application chat server "inside" the Dockerfile (which will be referred to as v1).
- Another version where the application chat server is built in the host computer and the `.jar` file is copied to the
  Docker image (which will be referred to as v2).

A docker image is a lightweight, standalone, executable package that includes everything needed to run a piece of
software, including the code, a runtime, libraries, environment variables, and configuration files. Docker images are
built from a Dockerfile, which is a text document that contains all the commands a user could call on the command line
to
assemble an image.

## Dockerfile v1

The Dockerfile v1 is used to build the chat server application inside the Docker container. To start, create a new
directory called `ca4/part1/v1` and create a new file called `Dockerfile` inside this directory. The contents of the
`Dockerfile` are as follows:

```dockerfile
FROM openjdk:17-jdk-slim
LABEL authors="José Castro"

WORKDIR /app

COPY ./ca2/part1 .

RUN chmod +x gradlew

RUN ./gradlew build

CMD ["./gradlew", "runServer"]
```

There are several ways of building the docker image that we want with our chat application, like cloning the repository
to the container and building it there, but for this assignment, since we are working in the repository where the chat
server is contained, we will simply copy it to the container and build it there.

Lets breakdown the Dockerfile v1:

- `FROM openjdk:17-jdk-slim`: This line specifies the base image for the Docker container. In this case, we are using
  the `openjdk:17-jdk-slim` image, which is a lightweight image that contains the Java Development Kit (JDK) version 17.
- `LABEL authors="José Castro"`: This line adds a label to the Docker image, specifying the author of the image.
- `WORKDIR /app`: This line sets the working directory inside the Docker container to `/app`. This is where the chat
  server application will be copied to, built and run.
- `COPY ./ca2/part1 .`: This line copies the contents of the `ca2/part1` directory from the host machine to the
  `/app` directory inside the Docker container. Note here the path is relative to the Dockerfile. This will be expanded
  on later.
- `RUN chmod +x gradlew`: This line makes the `gradlew` script executable inside the Docker container. This script is
  used to build the chat server application.
- `RUN ./gradlew build`: This line runs the `gradlew` script inside the Docker container to build the chat server
  application.
- `CMD ["./gradlew", "runServer"]`: This line specifies the command to run when the Docker container is started. In this
  case, it runs the `runServer` task of the `gradlew` script, which starts the chat server application.

As you can see Docker uses a set of specific commands, like `FROM`, `LABEL`, `WORKDIR`, `COPY`, `RUN`, and `CMD`, to
define the steps needed to build the Docker image. These commands are executed in order when the Docker image is built
and are specific to Docker. Here is a quick breakdown of the commands used in the Dockerfile:

- `FROM`: Specifies the base image for the Docker container.
- `LABEL`: Adds metadata to the Docker image.
- `WORKDIR`: Sets the working directory inside the Docker container.
- `COPY`: Copies files from the host machine to the Docker container.
- `RUN`: Runs commands inside the Docker container.
- `CMD`: Specifies the command to run when the Docker container is started.

## Dockerfile v2

The Dockerfile v2 is uses the chat server application built in the host computer by copying the `.jar` file to the
Docker image. To start, create a new directory called `ca4/part1/v2` and create a new file called `Dockerfile` inside
this directory. The contents of the `Dockerfile` are as follows:

```dockerfile
FROM openjdk:21-jdk-slim
LABEL authors="José Castro"

WORKDIR /app

COPY ./ca2/part1/build/libs/*.jar .

CMD ["java", "-cp", "basic_demo-0.1.0.jar", "basic_demo.ChatServerApp", "59001"]
```

Lets breakdown the Dockerfile v2:

- `FROM openjdk:21-jdk-slim`: This line specifies the base image for the Docker container. In this case, we are using
  the `openjdk:21-jdk-slim` image, which is a lightweight image that contains the Java Development Kit (JDK) version 21.
  This differs a bit from the previous Dockerfile, where we used JDK 17, because the JDK installed in the host machine
  is JDK 21 and to prevent compilation errors when executing the chat server, we need to use the same JDK version.
- `LABEL authors="José Castro"`: This line adds a label to the Docker image, specifying the author of the image.
- `WORKDIR /app`: This line sets the working directory inside the Docker container to `/app`. This is where the chat
  server application `.jar` file will be copied to and run.
- `COPY ./ca2/part1/build/libs/*.jar .`: This line copies the `.jar` file of the chat server application from the host
  machine to the `/app` directory inside the Docker container. Note here the path is relative to the Dockerfile. This
  will be expanded on later.
- `CMD ["java", "-cp", "basic_demo-0.1.0.jar", "basic_demo.ChatServerApp", "59001"]`: This line specifies the command
  to run when the Docker container is started. In this case, it runs the chat server application `.jar` file using the
  `java` command with the specified classpath and port number.

One important note here is that in order for this image to work, the chat server application must be built in the host
machine before building the Docker image. This is because the `.jar` file is copied from the `ca2/part1/build/libs`
directory, which is where the chat server application is built.

# Building the Docker Images

To build the Docker images using the Dockerfiles created above, we need to execute the `docker build` command. Normally,
this is done from the directory where the Dockerfile is located. However, in this case, it won't work due to the
relative paths defined earlier in the Dockerfiles. To resolve this, we need to run the `docker build` command from the
project's root directory and specify the Dockerfile location using the `-f` flag. This approach ensures that Docker uses
the root directory to look for everything that is needed to build the image.

To build the Docker image using the Dockerfile v1, run the following command from the project's root directory:

```bash
docker build -t ca4_part1_v1 -f ./ca4/part1/v1/Dockerfile .
```

To build the Docker image using the Dockerfile v2, run the following command from the project's root directory:

```bash
docker build -t ca4_part1_v2 -f ./ca4/part1/v2/Dockerfile .
```

The `-t` flag is used to tag the Docker image with a name and optional tag. In this case, we are tagging the images with
`ca4_part1_v1` and `ca4_part1_v2` for version 1 and version 2, respectively. The `-f` flag is used to specify the
location of the Dockerfile to use for building the image. The `.` at the end of the command specifies the build context.

The build context is the set of files and directories that are sent to the Docker daemon when building the image. By
specifying `.` as the build context, we are telling Docker to use the current directory as the build context.

# Running the Docker Containers

After building the Docker images, we can run the containers using the `docker run` command and passing it some
additional
arguments. To run the Docker container using the Dockerfile v1, run the following command:

```bash
docker run -d -p 59001:59001 ca4_part1_v1
```

To run the Docker container using the Dockerfile v2, run the following command:

```bash
docker run -d -p 59001:59001 ca4_part1_v2
```

The `-p` flag is used to map the port `59001` from the Docker container to the host machine. This allows us to access
the chat server application running inside the Docker container from the host machine. The `-d` flag is used to run the
Docker container in detached mode, which means it runs in the background.

To test if everything is working correctly, open a new terminal window, navigate to the directory where the chat
application is located, and run the following command:

```bash
./gradlew runClient
```

This will start the chat client application and connect to the chat server running inside the Docker container by
opening a chat window.

To stop the Docker container, you can use the `docker stop` command followed by the container ID. To get the container
ID, you can use the `docker ps` command to list all running containers. Once you have the container ID, you can stop the
container using the following command:

```bash
docker stop <CONTAINER_ID>
```

Replace `<CONTAINER_ID>` with the ID of the Docker container you want to stop.

If you want to remove the Docker container, you can use the `docker rm` command followed by the container ID. To remove
the container, use the following command:

```bash
docker rm <CONTAINER_ID>
```

# Tag image and publish to Docker Hub

Docker Hub is a cloud-based registry service that allows you to store and share Docker images. One of the requirements
of this assignment is to tag the Docker images created above and publish them to Docker Hub. First, navigate to
the [Docker Hub website](https://hub.docker.com/) and create an account if you don't have one already.

After creating an account, log in to Docker Hub using the `docker login` command:

```bash
docker login
```

Enter your Docker Hub username and password when prompted. After logging in we need to tag the Docker images with the
Docker Hub username and repository name. To see the images that were created, run the following command:

```bash
docker images
```

This will display a list of all the Docker images on your system. Find the images tagged below the `REPOSITORY` column
with the names `ca4_part1_v1` and `ca4_part1_v2`. To tag the images with your Docker Hub username and repository name,
run the following commands:

```bash
docker tag <IMAGE_ID> <DOCKER_HUB_USERNAME>/ca4_part1_v1:version1
docker tag <IMAGE_ID> <DOCKER_HUB_USERNAME>/ca4_part1_v2:version2
```

Replace `<IMAGE_ID>` with the ID of the Docker image you want to tag and `<DOCKER_HUB_USERNAME>` with your Docker Hub
username. The `:version1` and `:version2` tags are optional and can be used to specify different versions of the image.

After tagging the images, you can publish them to Docker Hub using the `docker push` command:

```bash
docker push <DOCKER_HUB_USERNAME>/ca4_part1_v1:version1
docker push <DOCKER_HUB_USERNAME>/ca4_part1_v2:version2
```

Replace `<DOCKER_HUB_USERNAME>` with your Docker Hub username and `:version1` and `:version2` with the tags you used
earlier. This will push the Docker images to your Docker Hub repository, making them available for others to use.

To see the images created during this assignment, you can visit the Docker Hub repository of the student José
Castro [here](https://hub.docker.com/u/josepedrocastro).

# Conclusion

In conclusion, this report provides a comprehensive overview of the first part of the fourth class assignment The
assignment involves creating Docker images for the chat server application from Class Assignment 2, using two different
approaches.

The first approach builds the application inside the Docker container, while the second approach builds the application
on the host machine and copies the `.jar` file to the Docker image. The report details the process of creating the
Dockerfiles, building the Docker images, running the Docker containers, and publishing the Docker images to Docker Hub.

It also explains the benefits and challenges of using Docker, the role of Dockerfiles, and the reasons for using
different approaches to build the Docker images. The assignment demonstrates the practical application of Docker in
software development and provides valuable hands-on experience with Docker commands and Dockerfiles.

Furthermore, it explored the process of tagging Docker images and publishing them to Docker Hub, demonstrating the
ability to share Docker images with others. This process highlighted the importance of understanding Docker commands and
their usage when working with Docker.

Overall, this exercise provided a comprehensive understanding of Docker and its role in creating consistent and
reproducible development environments. It also emphasized the importance of understanding Dockerfiles and Docker
commands in software development.