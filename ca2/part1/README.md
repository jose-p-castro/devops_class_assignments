# Class Assignment 2 / Part 1 - Report

## Introduction

The objective of this report is to furnish a comprehensive overview of the first part of the second class assignment.
Said assignment centered around the use of the build tool Gradle, a pivotal build automation tool in contemporary
software development. In this assignment, focus was directed towards the adept manipulation and customization of a
pre-existing application leveraging Gradle's capabilities.

While the previous assignment delved into version control and collaboration via Git, the attention now shifts towards
the realm of build automation with Gradle. The goal was to refine proficiency in Gradle by crafting and fine-tuning
various tasks within the Gradle build script. Although Git is not the main focus of this assignment, it is still
employed to manage the version control of the project. Basic understanding of Git is assumed, as it was covered in the
previous assignment. The repository used for this assignment was the same as the one used in the previous assignment.

An example application called [gradle_basic_demo](https://bitbucket.org/pssmatos/gradle_basic_demo/) was provided for
this assignment. It was the basis of all the work done
in this assignment, as it was used to develop a few features. However, like mentioned above, the main focus was on the
manipulation of the Gradle build script.

Issues should be created in GitHub for each task that was completed. To see how to create issues please refer
to the [Creating Issues](../../ca1/README.md) section on the Class Assignment 1 report.

This Class Assignment was made by the student José Castro nº1231836 of class B and the outcome of the work undertaken in
this assignment can be
found [here](https://github.com/J2PCastro/devops-23-24-PSM-1231836).

## Table of Contents

1. [Setup](#setup)
2. [Assignment Tasks](#assignment-tasks)
    1. [Copying and committing the example application to the repository](#1-copying-and-committing-the-example-application-to-the-repository)
    2. [Read the instructions and experiment with the application](#2-read-the-instructions-and-experiment-with-the-application)
    3. [Create a new task to execute the server](#3-create-a-new-task-to-execute-the-server)
    4. [Add a unit test and update the Gradle script](#4-add-a-unit-test-and-update-the-gradle-script)
    5. [Create a new task to make a backup of the sources of the application](#5-create-a-new-task-to-make-a-backup-of-the-sources-of-the-application)
    6. [Create a new task to make an archive of the sources of the application](#6-create-a-new-task-to-make-an-archive-of-the-sources-of-the-application)
    7. [Create a new tag to mark the completion of the assignment](#7-create-a-new-tag-to-mark-the-completion-of-the-assignment)
3. [Conclusion](#conclusion)

# Setup

To start working on this assignment, the first step was to clone
the [repository](https://bitbucket.org/pssmatos/gradle_basic_demo/) containing the example application to your local
machine.
This was done by running the following command:

```bash
mkdir <directory-name>
```

Then, move into to the directory and clone the repository:

```bash
cd path/to/directory
git clone <repository-url>
```

# Assignment Tasks

## 1: Copying and committing the example application to the repository

After doing so we should now create a new directory in the local repository to house this new assignment:

```bash
mkdir -p ca2/part1
```

- The `-p` flag is used to create the parent directory if it does not exist.

Then, we should move into the newly created directory:

```bash
cd ca2/part1
```

And copy the contents of the example application into this new directory:

```bash
cp -r path/to/example-application/* .
```

- The `-r` flag is used to copy directories recursively.

Before committing the changes, we should create a `.gitgnore` file to ignore some directories and files that should not
be committed to the repository. Inside the part1 directory run the following commands:

```bash
touch .gitignore
nano .gitignore
```

Add the following lines to the `.gitignore` file:

#Ignore Gradle project-specific cache directory

.gradle/

build/

bin/

.vscode/

.idea/*

/.idea/

.DS_Store/

backup/

.classpath

.project

.settings

*.swp

src.zip

Save the file and exit the editor.

Now we should commit the changes to the
repository:

```bash
git add .
git commit -m "Copy gradle_basic_demo. closes #<issue-number>"
git push
```

## 2: Read the instructions and experiment with the application.

One of the files copied from the example application was the `README.md` file. This file contains the instructions for
experimenting with the application.
This file was later renamed to `README.orig.md` to avoid confusion with the new `README.md` file that will be created
later.

To experiment with the application, we should first build the application. This can be done by running the following
command:

```bash
./gradlew build
```

- This command will build the application and create a `.jar` file in the `build/libs` directory.

After building the application, we should run the server. This can be done by running the following command:

```bash
java -cp build/libs/basic_demo-0.1.0.jar basic_demo.ChatServerApp <server-port>
```

- Replace `<server-port>` with a valid port number, e.g., 59001.

To run a client, we should open another terminal and execute the following gradle task from the project's root
directory:

```bash
./gradlew runClient
```

As you can see, before running any of the tasks above, we should navigate to the project's root directory. This is
important because the `build.gradle` file is located in the project's root directory, and it contains the configuration
for the tasks that we are running. It is also important to note that to run the tasks one must use the `gradlew` script
instead of the `gradle` command. This is because the `gradlew` script is a wrapper script that downloads and runs the
correct version of Gradle for the project. The name used after the `gradlew` script is the name of the task that we want
to run.

## 3: Create a new task to execute the server

Now let's create the first task required for this assignment. This task should execute the server. To do this, we should
open the `build.gradle` file located in the project's root directory. This file contains the configuration for the
project and the tasks that can be run. We should add a new task to this file that executes the server. The task should
be named `runServer` and should execute the server with a default port of 59001. To do so, open the `build.gradle` file
and add the following code:

```groovy
task runServer(type: JavaExec, dependsOn: classes) {
    group = "DevOps"
    description = "Launches a chat server that listens on port 59001"

    classpath = sourceSets.main.runtimeClasspath

    mainClass = 'basic_demo.ChatServerApp'

    args '59001'
}
```

- The `type` property is set to `JavaExec` to indicate that this task should execute a Java class.
- The `dependsOn` property is set to `classes` to indicate that this task depends on the `classes` task. This means that
  the `classes` task will be executed before the `runServer` task.
- The `group` property is set to `DevOps` to group the task under the `DevOps` category in the task list.
- The `description` property is set to `Launches a chat server that listens on port 59001` to provide a description of
  what the task does. This is not mandatory, but it is good practice to provide a description for each task.
- The `classpath` property is set to `sourceSets.main.runtimeClasspath` to specify the classpath for the task. This
  ensures that the task has access to the compiled classes.
- The `mainClass` property is set to `basic_demo.ChatServerApp` to specify the main class that should be executed.
- The `args` property is set to `59001` to specify the arguments that should be passed to the main class. In this case,
  the port number is passed as an argument.

The language used in the `build.gradle` file is Groovy, a dynamic language for the Java platform. Groovy is often used
for writing build scripts in Gradle because of its concise syntax and powerful features.

After adding the task to the `build.gradle` file, we should save the file and run the task to test if everything is
working correctly. To run the task, we should
execute the following command from the project's root directory:

```bash
./gradlew runServer
```

If everything is working correctly, the server should start listening on port 59001. We can test this by running a
client
and connecting to the server just as in the previous step.

After all changes have been made and tested, we should commit the changes to the repository:

```bash
git add .
git commit -m "Add runServer task to build.gradle file. closes #<issue-number>"
git push
```

## 4: Add a unit test and update the gradle script

The next task required for this assignment is to add a unit test for the `App` class. To do this, we should create
a new file named `AppTest.java` in the `src/test/java/basic_demo` directory. This file should contain a unit test for
the `App` class. The unit test should test the `main` method of the `App` class by checking if it prints the correct
message to the console. The unit test should use JUnit, a popular testing framework for Java.

```java
/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package basic_demo;

import org.junit.Test;

import static org.junit.Assert.*;

public class AppTest {
    @Test
    public void testAppHasAGreeting() {
        App classUnderTest = new App();
        assertNotNull("app should have a greeting", classUnderTest.getGreeting());
    }
}
```

After creating the unit test, we should update the `build.gradle` file to include the necessary dependencies for running
the unit test otherwise the test will not run. To do this, open the `build.gradle` file, look for the dependencies
closure, and add the following code before the closing curly brace:

```groovy
    testImplementation 'junit:junit:4.12'
```

This code adds the JUnit dependency to the project. The `testImplementation` configuration is used to specify
dependencies that are required to compile and run tests. The `junit:junit:4.12` dependency specifies the JUnit version
that should be used.
The word 'closure' was mentioned above. A closure is a block of code enclosed in curly braces `{}`. In Groovy, a closure
is an open,
anonymous, block of code that can take arguments, return a value, and be assigned to a variable.

After adding the dependency, we should save the file and run the unit test to check if everything is working correctly.
To run the unit test, we should execute the following command from the project's root directory:

```bash
./gradlew test
```

If everything is working correctly, the unit test should run and pass. We can check the output of the test to see if it
executed successfully.

After running the unit test, we should commit the changes to the repository:

```bash
git add .
git commit -m "Add unit test and update dependencies in build.gradle file. closes #<issue-number>"
git push
```

## 5: Create a new task to make a backup of the sources of the application

The next task required for this assignment is to create a new task that makes a backup of the sources of the
application.
It should copy the contents of the `src` directory to a new directory named `backup` in the project's root directory. To
do this, we should open the `build.gradle` file and add a new task named `copySources` with the following code:

```groovy
task copySources(type: Copy, dependsOn: classes) {
    group = "DevOps"
    description = "Copies the contents of the src folder to a new backup folder"

    from 'src'
    into 'backup'

    doLast {
        project.file('backup').mkdirs()
    }
}
```

- The `type` property is set to `Copy` to indicate that this task should copy files.
- The `dependsOn` property is set to `classes` to indicate that this task depends on the `classes` task. This means that
  the `classes` task will be executed before the `copySources` task.
- The `group` property is set to `DevOps` to group the task under the `DevOps` category in the task list.
- The `description` property is set to `Copies the contents of the src folder to a new backup folder` to provide a
  description of what the task does.
- The `from` property is set to `src` to specify the source directory that should be copied.
- The `into` property is set to `backup` to specify the destination directory where the files should be copied.
- The `doLast` closure is used to create the destination directory if it does not exist. This is done by calling the
  `mkdirs()` method on the `project.file('backup')` object.
    - The `project` object is a reference to the project that the build script is configuring. It provides access to
      various
      properties and methods that can be used to configure the project.
    - The `file()` method is used to create a `File` object that represents the `backup` directory.
    - The `mkdirs()` method is used to create the directory if it does not exist.
- The `doLast` block is executed after the task has completed its main work. It is used to perform additional actions
  after the main work of the task is done.

Here we come face to face with a few new things. The `Copy` type is a standard Gradle task type provided by Gradle's
build script DSL (Domain Specific Language) and does not require any additional plugins to use. Developers can use the
copy task directly within their Gradle build scripts to manage file copying operations as needed.
The use of a `doLast` block is also new. This block is used to execute additional actions after the main work of the
task is done. It is a common practice to use the `doLast` block to perform cleanup tasks or additional actions that
should be executed after the main work of the task is completed. Here it is used to create the destination directory if
it does not exist.

After adding the task to the `build.gradle` file, we should save the file and run the task to test if everything is
working correctly. To run the task, we should execute the following command from the project's root directory:

```bash
./gradlew copySources
```

If everything is working correctly, the contents of the `src` directory should be copied to the `backup` directory. We
can check the `backup` directory to see if the files were copied successfully.

After running the task, we should commit the changes to the repository:

```bash
git add .
git commit -m "Add new task of type Copy to build.gradle file. closes #<issue-number>"
git push
```

## 6: Create a new task to make an archive of the sources of the application

The next task required for this assignment is to create a new task that makes an archive of the sources of the
application. It should create a zip file containing the contents of the `src` directory and save it in the project's
root directory in a zip file. To do this, we should open the `build.gradle` file and add a new task
named `zipSources` with the following code:

```groovy
task zipSources(type: Zip, dependsOn: classes) {
    group = "DevOps"
    description = "Zips the contents of the src folder"

    archiveFileName = 'src.zip'
    destinationDirectory = file('.')

    from 'src'
}
```

- The `type` property is set to `Zip` to indicate that this task should create a zip file.
- The `dependsOn` property is set to `classes` to indicate that this task depends on the `classes` task. This means that
  the `classes` task will be executed before the `zipSources` task.
- The `group` property is set to `DevOps` to group the task under the `DevOps` category in the task list.
- The `description` property is set to `Zips the contents of the src folder` to provide a description of what the task
  does.
- The `archiveFileName` property is set to `src.zip` to specify the name of the zip file that should be created.
- The `destinationDirectory` property is set to `file('.')` to specify the destination directory where the zip file
  should be saved. The `file()` method is used to create a `File` object that represents the project's root directory.
- The `from` property is set to `src` to specify the source directory that should be zipped.

Here we encounter a new task type, `Zip`. The `Zip` type in Gradle refers to a task that creates a ZIP archive
containing specified files or directories. This task is used for bundling files together into a single compressed
archive, commonly used for packaging and distribution purposes. The `Zip` task is built into Gradle and can be utilized
directly within build scripts to create ZIP archives as part of the build process.

After adding the task to the `build.gradle` file, we should save the file and run the task to test if everything is
working correctly. To run the task, we should execute the following command from the project's root directory:

```bash
./gradlew zipSources
```

If everything is working correctly, a zip file named `src.zip` should be created in the project's root directory
containing the contents of the `src` directory. We can check the zip file to see if the contents were zipped
successfully.

After running the task, we should commit the changes to the repository:

```bash
git add .
git commit -m "Add new task of type Zip to build.gradle file. closes #<issue-number>"
git push
```

## 7: Create a new tag to mark the completion of the assignment

The final task required for this assignment is to create a new tag to mark the completion of the assignment. To do this,
we should create a new tag in the repository with the name `ca2-part1`. To create the tag, we should execute the
following command from the project's root directory:

```bash
git tag ca2-part1
git push origin ca2-part1
```

# Conclusion

In conclusion, this report has provided a detailed overview of the first part of the second class assignment, which
focused on utilizing Gradle as a build automation tool in software development. Throughout the assignment, emphasis was
placed on manipulating and customizing a pre-existing application using Gradle's functionalities.

The assignment began with setting up the project by cloning the example application repository and configuring the
environment. Subsequently, tasks were undertaken to understand and experiment with the application, followed by the
creation of custom Gradle tasks to enhance the build process.

Noteworthy tasks included copying and committing the example application to the repository, creating a task to execute
the server, adding a unit test and updating the Gradle script, creating tasks to make backups and archives of the
application sources, and finally, creating a tag to signify the completion of the assignment.

Throughout the report, various commands and configurations were provided to facilitate the execution of tasks and ensure
a comprehensive understanding of the Gradle build process. By completing these tasks, the objective of refining
proficiency in Gradle and understanding its role in modern software development was achieved.

Moving forward, the skills acquired in this assignment will serve as a strong foundation for further exploration and
application of build automation principles in software development endeavors.