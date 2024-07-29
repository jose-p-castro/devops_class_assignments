# Class Assignment 3 / Part 1 - Report

## Introduction

The objective of this report is to furnish a comprehensive overview of the first part of the third class assignment.
Said assignment centered around virtualization with the use of Virtual Box, a free and open-source software developed by
Oracle. It allows users to run multiple operating systems (such as Windows, Linux, macOS, etc.) simultaneously on a
single physical machine, acting as if they were separate computers.

Virtualization is the process of creating a virtual (rather than actual) version of something, such as a server, storage
device, network, or operating system. It's achieved through software called a hypervisor, which sits between the
hardware and the virtual machine instances and manages the allocation of resources.

A hypervisor, also known as a virtual machine monitor (VMM), is responsible for creating and managing virtual machines
on a physical host. There are two types of hypervisors: Type 1, which runs directly on the host's hardware (bare-metal),
and Type 2, which runs on top of the host's operating system.

Advantages of virtualization include optimized resource utilization, enabling multiple virtual machines to operate on a
single physical host, reducing hardware costs, and energy consumption. This consolidation streamlines management efforts
and enhances security by providing isolation between virtual machines. Additionally, virtualization offers flexibility
and scalability, facilitating quick provisioning of new virtual machines and enabling robust disaster recovery
strategies.

However, virtualization presents challenges such as performance overhead, as running multiple virtual machines on a
single host can lead to resource contention and diminish overall performance. Managing virtualized environments can be
complex, requiring specialized skills, and knowledge. Moreover, virtualization introduces a single point of failure, and
security concerns such as hypervisor vulnerabilities and VM escape attacks must be addressed to safeguard virtualized
infrastructure and data.

The goal of the part 1 of this assignment is to practice with VirtualBox using the same projects from the previous
assignments but now inside a VirtualBox Virtual Machine (VM) with Ubuntu.

Issues should be created in GitHub for each task that was completed. To see how to create issues please refer
to the [Creating Issues](../../ca1/README.md) section on the Class Assignment 1 report.

