# Class Assignment 2 - Part 2 - Report

## Introduction

This report is the second part of the second class assignment. The goal of this assignment was to use our previously
acquired knowledge of the build tool Gradle and apply it to this assignment. In the first class assignment, an example
application was provided with the objective of learning how to use Git. That application was built using Maven. The goal
of this assignment was to convert that application to use Gradle instead of Maven, and to develop tasks to automate the
build process.

To do so, it was required to download a Gradle spring boot project from
the [Spring Initializr](https://start.spring.io/) with certain configurations that will be specified later in this
report. After that, the project was imported into IntelliJ IDEA and the necessary changes were made to the project to
make it work as expected. As in the previous assignment Git will be used as the version control system, and basic
knowledge and understanding of Git is required.

Issues should be created in GitHub for each task. To see how to create issues please refer to
the [Creating Issues](../../ca1/README.md) section on the Class Assignment 1 report.

This Class Assignment was made by the student José Castro nº1231836 of class B and the outcome of the work undertaken in
this assignment can be found [here](https://github.com/J2PCastro/devops-23-24-PSM-1231836), more specifically in the
ca2/part2/react-and-spring-data-rest-basic directory.

# Table of Contents

- [Setup](#setup)
    - [Creating a new branch](#creating-a-new-branch)
    - [Setting up the project](#setting-up-the-project)
    - [Adding the frontend plugin](#adding-the-frontend-plugin)
    - [Configuring the frontend plugin](#configuring-the-frontend-plugin)
    - [Fixing compilation errors](#fixing-compilation-errors)
- [Assignment Tasks](#assignment-tasks)
    - [Task 1](#task-1)
    - [Task 2](#task-2)
- [Push changes and merge](#push-changes-and-merge)
- [Alternative to Gradle: Maven](#alternative-to-gradle-maven)
    - [Using Maven to complete this assignment](#using-maven-to-complete-this-assignment)
        - [Setting up the project](#setting-up-the-project-1)
        - [Adding the frontend plugin](#adding-the-frontend-plugin)
        - [Adding the frontend plugin configuration](#adding-the-frontend-plugin-configuration)
        - [Building the project](#building-the-project)
        - [Goal 1](#goal-1)
        - [Goal 2](#goal-2)
- [Conclusion](#conclusion)

# Setup

## Creating a new branch

Let's start by creating a new directory in the repository:

```bash
cd path/to/repository/ca2
mkdir part2
cd part2
```

- `cd path/to/repository` is the path to the repository where the project will be stored.
- `mkdir part2` creates a new directory called part2.
- `cd part2` changes the current directory to the part2 directory.

Before continuing with the assignment, we should create a new branch on the repository to work on. This is a good
practice to keep the main branch clean and to separate the work being done. To create a new branch, open a bash terminal
and run the following commands:

```bash
cd path/to/repository
git checkout -b <branch-name>
```

## Setting up the project

To start this assignment, it was necessary to download a Gradle spring boot project from
the [Spring Initializr](https://start.spring.io/) with the following configurations:

- Project: Gradle - Groovy
- Language: Java
- Spring Boot: 3.2.4
- Project Metadata:
    - Group: com.greglturnquist
    - Artifact: react-and-spring-data-rest-basic
    - Name: react-and-spring-data-rest-basic
    - Description: Demo project for Spring Boot
    - Package Name: com.greglturnquist.payroll
    - Packaging: Jar
    - Java: 17
- Dependencies:
    - Rest Repositories
    - Thymeleaf
    - Spring Data JPA
    - H2 Database

Before proceeding let's take a quick overview of some of the configurations:

- **Gradle - Groovy**: This is the build tool that will be used in this project. Gradle is a build automation tool that
  is an open-source build automation system that builds upon the concepts of Apache Ant and Apache Maven and introduces
  a Groovy-based domain-specific language (DSL) instead of the XML form used by Apache Maven for declaring the project
  configuration.
- **Java 17**: This is the version of Java that will be used in this project. Java 17 is the latest LTS version of Java
  and it is recommended to use the latest version of Java to take advantage of the latest features and improvements.
- **Spring Boot 3.2.4**: This is the version of Spring Boot that will be used in this project. Spring Boot is an open
  source Java-based framework used to create micro-services. It is developed by Pivotal Team and is used to build
  stand-alone and production-ready spring applications.
- **Packaging: Jar**: This is the packaging that will be used in this project. The packaging is the format used to
  distribute the project. In this case, the project will be distributed as a Jar file.
- **Dependencies**:
    - **Rest Repositories**: This dependency will be used to create RESTful web services using Spring Data REST. Spring
      Data REST is a part of the larger Spring Data project that makes it easy to build hypermedia-driven REST web
      services on top of Spring Data repositories.
    - **Thymeleaf**: This dependency will be used to create web applications using Thymeleaf. Thymeleaf is a modern
      server-side Java template engine for web and standalone environments. It is a better alternative to JSP and other
      template engines.
    - **Spring Data JPA**: This dependency will be used to create JPA repositories using Spring Data JPA. Spring Data
      JPA is a part of the larger Spring Data project that makes it easy to implement JPA-based repositories using
      Spring.
    - **H2 Database**: This dependency will be used to create an in-memory database using H2 Database. H2 is an open
      source database management system written in Java. It can be embedded in Java applications or run in the client
      server mode.

After downloading the project from the Spring Initializr, we should now unzip it into the correct directory. Open up a
bash terminal and run the following command:

```bash
unzip path/to/downloaded/project.zip -d .
```

- `unzip` is a command-line utility for unpacking zip archives.
- `unzip path/to/downloaded/project.zip -d .` unzips the downloaded project into the current directory.
- Because in the previous step we positioned ourselves in the part2 directory, the project will be unzipped into the
  part2 directory.

Our part2 directory should now contain a new directory called react-and-spring-data-rest-basic. This directory contains
the project that was downloaded from the Spring Initializr. Let's move inside the project directory:

```bash
cd react-and-spring-data-rest-basic
```

After unzipping the project we now have an "empty" spring application that can be built using Gradle. Since Gradle
already has a few built in tasks, we can use the following command to see the available tasks:

```bash
./gradlew tasks
```

Now continuing with the step-up process, let's delete the src directory since we want to use the code from the previous
assignment. To do so we can use the following command:

```bash
rm -r ./src
```

After deleting the src directory, we should now copy the src directory from the previous assignment into the current
project We should also copy two other necessary files. To do so we can use the following command:

```bash
cp -r ./../../../ca1/basic/src . && cp ./../../../ca1/basic/webpack.config.js ./../../../ca1/basic/package.json .
```

- `cp -r ./../../../ca1/basic/src .` copies the src directory from the previous assignment into the current project.
- `cp ./../../../ca1/basic/webpack.config.js ./../../../ca1/basic/package.json .` copies the webpack.config.js and
  package.json files from the previous assignment into the current project.

The command shown above is an efficient way of copying multiple files from one directory to another. This way, the sc
directory and all it's contents are copied to the current project directory. The same is done for the webpack.config.js
and package.json files.
The webpack.config.js file is used to configure webpack, a module bundler for JavaScript applications. The package.json
file is used to define the properties of the project, such as the name, version, and dependencies. Both this files aro
also necessary in this new assignment.

The next step is to delete `src/main/resources/static/built/` directory since this folder should be generated from the
JavaScript by the webpack tool

```bash
rm -r ./src/main/resources/static/built/
```

After this first configuration and set up, we should commit the changes to the repository:

```bash
git add .
git commit -m "Create part2 directory and configure it. closes #<issue-number>"
```

We can now experiment with the application by using the following command:

```bash
./gradlew bootRun
```

After this open up a browser and go [here](http://localhost:8080/) to see the application running. As we can see, the
web page is empty. This is because gradle is missing the plugin for dealing with the frontend code. To fix this we need
to add the Gradle plugin org.siouan.frontend so that Gradle is also able to manage the frontend.

### Adding the frontend plugin

Let's open our project in IntelliJ IDEA and add the plugin to the build.gradle file.

To do so, open the build.gradle file and add the following lines to the plugins section:

```groovy
id "org.siouan.frontend-jdk17" version "8.0.0"
```

Notice the version of Java chose in the Spring Initializr was Java 17, so we need to use the plugin version that is
compatible with Java 17. The org.siouan.frontend Gradle plugin is a plugin that helps manage frontend dependencies and
build processes within a Gradle-based project. It's particularly useful when working with projects that have both
backend (Java, Kotlin, etc.) and frontend (JavaScript, TypeScript, etc.) components.

Here we find something relatively new, a Gradle plugin. A Gradle plugin is a piece of software that extends Gradle's
functionality, enabling it to perform specific tasks or integrate with external tools and systems. Plugins can define
new tasks, configurations, and dependencies, making it easier to automate build processes and manage project
dependencies within a Gradle-based project.

### Configuring the frontend plugin

After this we should configure the plugin by adding the following lines to the build.gradle file:

```groovy
frontend {
    nodeVersion = "16.20.2"
    assembleScript = "run build"
    cleanScript = "run clean"
    checkScript = "run check"
}
```

The frontend block is used to configure the frontend plugin. The nodeVersion property is used to specify the version of
Node.js that will be used by the plugin. The assembleScript property is used to specify the script that will be executed
when the assemble task is run. The cleanScript property is used to specify the script that will be executed when the
clean task is run. The checkScript property is used to specify the script that will be executed when the check task is
run.

And finally, let's update the scripts section/object in package.json to configure the execution of
webpack:

```json
"scripts": {
"webpack": "webpack",
"build": "npm run webpack",
"check": "echo Checking frontend",
"clean": "echo Cleaning frontend",
"lint": "echo Linting frontend",
"test": "echo Testing frontend"
},
```

### Fixing compilation errors

In order to prevent a few compilation errors, we should make a few changes to the Employee.java file and to the
package.json file.
In the Employee.java file navigate to lines 18, 19 and 20 and change to the following:

```java
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
```

In the package.json file, right before the previously updated scripts object, add the following:

```json
"packageManager": "npm@9.6.7",
```

Both these changes ensure that the project compiles and runs without any issues.

After making these changes, we should commit the changes to the repository:

```bash
git add .
git commit -m "Add dependencies and plugins. closes #<issue-number>"
```

# Assignment Tasks

Now that all the initial setup is done, we can start working on the assignment tasks. Before doing so however let's
build the project:

```bash
./gradlew build
```

This command will build the project and run all the tests. If everything is working as expected, the build should be
successful. If there are any issues, they should be fixed before proceeding with the assignment tasks. Check the console
output. Notice that the tasks related to the frontend are also executed and the frontend code is generated. This is
because of the frontend plugin that we added to the build.gradle file.

We can also execute the application by running the following command:

```bash
./gradlew bootRun
```

Before proceeding any further, let's create a `.gitignore` file to ignore some directories and files that should not
be committed to the repository. Inside the part1 directory run the following commands:

```bash
touch .gitignore
nano .gitignore
```

Add the following lines to the `.gitignore` file:

```gitignore
HELP.md
.gradle
build/
!gradle/wrapper/gradle-wrapper.jar
!**/src/main/**/build/
!**/src/test/**/build/

### STS ###
.apt_generated
.classpath
.factorypath
.project
.settings
.springBeans
.sts4-cache
bin/
!**/src/main/**/bin/
!**/src/test/**/bin/

### IntelliJ IDEA ###
.idea
*.iws
*.iml
*.ipr
out/
!**/src/main/**/out/
!**/src/test/**/out/

### NetBeans ###
/nbproject/private/
/nbbuild/
/dist/
/nbdist/
/.nb-gradle/

### VS Code ###
.vscode/

# Ignore Gradle project-specific cache directory
.gradle/

.idea/*

/.idea/

.DS_Store/

backup/

*.swp

src.zip

src/main/resources/static/built/

.frontend-gradle-plugin/

node/

node_modules/

dist/
```

## Task 1

The first tasks asks us to create a new task to copy the generated jar to a folder named `dist` located at
the project root folder level. To do so we need to add the following code to the build.gradle file:

```groovy
task copyJar(type: Copy, dependsOn: build) {
    group = "DevOps"
    description = "Copies the generated JAR to the dist folder"

    from 'build/libs/'
    into 'dist'
    include '*.jar'

    doLast {
        project.file('dist')
    }
}
```

- The task `copyJar` is created using the `task` method. The `type` property is set to `Copy`, which means that this
  task will copy files from one location to another. The `dependsOn` property is set to `build`, which means that this
  task depends on the build task. This means that the build task will be executed before the copyJar task.
- The `group` property is set to "DevOps", which means that this task belongs to the DevOps group. The `description`
  property is set to "Copies the generated JAR to the dist folder", which is a description of what this task does.
- The `from` method is used to specify the source directory from which files will be copied. In this case, the source
  directory is 'build/libs/'.
- The `into` method is used to specify the destination directory to which files will be copied. In this case, the
  destination directory is 'dist'.
- The `include` method is used to specify the files that will be copied. In this case, all files with the .jar extension
  will be copied.
- The `doLast` method is used to perform some actions after the task has been executed. In this case, the project.file
  method is called with the 'dist' directory as an argument. This method returns a File object representing the 'dist'
  directory. This is done to ensure that the 'dist' directory is created if it does not exist.

In summary, the `copyJar` task is created to copy generated JAR files from the 'build/libs/' directory to the 'dist'
directory. It depends on the `build` task, ensuring that the JAR files are copied after they are generated. This task is
grouped under "DevOps" and described as copying JARs to the 'dist' folder. All JAR files in 'build/libs/' are included
for copying. If the `build` task has not been executed prior to `copyJar`, Gradle automatically executes the `build`
task before proceeding with `copyJar`, ensuring the JAR files are available for copying. After execution, the 'dist'
directory is ensured to exist. This task streamlines the process of distributing JAR files within the project.

To test this task, run the following command:

```bash
./gradlew copyJar
```

This command will execute the copyJar task, which will copy the generated JAR file to the 'dist' directory. If the task
is successful, the JAR file should be copied to the 'dist' directory. Check the console output for any errors or
warnings and check the file tree in the project directory to verify that the JAR file has been copied to the 'dist'
directory.

After thoroughly testing the task, we should commit the changes to the repository:

```bash
git add .
git commit -m "Add a task to gradle to copy the generated jar. closes #<issue-number>"
```

## Task 2

The second task asks us to create a new task to delete all the files generated by webpack. These files are usually
located in the `src/main/resources/static/built/` directory. Also, this new task should be executed automatically by
Gradle before the `clean` task. To do so we need to add the following code to the build.gradle file:

```groovy
task deleteWebpackFiles(type: Delete) {
    group = "Clean"
    description = "Deletes files generated by webpack"

    delete 'src/main/resources/static/built/'
}

clean.dependsOn deleteWebpackFiles
```

- The task `deleteWebpackFiles` is created using the `task` method. The `type` property is set to `Delete`, which means
  that
  this task will delete files. The `group` property is set to `Clean`, which means that this task belongs to the Clean
  group. The `description` property is set to `Deletes files generated by webpack`, which is a description of what this
  task does.
- The `delete` method is used to specify the files or directories that will be deleted. In this case, the '
  src/main/resources/static/built/'
  directory will be deleted.
- The `clean.dependsOn deleteWebpackFiles` line specifies that the `deleteWebpackFiles` task should be executed before
  the
  `clean` task. This ensures that the files generated by webpack are deleted before the project is cleaned.

The `clean` task is a built-in task provided by Gradle that cleans the project by deleting all generated files and
directories. By specifying that the `deleteWebpackFiles` task depends on the `clean` task, we ensure that the files
generated by webpack are deleted before the project is cleaned.

To test this task, run the following command:

```bash
./gradlew clean
```

This command will execute the `clean` task, which will delete all generated files and directories.
The `deleteWebpackFiles` task will be executed automatically before the `clean` task, ensuring that the files generated
by webpack are deleted before the project is cleaned. Check the console output for any errors or warnings and check the
file tree in the project directory to verify that the files generated by webpack have been deleted. Notice, that instead
of running the`deleteWebpackFiles` task directly, we are running the `clean` task. This is because
the `deleteWebpackFiles` task is configured to run before the `clean` task.

After thoroughly testing the task, we should commit the changes to the repository:

```bash
git add .
git commit -m "Add task to delete all the files generated by webpack. closes #<issue-number>"
```

# Push changes and merge

After completing all the tasks, and after testing that everything is working as expected, we should push the changes and
the branch to the remote repository and merge this branch with the master branch. To do so, run the following commands:

```bash
git push origin <branch-name>
git checkout master
git merge --no-ff <branch-name>
git push origin master
```

# Alternative to Gradle: Maven

Apache Maven is a widely-used build automation tool that originated from the Apache Software Foundation. It was
initially developed by Jason van Zyl in 2002 as an effort to simplify the build processes in Java projects. Maven
introduced the concept of convention-over-configuration, aiming to reduce the complexity of project setup and management
by enforcing standard project structures and dependencies. Over the years, Maven has evolved into a mature and robust
tool, becoming a staple in the Java development ecosystem for its efficiency and reliability in managing project builds
and dependencies.

It simplifies the process of building, managing dependencies, and executing tasks within a project. Maven operates based
on XML-based configuration files called POM (Project Object Model), which define project settings, dependencies, and
build processes.

Maven's centralized repository system ensures efficient dependency management by automatically downloading and including
project dependencies during the build process. Developers can define custom build lifecycles and phases, allowing for
precise control over the build process and the execution of various tasks. Additionally, Maven's extensive plugin
ecosystem provides additional functionality and integration with various tools and frameworks, further enhancing its
capabilities.

Maven and Gradle are both widely-used build automation tools in software development. Maven relies on XML-based
configuration files, known as POM (Project Object Model), and follows a convention-over-configuration approach. It
simplifies project setup and dependency management by enforcing default project structures and naming conventions.
Gradle, on the other hand, uses a Groovy or Kotlin DSL (Domain-Specific Language) for build configuration, offering more
flexibility and expressiveness. Its task-based execution and incremental build capabilities contribute to faster build
times, especially for larger projects.

In terms of extensibility, both Maven and Gradle support plugin development to enhance their functionality. Maven
plugins integrate seamlessly into the build lifecycle, while Gradle's scripting capabilities allow for custom tasks and
configurations directly within build scripts. Gradle's rich ecosystem of plugins covers a wide range of functionalities,
making it suitable for diverse project requirements. Ultimately, the choice between Maven and Gradle depends on factors
such as project complexity, team preferences, and familiarity with the tools.

## Using Maven to complete this assignment

As both Gradle and Maven are build tools, one could use Maven to accomplish this assignment. Some of the steps used are
interchangeable, meaning, they work for both Gradle and Maven. The changes are outlined below. Let's go through it step
by step:

### Setting up the project

Following the same workflow, when it's time to download the project from Spring Initializr, in the project section,
select **Maven** instead of **Gradle - Groovy**. The rest of the configurations should remain the same.

Continue following the workflow and when it's time to check the available tasks, use the following command:

```bash
./mvnw compiler:help
```

What is referred to as tasks in Gradle, in Maven are called goals. The `compiler:help` goal is used to display a list of
all available goals in the project.

Continue with the workflow and when it's time to experiment with the application use the following command:

```bash
./mvnw spring-boot:run
```

This command will run the application using the Spring Boot Maven plugin. The Spring Boot Maven plugin provides
convenient goals for building and running Spring Boot applications.

If you encounter any issues while trying to run goals using the Maven Wrapper, use this code in order to update the
Maven Wrapper:

```bash
./mvnw -N io.takari:maven:wrapper
```

This command will update the Maven Wrapper to the latest version. This ensures that you have the correct version of the
Maven Wrapper configured for your project, allowing you to use consistent Maven builds across different environments and
systems.

### Adding the frontend plugin

When it's time to add the frontend plugin, in Maven, we don't have a direct equivalent to the frontend plugin in Gradle.
However, we can achieve similar functionality by using the frontend-maven-plugin. This plugin allows us to run frontend
build tools like npm, webpack, or gulp as part of the Maven build process.

To add the frontend-maven-plugin to the project, add the following configuration to the pom.xml file right after
the `<plugins>` tag:

```xml
<plugin>
    <groupId>com.github.eirslett</groupId>
    <artifactId>frontend-maven-plugin</artifactId>
    <version>1.9.1</version>
    <configuration>
        <installDirectory>target</installDirectory>
    </configuration>
    <executions>
        <execution>
            <id>install node and npm</id>
            <goals>
                <goal>install-node-and-npm</goal>
            </goals>
            <configuration>
                <nodeVersion>v12.14.0</nodeVersion>
                <npmVersion>6.13.4</npmVersion>
            </configuration>
        </execution>
        <execution>
            <id>npm install</id>
            <goals>
                <goal>npm</goal>
            </goals>
            <configuration>
                <arguments>install</arguments>
            </configuration>
        </execution>
        <execution>
            <id>webpack build</id>
            <goals>
                <goal>webpack</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

This configuration sets up the frontend-maven-plugin to install Node.js and npm, run npm install, and run webpack build
as part of the Maven build process. The `<installDirectory>` property specifies the directory where Node.js and npm will
be
installed. The `<nodeVersion>` and `<npmVersion>` properties specify the versions of Node.js and npm to install. The
`<arguments>` property specifies the arguments to pass to npm when running the install command.

### Building the project

When it's time to build the project, use the following command:

```bash
./mvnw clean install
```

This command will clean the project, compile the source code, run the tests, and package the application into a JAR
file.
The `install` goal will install the project artifacts into the local Maven repository.

Use this command to run the application:

```bash
./mvnw spring-boot:run
```

In the `.gitignore` file, the same directories and files should be ignored as in the Gradle version. However, let's
change the Gradle specific files for Maven specific files:

```gitignore
### Maven ###
HELP.md
target/
pom.xml.tag
pom.xml.releaseBackup
pom.xml.versionsBackup
pom.xml.next
release.properties
dependency-reduced-pom.xml
buildNumber.properties
.mvn/timing.properties
# https://github.com/takari/maven-wrapper#usage-without-binary-jar
.mvn/wrapper/maven-wrapper.jar

# Eclipse m2e generated files
# Eclipse Core
.project
# JDT-specific (Eclipse Java Development Tools)
.classpath

### Node ###
# Logs
logs
*.log
npm-debug.log*
yarn-debug.log*
yarn-error.log*
lerna-debug.log*
.pnpm-debug.log*

# Diagnostic reports (https://nodejs.org/api/report.html)
report.[0-9]*.[0-9]*.[0-9]*.[0-9]*.json

# Runtime data
pids
*.pid
*.seed
*.pid.lock

# Directory for instrumented libs generated by jscoverage/JSCover
lib-cov

# Coverage directory used by tools like istanbul
coverage
*.lcov

# nyc test coverage
.nyc_output

# Grunt intermediate storage (https://gruntjs.com/creating-plugins#storing-task-files)
.grunt

# Bower dependency directory (https://bower.io/)
bower_components

# node-waf configuration
.lock-wscript

# Compiled binary addons (https://nodejs.org/api/addons.html)
build/Release

# Dependency directories
node_modules/
jspm_packages/

# Snowpack dependency directory (https://snowpack.dev/)
web_modules/

# TypeScript cache
*.tsbuildinfo

# Optional npm cache directory
.npm

# Optional eslint cache
.eslintcache

# Optional stylelint cache
.stylelintcache

# Microbundle cache
.rpt2_cache/
.rts2_cache_cjs/
.rts2_cache_es/
.rts2_cache_umd/

# Optional REPL history
.node_repl_history

# Output of 'npm pack'
*.tgz

# Yarn Integrity file
.yarn-integrity

# dotenv environment variable files
.env
.env.development.local
.env.test.local
.env.production.local
.env.local

# parcel-bundler cache (https://parceljs.org/)
.cache
.parcel-cache

# Next.js build output
.next
out

# Nuxt.js build / generate output
.nuxt
dist

# Gatsby files
.cache/
# Comment in the public line in if your project uses Gatsby and not Next.js
# https://nextjs.org/blog/next-9-1#public-directory-support
# public

# vuepress build output
.vuepress/dist

# vuepress v2.x temp and cache directory
.temp

# Docusaurus cache and generated files
.docusaurus

# Serverless directories
.serverless/

# FuseBox cache
.fusebox/

# DynamoDB Local files
.dynamodb/

# TernJS port file
.tern-port

# Stores VSCode versions used for testing VSCode extensions
.vscode-test

# yarn v2
.yarn/cache
.yarn/unplugged
.yarn/build-state.yml
.yarn/install-state.gz
.pnp.*

### Node Patch ###
# Serverless Webpack directories
.webpack/

# Optional stylelint cache

# SvelteKit build / generate output
.svelte-kit

src/main/resources/static/built/

node/
```

### Goal 1

To create a new goal to copy the generated JAR to a folder named `dist` located at the project root folder level, add
the following configuration to the pom.xml file:

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-resources-plugin</artifactId>
    <version>3.1.0</version>
    <executions>
        <execution>
            <id>copy-jar</id>
            <phase>package</phase>
            <goals>
                <goal>copy-resources</goal>
            </goals>
            <configuration>
                <outputDirectory>./dist</outputDirectory>
                <resources>
                    <resource>
                        <directory>${project.build.directory}</directory>
                        <includes>
                            <include>${project.build.finalName}.jar</include>
                        </includes>
                    </resource>
                </resources>
            </configuration>
        </execution>
    </executions>
</plugin>
```

This configuration sets up the maven-resources-plugin to copy the generated JAR file to the dist directory. The
execution is bound to the package phase, which is the phase where the project artifacts are packaged. The
outputDirectory property specifies the directory where the JAR file will be copied. The resources property specifies the
resources to copy. The resource element specifies the source directory and the files to include.

To test this goal, run the following command:

```bash
./mvnw package
```

This command will execute the package phase, which will compile the source code, run the tests, and package the
application into a JAR file. The maven-resources-plugin will copy the generated JAR file to the dist directory. If we
just want to run the goal, we can use the following command:

```bash
./mvnw resources:copy-resources
```

### Goal 2

To create a new goal to delete all the files generated by webpack, add the following configuration to the pom.xml file:

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-clean-plugin</artifactId>
    <version>3.1.0</version>
    <executions>
        <execution>
            <id>delete-webpack-files</id>
            <phase>clean</phase>
            <goals>
                <goal>clean</goal>
            </goals>
            <configuration>
                <filesets>
                    <fileset>
                        <directory>src/main/resources/static/built</directory>
                        <includes>
                            <include>*</include>
                        </includes>
                    </fileset>
                </filesets>
            </configuration>
        </execution>
    </executions>
</plugin>
```

This configuration sets up the maven-clean-plugin to delete all files in the src/main/resources/static/built directory.
The execution is bound to the clean phase, which is the phase where the project is cleaned. The filesets property
specifies the files to delete. The fileset element specifies the source directory and the files to include.

To test this goal, run the following command:

```bash
./mvnw clean
```

This command will execute the clean phase, which will delete all files in the src/main/resources/static/built directory.

The rest of the workflow is the same as the Gradle version. The result of the implementation of this alternative can be
found [here](https://github.com/J2PCastro/devops-23-24-PSM-1231836), more specifically in the
ca2/part2/part2_maven_alternative directory.

# Conclusion

In conclusion, this report covered the second part of the second class assignment, which focused on using Gradle to
automate the build process of a Spring Boot application. The assignment involved converting a Maven-based Spring Boot
project to a Gradle-based project, configuring the project to use the frontend plugin, and creating tasks to automate
the build process. The report outlined the steps taken to set up the project, configure the frontend plugin, and create
tasks to copy the generated JAR to a dist folder and delete files generated by webpack. The report also provided an
alternative approach using Maven to complete the assignment. By following the steps outlined in this report, the
assignment tasks were successfully completed, and the project was built and tested using Gradle. The report also
discussed the Maven alternative to the Gradle build tool, highlighting the similarities and differences between the two
tools. Overall, the assignment provided valuable hands-on experience with Gradle and build automation, enhancing the
understanding of software development practices and tools.