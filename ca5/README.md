# Class Assignment 5 - Report

## Introduction

The objective of this report is to furnish a detailed account of the work done in the fifth class assignment. This
assignment revolved around the use of `Jenkins`.

`Jenkins` is an open-source automation server used for continuous integration (CI)
and continuous delivery (CD) pipelines. It automates tasks like code integration, building, testing, and deployment,
streamlining the software development lifecycle. With a vast plugin ecosystem, Jenkins integrates seamlessly with
various tools and technologies, providing flexibility and extensibility. Its web-based interface simplifies
configuration, while distributed builds enable faster processing. Jenkins' monitoring capabilities offer real-time
feedback and notifications, enhancing visibility and ensuring prompt response to issues throughout the development
process.

`Jenkins` is a very powerful tool on its own, but when used in conjunction with other tools such as `Docker`, its
capabilities are greatly enhanced. `Jenkins` and `Docker` complement each other seamlessly in modern software
development pipelines. `Docker` allows applications to be packaged into containers, ensuring consistency across
different environments. When integrated with `Jenkins`, `Docker` enables the creation of isolated environments for
building, testing, and deploying applications. `Jenkins` can use `Docker` containers as build agents, providing a clean
and consistent environment for executing tasks. Additionally, `Docker` images can be used to deploy applications,
ensuring that they run consistently across different environments. Together, `Jenkins` and `Docker` streamline the
development process, improving efficiency, and facilitating the adoption of DevOps practices.

The goal of this assignment was to create two `Jenkins` pipelines that automates the build, test, and deployment of the
applications developed in the second class assignment. The first pipeline will run several stages and produce several
reports for viewing in the `Jenkins` interface. The second pipeline will run the same stages but will deploy the
application to a `Docker` container and then publish the container to `Docker Hub`.

Issues should be created in GitHub for each main task that was completed. To see how to create issues please refer
to the [Creating Issues](../../ca1/README.md) section on the Class Assignment 1 report.