This Class Assignment was made by the student José Castro nº1231836 of class B and the outcome of the work undertaken in
this assignment can be found [here](https://github.com/J2PCastro/devops-23-24-PSM-1231836).

## Table of Contents

- [Setup](#setup)
- [Experimenting with VirtualBox](#experimenting-with-virtualbox)
    - [Setting Up the Environment](#setting-up-the-environment)
    - [Cloning the Repository](#cloning-the-repository)
    - [Running the Projects](#running-the-projects)
        - [Spring Boot Tutorial Basic Project](#spring-boot-tutorial-basic-project)
        - [Gradle_Basic_Demo Project](#gradle_basic_demo-project)
        - [Why should we launch the client side in host and server side in guest?](#why-should-we-launch-the-client-side-in-host-and-server-side-in-guest)
- [Conclusion](#conclusion)

# Setup

First of all, before starting with the actual assignment tasks, it is necessary to set up the VirtualBox VM with Ubuntu.
This was achieved by following the "Hands-On" section of the lecture slides, which provided a step-by-step guide on how
to create a new VM in VirtualBox and install Ubuntu on it.

Before proceeding, some notes on the setup process:

- The ISO file that should be used for the Ubuntu Server installation is the Ubuntu 22.04.4 LTS (64-bit) version. This
  is the preferred version in order to prevent some compatibility issues that may arise with the older versions.
- Do not install openjdk 8, as it may cause some issues with the projects from the previous assignments. Instead,
  install
  openjdk 17.

After following the guide provided in the lecture slides, you should now have:

- A new VM in VirtualBox with Ubuntu Server installed on it.
- A host-only network adapter configured in the VM settings.
- net-tools installed in the VM.
- The `01-netcfg.yaml` file configured to set up the host-only network adapter.
- The `openssh-server` package installed in the VM.
- The `sshd_config` file configured to allow PasswordAuthentication.
- Git installed in the VM.
- OpenJDK 17 installed in the VM.
- And a few other tools not covered by this assignment.

With all these configurations in place you can, if needed, log in to the VM using SSH from the host machine.

# Experimenting with VirtualBox

Now that the setup is complete, it's time to start experimenting with VirtualBox. To do so the previous projects from
the first and second class assignments should be used.

## Setting Up the Environment

Before starting with the projects, it is necessary to set up the environment in the VM by installing the necessary
dependencies. The projects from the previous assignments use four main technologies: Git, Java, Maven, and Gradle. Both
Git and Java are already installed in the VM, so the only dependencies that need to be installed are Maven and Gradle.
However,
we are not going to do so. Why? Because the projects used in the previous assignments are configured to use the wrapper
scripts
provided by Maven and Gradle, which download the necessary dependencies when the projects are built. This way we don't
need to
install Maven and Gradle in the VM.

## Cloning the Repository

The first step is to clone, into the VM, the repository containing the projects from the previous assignments. This can
be done by running the following command in the terminal:

```bash
git clone <repository-url>
```

Now a few problems may arise when trying to clone the repository. The repository is set to private, so the user will
need to provide their GitHub credentials to clone it. Now since we don't want that kind of interaction with the VM, we
have two possible solutions:

- Create and use an SSH key to authenticate with GitHub. This way the user won't need to provide their credentials every
  time they interact with the repository. To do so we must create a public and private key pair in the VM and add the
  public key to the GitHub account.
- Set the repository to public. This way the user won't need to authenticate with GitHub to clone the repository.

Either of these solutions will work, but for this assignment, the second solution was chosen. The repository was set to
public, so the user can clone it without needing to authenticate with GitHub. This was also done in preparation for the
part 2 of this assignment.

## Running the Projects

After cloning the repository, the user should now have access to the projects from the previous assignments. The next
step is to run the projects in the VM.

### Spring Boot Tutorial Basic Project

Some notes on running the Spring Boot Tutorial Basic Project in the VM. The project uses Maven as the build tool, so the
user should use the wrapper script provided by Maven to build and run the project. This can be done by running the
following command in the terminal:

```bash
cd devops-23-24-PSM-1231836/ca1/basic/
./mvnw spring-boot:run
```

The same process can be done in the project inside the ca2/part2 directory:

```bash
cd devops-23-24-PSM-1231836/ca2/part2/react-and-spring-data-rest-basic/
./gradlew build
./gradlew bootRun
```

After running this command the project wil start its build process, and then it will start the application. The user can
access the application by opening a web browser on the host machine and navigating to `http://<vm-ip>:8080`. Note here
that the IP address may vary depending on the IP used in the `01-netcfg.yaml` file configuration.

### Gradle_Basic_Demo Project

The Gradle_Basic_Demo project uses Gradle as the build tool, so the user should use the wrapper script provided by
Gradle to build and run the project. This can be done by running the following command in the terminal:

```bash
cd devops-23-24-PSM-1231836/ca2/part1/
./gradlew build
./gradlew runServer
```

After running this command the project wil start its build process, and then it will start the server side of the chat
application. Now in order to test if everything is working correctly, the user should open a terminal in the host
machine
and run the following command:

```bash
./gradlew runClient --args="<vm-ip> 59001"
```

This command will start the client side of the chat application. Why the extra arguments? The first argument is the IP
address of the server, in this case the VM IP address, and the second argument is the port number that the server is
listening on. This is required in order to establish a connection between the client and the server. Another alternative
to this is to change the build.gradle file and set the server IP address and port number as default values.

#### Why should we launch the client side in host and server side in guest?

The client side, responsible for user interaction and featuring a Graphical User Interface (GUI), is best suited for the
host machine, where user interaction occurs. Conversely, the server side, tasked with listening for connections and
typically lacking a GUI, is aptly deployed on the guest machine, ensuring efficient resource utilization. This setup
allows the server to operate in a headless manner, optimizing performance by avoiding unnecessary GUI-related resource
consumption. Trying to run a GUI application in a headless server would result in an error and the application wouldn't
start. Another reason might be that the server side is running in a VM, which is isolated from the host machine, and
therefore the client side must be run on the host machine to establish a connection between the two. If the connection is
established, then it means all previous configurations were done correctly and the interaction between the client and
server is working as expected.

Since all the necessary dependencies were already installed in the VM and all file permissions were correctly set in
the project files, no difficulties were encountered when running the projects in the VM. The projects were successfully
built and run, and the applications were accessible from the host machine.

# Conclusion

In conclusion, this report provides a comprehensive overview of the first part of the third class assignment, which
focuses on virtualization using VirtualBox. The assignment involves setting up a VirtualBox VM with Ubuntu and running
previous projects within this VM. The report details the process of setting up the VM, cloning the necessary
repositories, and running the projects. It also explains the benefits and challenges of virtualization, the role of a
hypervisor, and the reasons for running the client side of an application on the host machine and the server side on the
guest machine. The assignment demonstrates the practical application of virtualization in software development and
provides valuable hands-on experience with VirtualBox.