This Class Assignment was made by the student José Castro nº1231836 of class B and the outcome of the work undertaken in
this assignment can be found [here](https://github.com/J2PCastro/devops-23-24-PSM-1231836).

# Table of Contents

- [Setup](#setup)
    - [Use docker-compose to automate the creation of the two services and network](#use-docker-compose-to-automate-the-creation-of-the-two-services-and-network)
    - [Access Jenkins](#access-jenkins)
    - [Plugins](#plugins)
- [Pipelines](#pipelines)
    - [Pipeline 1](#pipeline-1)
    - [Pipeline 2](#pipeline-2)
        - [Docker Hub Credentials](#docker-hub-credentials)
        - [Running the Pipeline](#running-the-pipeline)
- [Conclusion](#conclusion)

# Setup

To get started on this assignment we must first install `Jenkins`. If we analise the document containing the
requirements for this assignment we can see that we have several ways of using `Jenkins`. One of the requirements in the
second pipeline is to use `Docker` to deploy the application to a container and then publish the container
to `Docker Hub`. To do this we need to have a `Jenkins` server with capabilities to run dockers, what is called
a `Docker in Docker` (DinD) setup. This setup is not recommended for production environments, but it is perfect for our
use case and since one of the ways to use `Jenkins` is through `Docker` we will use this method. To do this simply
navigate [here](https://www.jenkins.io/doc/book/installing/docker/) and follow the instructions for you Operating
System.

## Use docker-compose to automate the creation of the two services and network

Following the instructions provided in the above link can be a bit cumbersome. To make it easier we can use a
`docker-compose` file to automate the creation of the two services and network. First let's create a directory in our
machine and inside it, create a `Dockerfile` with the following content that is available in the link above:

```dockerfile
FROM jenkins/jenkins:2.452.1-jdk17
USER root
RUN apt-get update && apt-get install -y lsb-release
RUN curl -fsSLo /usr/share/keyrings/docker-archive-keyring.asc \
  https://download.docker.com/linux/debian/gpg
RUN echo "deb [arch=$(dpkg --print-architecture) \
  signed-by=/usr/share/keyrings/docker-archive-keyring.asc] \
  https://download.docker.com/linux/debian \
  $(lsb_release -cs) stable" > /etc/apt/sources.list.d/docker.list
RUN apt-get update && apt-get install -y docker-ce-cli
USER jenkins
RUN jenkins-plugin-cli --plugins "blueocean docker-workflow"
```

Then let's create a `docker-compose` file with the following content:

```yaml
version: '3'

services:
  jenkins-blueocean:
    build:
      context: .
      dockerfile: Dockerfile
    restart: on-failure
    ports:
      - "8080:8080"
      - "50000:50000"
    volumes:
      - jenkins-data:/var/jenkins_home
      - jenkins-docker-certs:/certs/client:ro
    environment:
      DOCKER_HOST: tcp://docker:2376
      DOCKER_CERT_PATH: /certs/client
      DOCKER_TLS_VERIFY: "1"
    networks:
      - jenkins

  jenkins-docker:
    image: docker:dind
    privileged: true
    restart: always
    ports:
      - "2376:2376"
    volumes:
      - jenkins-docker-certs:/certs/client
    networks:
      - jenkins

networks:
  jenkins:
    driver: bridge

volumes:
  jenkins-data:
  jenkins-docker-certs:
```

These files will create two services, one with `Jenkins` and another with `Docker in Docker`. This way, the process of
creating and then running the `Jenkins` server is automated. To do this simply run the following command in the
directory where the `Dockerfile` and `docker-compose` files are located:

```shell
docker-compose up -d
```

This docker-compose file is an example of how to use `Docker in Docker` with `Jenkins`. For the purpose of this
assignment, in order to explore and experiment different approaches we will use the instructions provided in the link
above. This way we can have a better understanding of how to use `Jenkins` with `Docker` and how to create
a `Dockerfile` to automate the installation of `Jenkins` and the necessary plugins.

## Access Jenkins

After running the above command, `Jenkins` should be running and accessible at `http://localhost:8080`. To access it
simply open a browser and navigate to the address. To finalize the setup
navigate [here](https://www.jenkins.io/doc/book/installing/docker/#setup-wizard) and follow the instructions.

## Plugins

`Jenkins` has a vast plugin ecosystem that provides additional functionality and integrations with various tools and
technologies. For this assignment, if during the setup provided above we followed the instructions to install the
suggested plugins, we should have the necessary plugins installed. If not, we can install them manually in the manage
plugins section of `Jenkins`.

# Pipelines

## Pipeline 1

The first pipeline is a simple pipeline that runs several stages and produces several reports for viewing among other
jobs in the `Jenkins` interface. The pipeline is defined in a `Jenkinsfile` that should be available in the repository.
So to start we should create a new directory in the repository:

```shell
mkdir -p ca5/practice
```

We call this directory `practice` because we will practice creating a `Jenkinsfile` in this directory as requested in
the
assignment. Inside this directory, we should create a `Jenkinsfile` with the following content:

```groovy
pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out...'
                git 'https://github.com/J2PCastro/devops-23-24-PSM-1231836.git'
            }
        }
        stage('Prepare') {
            steps {
                echo 'Setting executable permissions for gradlew...'
                script {
                    if (isUnix()) {
                        dir('ca2/part1') {
                            sh 'chmod +x gradlew'
                        }
                    } else {
                        echo 'Skipping chmod on Windows'
                    }
                }
            }
        }
        stage('Assemble') {
            steps {
                echo 'Assembling ca2/part1 project...'
                script {
                    if (isUnix()) {
                        dir('ca2/part1') {
                            sh './gradlew clean'
                            sh './gradlew assemble'
                        }
                    } else {
                        dir('ca2/part1') {
                            bat 'gradlew.bat clean'
                            bat 'gradlew.bat assemble'
                        }
                    }
                }
            }
        }
        stage('Test') {
            steps {
                echo 'Running tests...'
                script {
                    if (isUnix()) {
                        dir('ca2/part1') {
                            sh './gradlew test'
                        }
                    } else {
                        dir('ca2/part1') {
                            bat 'gradlew.bat test'
                        }
                    }
                }
            }
            post {
                always {
                    echo 'Publishing test results...'
                    junit 'ca2/part1/build/test-results/test/*.xml'
                }
            }
        }
        stage('Archiving') {
            steps {
                echo 'Archiving...'
                archiveArtifacts 'ca2/part1/build/libs/*.jar'
            }
        }
    }
}
```

This `Jenkinsfile` defines a pipeline with several stages. Let's analyze each stage:

1. **Checkout**: This stage checks out the repository containing the project to be built.
2. **Prepare**: This stage sets executable permissions for the `gradlew` script.
3. **Assemble**: This stage assembles the project by cleaning and building it.
4. **Test**: This stage runs the tests for the project and publishes the test results.
5. **Archiving**: This stage archives the built JAR file.

One thing to note here is the use of `junit` in the `post` block of the `Test` stage. Jenkins makes use of several
plugins to provide additional functionality. The `junit` plugin is used to parse JUnit test results and display them in
the Jenkins interface. This is a very useful feature as it allows developers to quickly identify failing tests and take
appropriate action.
The `dir`, `sh`, and `echo` steps are also used in this pipeline. The `dir` step allows you to change the working
directory for subsequent steps. The `sh` step runs shell commands, and the `echo` step prints messages to the console.

To run this pipeline in `Jenkins`, we first must push this file to the remote repository. After that we need to create a
new pipeline job and point it to the `Jenkinsfile` in the repository. To do this, follow these steps:

1. Open `Jenkins` in a browser and log in.
2. Click on `New Item` to create a new pipeline job.
3. Enter a name for the job and select `Pipeline` as the job type.
4. Click `OK` to create the job.
5. In the job configuration, scroll down to the `Pipeline` section.
6. Select `Pipeline script from SCM` as the definition.
7. Choose `Git` as the SCM and enter the repository URL.
8. In the `Script Path` field, enter the path to the `Jenkinsfile` (e.g., `ca5/practice/Jenkinsfile`).
9. Click `Save` to save the job configuration.

Note that in step `7` we are using the repository URL of the repository used for the DevOps class and since in previous
assignments it was required to make that repository public we didn't need to specify any credentials. If it were a
private repository we would need to provide these credentials to `Jenkins` and then configure the pipeline to use them.
This was done in the second pipeline but for authentication in `Docker Hub`. This will be explained in the appropriate
section further down the report.

After saving the job configuration, you can run the pipeline by clicking on `Build Now`. This will trigger the pipeline,
and you will be able to see the progress and results in the `Jenkins` interface. The pipeline will run the stages
defined in the `Jenkinsfile` and produce the reports specified in the `Test` stage. These reports can be viewed in the
`Jenkins` interface, providing valuable information about the build and test results. If by any reason the pipeline
fails, `Jenkins` will provide detailed information about the failure, helping you to identify and fix the issue. Also,
we
can access a console output of the pipeline by clicking on the build number and then on `Console Output`.

## Pipeline 2

The second pipeline is similar to the first one but with an additional stage that deploys the application to a `Docker`
container and then publishes the container to `Docker Hub`. This pipeline is also defined in a `Jenkinsfile` that should
be available in the repository. Since one of the requirements is to deploy the application to a `Docker` container we
should have a `Dockerfile` in the repository. To do this we should create a `Dockerfile` in the ca5 directory with the
following content:

```dockerfile
FROM tomcat:10.1.24-jdk17-temurin-jammy
LABEL authors="José Castro"

# Remove the default ROOT webapp to avoid conflicts
RUN rm -rf /usr/local/tomcat/webapps/ROOT

# Copy the WAR file from the ca2 directory to the webapps directory
COPY ./ca2/part2/react-and-spring-data-rest-basic/build/libs/react-and-spring-data-rest-basic-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/

# Expose port 8080
EXPOSE 8080

# Start Tomcat
CMD ["catalina.sh", "run"]
```

This `Dockerfile` defines a `Docker` image based on the `tomcat:10.1.24-jdk17-temurin-jammy` image. It removes the
default `ROOT` webapp to avoid conflicts and copies the WAR file from the `ca2/part2/react-and-spring-data-rest-basic`
directory to the `webapps` directory in the `Tomcat` container. It exposes port `8080` and starts `Tomcat` when the
container is run.

With the `Dockerfile` in place, we can now create the `Jenkinsfile` for the second pipeline. To do this we should create
a `Jenkinsfile` in the same directory as the `Dockerfile` with the following content:

```groovy
pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out...'
                git 'https://github.com/J2PCastro/devops-23-24-PSM-1231836.git'
            }
        }
        stage('Prepare') {
            steps {
                echo 'Setting executable permissions for gradlew...'
                script {
                    if (isUnix()) {
                        dir('ca2/part2/react-and-spring-data-rest-basic') {
                            sh 'chmod +x gradlew'
                        }
                    } else {
                        echo 'Skipping chmod on Windows'
                    }
                }
            }
        }
        stage('Assemble') {
            steps {
                echo 'Assembling ca2/part2/react-and-spring-data-rest-basic project...'
                script {
                    if (isUnix()) {
                        dir('ca2/part2/react-and-spring-data-rest-basic') {
                            sh './gradlew clean'
                            sh './gradlew assemble'
                        }
                    } else {
                        dir('ca2/part2/react-and-spring-data-rest-basic') {
                            bat 'gradlew.bat clean'
                            bat 'gradlew.bat assemble'
                        }
                    }
                }
            }
        }
        stage('Test') {
            steps {
                echo 'Running tests...'
                script {
                    if (isUnix()) {
                        dir('ca2/part2/react-and-spring-data-rest-basic') {
                            sh './gradlew test'
                        }
                    } else {
                        dir('ca2/part2/react-and-spring-data-rest-basic') {
                            bat 'gradlew.bat test'
                        }
                    }
                }
            }
            post {
                always {
                    echo 'Publishing test results...'
                    junit 'ca2/part2/react-and-spring-data-rest-basic/build/test-results/test/*.xml'
                }
            }
        }
        stage('Javadoc') {
            steps {
                echo 'Generating Javadoc...'
                script {
                    if (isUnix()) {
                        dir('ca2/part2/react-and-spring-data-rest-basic') {
                            sh './gradlew javadoc'
                        }
                    } else {
                        dir('ca2/part2/react-and-spring-data-rest-basic') {
                            bat 'gradlew.bat javadoc'
                        }
                    }
                }
                echo 'Archiving and publishing Javadoc...'
                publishHTML(target: [allowMissing         : false,
                                     alwaysLinkToLastBuild: false,
                                     keepAll              : true,
                                     reportDir            : '/var/jenkins_home/workspace/ca5_pipeline/ca2/part2/react-and-spring-data-rest-basic/build/docs/javadoc',
                                     reportFiles          : 'index.html',
                                     reportName           : 'Javadoc Report'])
            }
        }
        stage('Archiving') {
            steps {
                echo 'Archiving...'
                archiveArtifacts 'ca2/part2/react-and-spring-data-rest-basic/build/libs/*.war'
            }
        }
        stage('Docker') {
            steps {
                script {
                    echo 'Building and publishing Docker image...'
                    def dockerImage = docker.build("josepedrocastro/react-and-spring-data-rest-basic:${env.BUILD_ID}", "-f ./ca5/Dockerfile .")
                    docker.withRegistry('https://index.docker.io/v1/', 'docker-id') {
                        dockerImage.push()
                    }
                }
            }
        }
    }
}
```

This `Jenkinsfile` defines a pipeline with several stages, similar to the first pipeline. Let's analyze each stage:

1. **Checkout**: This stage checks out the repository containing the project to be built.
2. **Prepare**: This stage sets executable permissions for the `gradlew` script.
3. **Assemble**: This stage assembles the project by cleaning and building it.
4. **Test**: This stage runs the tests for the project and publishes the test results.
5. **Javadoc**: This stage generates Javadoc for the project and publishes it as an HTML report.
6. **Archiving**: This stage archives the built WAR file.
7. **Docker**: This stage builds a Docker image based on the `Dockerfile` in the repository and publishes it to Docker
   Hub.

One thing to note here is the use of `publishHTML` in the `Javadoc` stage. This plugin is used to publish an HTML report
generated by the `Javadoc` task in the project. The report is archived and can be viewed in the `Jenkins` interface,
providing additional information about the project.

The `Docker` stage is the new stage in this pipeline. It uses the `docker.build` and `docker.withRegistry` steps to
build
a Docker image based on the `Dockerfile` in the repository and publish it to `Docker Hub`. The `docker.build` step
creates a Docker image with a specific tag based on the build ID. The `docker.withRegistry` step authenticates with
`Docker Hub` using the credentials provided in the `Jenkins` configuration. The `dockerImage.push` step pushes the
Docker image to `Docker Hub`.

After both these files are created, we must push them to the remote repository. After that, we need to create a new
pipeline job in `Jenkins` and point it to the `Jenkinsfile` in the repository. However, since we are going to publish
the
Docker image to `Docker Hub` we need to provide credentials to `Jenkins` to authenticate with `Docker Hub`.

### Docker Hub Credentials

To provide credentials to `Jenkins` to authenticate with `Docker Hub`, follow these steps:

1. Open `Jenkins` in a browser and log in.
2. Click on `Manage Jenkins` in the sidebar.
3. Click on `Credentials`.
4. Click on `System`.
5. Click on `Global credentials (unrestricted)`.
6. Click on `Add Credentials`.
7. Select `Username with password` as the kind.
8. Enter your `Docker Hub` username and password.
9. Enter an ID for the credentials (e.g., `docker-id`).
10. Click `OK` to save the credentials.

This creates a new set of credentials that `Jenkins` can use to authenticate with `Docker Hub`. If we pay attention to
the
`Docker` stage in the `Jenkinsfile` we can see that we are using the `docker.withRegistry` step to authenticate with
`Docker Hub` using the credentials we just created. This step allows us to push the Docker image to `Docker Hub` without
having to provide the credentials in the `Jenkinsfile`.

### Running the Pipeline

To run this pipeline in `Jenkins` we follow the same steps as before but with a slight change to the script path in the
job configuration. To do this, follow these steps:

1. Open `Jenkins` in a browser and log in.
2. Click on `New Item` to create a new pipeline job.
3. Enter a name for the job and select `Pipeline` as the job type.
4. Click `OK` to create the job.
5. In the job configuration, scroll down to the `Pipeline` section.
6. Select `Pipeline script from SCM` as the definition.
7. Choose `Git` as the SCM and enter the repository URL.
8. In the `Script Path` field, enter the path to the `Jenkinsfile` (e.g., `ca5/Jenkinsfile`).
9. Click `Save` to save the job configuration.

After saving the job configuration, you can run the pipeline by clicking on `Build Now`. This will trigger the pipeline,
and you will be able to see the progress and results in the `Jenkins` interface. The pipeline will run the stages
defined in the `Jenkinsfile` and publish the Docker image to `Docker Hub`. If by any reason the pipeline
fails, `Jenkins` will provide detailed information about the failure, helping you to identify and fix the issue. Also,
we can access a console output of the pipeline by clicking on the build number and then on `Console Output`.

Now we see some differences in the `Jenkins` interface. We can see the test results, the archived artifacts, and now in
the sidebar we can also se a new option that allows us to check the Javadoc generated during the execution of the
pipeline.

If we navigate to `Docker Hub` we can see the image published [here](https://hub.docker.com/u/josepedrocastro). This is
a very useful feature as it allows us to easily share and distribute our applications using Docker images. This is
especially useful in modern software development pipelines where applications are built and deployed using containers.

# Conclusion

In conclusion, this report provides a comprehensive overview of the fifth class assignment, which focuses on `Jenkins`,
a powerful open-source automation server used for continuous integration and continuous delivery pipelines. The
assignment involves creating two `Jenkins` pipelines that automate the build, test, and deployment of the applications
developed in the second class assignment.

The first pipeline runs several stages and produces several reports for viewing in the `Jenkins` interface. The second
pipeline runs the same stages but also deploys the application to a `Docker` container and then publishes the container
to`Docker Hub`. This process is automated using a `Jenkinsfile` and a `Dockerfile`, which define the stages of the
pipeline and the `Docker image`, respectively.

The report also discusses the use of various `Jenkins` plugins and steps, such
as `junit`, `publishHTML`, `docker.build`, and`docker.withRegistry`, which provide additional functionality and
integrations with various tools and technologies. The use of these plugins and steps demonstrates the flexibility and
extensibility of `Jenkins`, making it a valuable tool in modern software development pipelines.

Furthermore, the report explains the process of providing credentials to `Jenkins` for authenticating with `Docker Hub`,
demonstrating the ability to securely handle sensitive information in `Jenkins`.

Overall, this exercise provided a comprehensive understanding of `Jenkins` and its role in automating the software
development lifecycle. It also emphasized the importance of understanding `Jenkinsfiles`, `Dockerfiles`, and `Jenkins`
plugins in software development.