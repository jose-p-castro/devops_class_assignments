# Class Assignment 3 / Part 2 - Report

## Introduction

The objective of this report is to furnish a comprehensive overview of the second part of the third class assignment.
This part focuses on the use of Vagrant an open-source tool used for building and managing portable virtual development
environments. The report will provide a detailed explanation of the Vagrantfile used to create the virtual machines (
VMs), the installation of the necessary software, and the configuration of the virtual machine. Additionally, the
report will tackle the challenges encountered during the assignment and the solutions implemented to overcome them.

Vagrant allows developers to create reproducible and isolated development environments, making it easier to share and
collaborate on projects across different machines and operating systems. With Vagrant, you can define your environment's
configuration in a simple text file and quickly spin up or tear down VMs with the desired specifications.

It also provides key advantages for development teams. It ensures portability and consistency by offering a uniform
environment across machines and Operating Systems (OSs), easing project sharing. Its isolation and repeatability
features create isolated virtual environments, preventing conflicts and enabling easy replication. Moreover, Vagrant
streamlines setup through automated provisioning, automating configuration tasks and enhancing reproducibility across
the development lifecycle.

Vagrant has its drawbacks. Firstly, there's a performance overhead due to running virtual machines, which can impact
application performance, especially for resource-intensive tasks. Secondly, there's a learning curve involved in setting
up and configuring Vagrant environments, particularly for newcomers to virtualization concepts. Lastly, resource
consumption is a concern as Vagrant VMs consume system resources, potentially straining the host machine, particularly
when running multiple environments simultaneously or with high resource allocations. These drawbacks were felt during
the assignment, mainly the learning curve associated with Vagrant configuration.

The goal of Part 2 of this assignment is to use Vagrant to set up a virtual environment to execute the tutorial spring
boot application, gradle "basic" version, developed in CA2, Part2.

Issues should be created in GitHub for each main task that was completed. To see how to create issues please refer
to the [Creating Issues](../../ca1/README.md) section on the Class Assignment 1 report.

This Class Assignment was made by the student José Castro nº1231836 of class B and the outcome of the work undertaken in
this assignment can be found [here](https://github.com/J2PCastro/devops-23-24-PSM-1231836).

# Table of Contents

- [Setup](#setup)
- [Vagrantfile](#vagrantfile)
- [Copying the Vagrantfile](#copying-the-vagrantfile)
- [Update Vagrantfile](#update-vagrantfile)
- [Running Vagrant](#running-vagrant)
- [Challenges](#challenges)
    - [Issue 1: Base Box Version](#issue-1-base-box-version)
    - [Issue 2: Java Version](#issue-2-java-version)
    - [Issue 3: H2 Database Version](#issue-3-h2-database-version)
    - [Issue 4: Apache Tomcat Version](#issue-4-apache-tomcat-version)
        - [Note on Apache Tomcat](#note-on-apache-tomcat)
    - [Issue 5: Missing plugins and dependencies](#issue-5-missing-plugins-and-dependencies)
    - [Issue 6: Application Configuration](#issue-6-application-configuration)
    - [Issue 7: App.js Configuration](#issue-7-appjs-configuration)
    - [Issue 8: Add ServletInitializer](#issue-8-add-servletinitializer)
    - [Issue 9: Small rendering issues](#issue-9-small-rendering-issues)
- [Notes](#notes)
- [Running Vagrant file after changes](#running-vagrant-file-after-changes)
    - [Another note](#another-note)
- [Alternative to VirtualBox: KVM/QEMU](#alternative-to-virtualbox-kvmqemu)
    - [Installing KVM/QEMU on Ubuntu](#installing-kvmqemu-on-ubuntu)
    - [Configuring Vagrant for KVM/QEMU](#configuring-vagrant-for-kvmqemu)
- [Conclusion](#conclusion)

# Setup

First, we need to install Vagrant. To do this, we need to navigate to the [Vagrant website](https://www.vagrantup.com/)
and follow the steps to download and install the latest version of Vagrant for your operating system.

In order for Vagrant to work, we also need to install a provider. In this case, we will be using VirtualBox since it is
already installed on our machine from previous assignments. Vagrant provisions and manages VMs using providers like
VirtualBox and that is why we need to have it installed. A provision is a set of instructions that configure the virtual
machine with the necessary software and settings.

# Vagrantfile

The Vagrantfile is a configuration file that defines the VMs settings and provisions. It is written in Ruby and contains
the specifications for the VM, such as the base box, network settings, and provisioning scripts. For this assignment,
the Vagrantfile used as an initial solution can be
found [here](https://bitbucket.org/pssmatos/vagrant-multi-spring-tut-demo/).

After navigating to the above repository, we should analyze the Vagrantfile to understand the VMs configuration and
study the README file to understand how to run the VM. Let's start by analyzing the Vagrantfile by sections:

```ruby
Vagrant.configure("2") do |config|
  config.vm.box = "ubuntu/bionic64"

  config.vm.provision "shell", inline: <<-SHELL
    sudo apt-get update -y
    sudo apt-get install -y iputils-ping avahi-daemon libnss-mdns unzip \
        openjdk-11-jdk-headless
  SHELL
```

This section of the Vagrantfile configures the base box for the VM and provisions the VM with the necessary software.
The base box is the image used to create the VM, and in this case, it is an Ubuntu 18.04 image. The "2" in
`Vagrant.configure("2")` specifies the version of the Vagrant configuration format. The provision script
installs the required packages for the VM, such as `iputils-ping`, `avahi-daemon`, `libnss-mdns`, `unzip`, and
`openjdk-11-jdk-headless`.

```ruby
  # Configurations specific to the database VM
  config.vm.define "db" do |db|
    db.vm.box = "ubuntu/bionic64"
    db.vm.hostname = "db"
    db.vm.network "private_network", ip: "192.168.56.11"

    # We want to access H2 console from the host using port 8082
    # We want to connet to the H2 server using port 9092
    db.vm.network "forwarded_port", guest: 8082, host: 8082
    db.vm.network "forwarded_port", guest: 9092, host: 9092

    # We need to download H2
    db.vm.provision "shell", inline: <<-SHELL
      wget https://repo1.maven.org/maven2/com/h2database/h2/1.4.200/h2-1.4.200.jar
    SHELL

    # The following provision shell will run ALWAYS so that we can execute the H2 server process
    # This could be done in a different way, for instance, setiing H2 as as service, like in the following link:
    # How to setup java as a service in ubuntu: http://www.jcgonzalez.com/ubuntu-16-java-service-wrapper-example
    #
    # To connect to H2 use: jdbc:h2:tcp://192.168.33.11:9092/./jpadb
    db.vm.provision "shell", :run => 'always', inline: <<-SHELL
      java -cp ./h2*.jar org.h2.tools.Server -web -webAllowOthers -tcp -tcpAllowOthers -ifNotExists > ~/out.txt &
    SHELL
  end
```

This section of the Vagrantfile configures the database VM, which is a separate VM. The database VM is
configured with the same base box as the main VM, and it is assigned a hostname and a private network IP address. The
database VM is also configured with forwarded ports to access the H2 console and server from the host machine. The
provision script downloads the H2 database jar file, and another provision script runs the H2 server process.

```ruby
# Configurations specific to the webserver VM
  config.vm.define "web" do |web|
    web.vm.box = "ubuntu/bionic64"
    web.vm.hostname = "web"
    web.vm.network "private_network", ip: "192.168.56.10"

    # We set more ram memmory for this VM
    web.vm.provider "virtualbox" do |v|
      v.memory = 1024
    end

    # We want to access tomcat from the host using port 8080
    web.vm.network "forwarded_port", guest: 8080, host: 8080

    web.vm.provision "shell", inline: <<-SHELL, privileged: false
      sudo apt install -y tomcat9 tomcat9-admin

      # Change the following command to clone your own repository!
      git clone https://bitbucket.org/pssmatos/tut-basic-gradle.git
      cd tut-basic-gradle
      chmod u+x gradlew
      ./gradlew clean build
      # To deploy the war file to tomcat9 do the following command:
      sudo cp ./build/libs/basic-0.0.1-SNAPSHOT.war /var/lib/tomcat9/webapps
    SHELL
  end
end
```

This section of the Vagrantfile configures the webserver VM, which is another separate VM. The webserver VM is
configured with the same base box as the main VM, and it is assigned a hostname and a private network IP address. The
webserver VM is also configured with more memory allocation and a forwarded port to access Tomcat from the host machine.
The provision script installs Tomcat and clones the tutorial spring boot application repository. The script then builds
the application using Gradle and deploys the generated war file to Tomcat.

The Vagrantfile is well-structured and provides clear configurations for the database VM and webserver VM. It defines
the base box, network settings, memory allocation, forwarded ports, and provisioning scripts for each VM. The
provisioning scripts install the necessary software and configure the VMs according to the requirements of the tutorial
spring boot application.

For readability purposes, the commented lines in the Vagrantfile were removed.

# Copying the Vagrantfile

Start by creating a new directory in the repository where we will store the Vagrantfile. To do this, we need to
open a terminal and run the following command:

```bash
cd path/to/repository/ca3
mkdir part2
```

To start the assignment, we need to clone the repository containing the Vagrantfile. To do this, we need to open a
terminal in another directory and run the following command:

```bash
git clone https://bitbucket.org/pssmatos/vagrant-multi-spring-tut-demo.git
```

After cloning the repository, we need to navigate to the directory containing the Vagrantfile and copy it to our
repository:

```bash
cd path/to/vagrant-multi-spring-tut-demo
cp Vagrantfile path/to/repository/ca3/part2
```

Add a `.gitignore` file to the directory to ignore the `.vagrant` directory:

```bash
echo ".vagrant" > path/to/repository/ca3/part2/.gitignore
```

# Update Vagrantfile

After copying the Vagrantfile to our repository, we need to update it in order for it to use our repository as the
source for the spring boot application. To do this, we need to open the Vagrantfile in a text editor and update the
following line:

```ruby
# Change the following command to clone your own repository!
git clone <repository-url>
```

This update creates a problem. If the repository is private, we need to provide credentials to clone it however, since
we can't provide credentials in the Vagrantfile, we need to find a different solution. The most simple solution is to
change the repository to public and since this was already done, we can proceed to the next step.

We will use the application developed in CA2 Part2. The application uses Spring Boot and Gradle. Spring Boot as an
embedded Tomcat server that allows us to package our Spring application as a self-contained `jar` file, making it easy
to deploy and run without needing to install and configure an external servlet container like Apache Tomcat separately.
This approach simplifies the deployment process and is one of the key features that makes Spring Boot so convenient for
building and deploying web applications. However, in this assignment, we will deploy the application to an external
Tomcat server. This way we can change some aspects of the original application in order to experiment and try to deploy
the app in an external Tomcat server. This method allows me to explore and experiment with different technologies,
as in the previous assignment the embedded Tomcat server was already used to deploy the app.

# Running Vagrant

To run the Vagrantfile, we need to navigate to the directory containing the Vagrantfile and run the following command:

```bash
vagrant up
```

This command will start the virtual machine and provision it according to the specifications in the Vagrantfile.
Provisioning is the process of setting up the VM with the necessary software and configurations. CA2 Part2 contains a
simple application that displays some text on the browser. After everything is set up, we can access the application
by opening a web browser and navigating to `http://localhost:8080/basic-0.0.1-SNAPSHOT/`
or `http://192.168.56.10:8080/basic-0.0.1-SNAPSHOT/`. This last URL uses the IP address of the webserver VM that was
defined in the Vagrantfile. In the same fashion, we can access the H2 console by navigating
to `http://localhost:8080/basic-0.0.1-SNAPSHOT/h2-console`
or `http://192.168.56.10:8080/basic-0.0.1-SNAPSHOT/h2-console`. For the connection string
use: `jdbc:h2:tcp://192.168.56.11:9092/./jpadb`.

After we are done with the VM, we can stop it by running the following command:

```bash
vagrant halt
```

This command will stop the VM, but it will keep the VMs state, allowing us to start it again later. If we want to
destroy the VM and remove all its resources, we can run the following command:

```bash
vagrant destroy -f
```

This command will stop the VMs and remove all their resources, including the virtual disk and network interfaces. It is
important to note that this action is irreversible, and all data stored in the VMs will be lost. The `-f` flag is used
to force the destruction without confirmation.

# Challenges

After performing all the above steps and trying to see the application running by navigating to the above links, we find
that nothing is displayed on the browser. This issue is caused by the fact that the application contained in CA2 Part2
has some compatibility issues with several of the software versions used in the Vagrantfile. After analyzing the issue
and searching the internet for a solution, it was discovered that the version of Spring Boot used in the application
isn't compatible with both the version of Tomcat installed in the VM and the H2 database installed in the VM. In
addition to this several components were missing from the original application in order for it to be properly deployed
and run in a Tomcat server. Let's address the issues now, step by step.

## Issue 1: Base Box Version

The base box used in the Vagrantfile is `ubuntu/bionic64`, which is an Ubuntu 18.04 image. The application was built
using Spring Boot 3.2.4, which may not be compatible with the Ubuntu versions installed in the VMs. To address this
issue, we simply need to provide a base box that is compatible with the application. In this case the base box
used was `generic/ubuntu2204` which is an Ubuntu 22.04 image. To update the base box, we need to open the Vagrantfile
and change the following line:

```ruby
config.vm.box = "generic/ubuntu2204"
```

This change was done in all the VMs in the Vagrantfile.

## Issue 2: Java Version

Openjdk-11-jdk-headless was installed in the VMs, but the application was built using Java 17. To avoid potential
compatibility issues, we need to install Java 17 in the VM. To do this, we need to update the provision script in the
Vagrantfile to install Java 17. The provision script should look like this:

```ruby
  # This provision is common for both VMs
  config.vm.provision "shell", inline: <<-SHELL
    sudo apt-get update -y
    sudo apt-get install -y iputils-ping avahi-daemon libnss-mdns unzip \
        openjdk-17-jdk-headless
  SHELL
```

## Issue 3: H2 Database Version

The latest version of the H2 database should be downloaded (version 2.2.224 as of the making of this report) to again
prevent possible compatibility issues. To do this, we need to update the provision script in the Vagrantfile to download
the latest version of the H2 database. The provision script should look like this:

```ruby
    db.vm.provision "shell", inline: <<-SHELL
      wget https://repo1.maven.org/maven2/com/h2database/h2/2.2.224/h2-2.2.224.jar
    SHELL
```

## Issue 4: Apache Tomcat Version

Tomcat 9 was the version that was installed in the web VM in the original Vagrantfile, however, the Spring Boot version
of the application is incompatible with this version. To address this issue, we need to install Tomcat 10 in the VM. To
do this, we need to update the provision script in the Vagrantfile to look like this:

```ruby
      web.vm.provision "shell", inline: <<-SHELL, privileged: false
        sudo mkdir /opt/tomcat/
        cd /tmp
        wget https://archive.apache.org/dist/tomcat/tomcat-10/v10.1.23/bin/apache-tomcat-10.1.23.tar.gz
        sudo tar xzvf apache-tomcat-10*tar.gz -C /opt/tomcat --strip-components=1
        sudo chown -R vagrant:vagrant /opt/tomcat
        <rest of the script>
      SHELL
```

Here we are creating a directory for Tomcat, downloading the latest version of Tomcat, extracting the downloaded file,
and changing the ownership of the Tomcat directory to the vagrant user. The rest of the script is the same as before.
Notice that we are using the version 10.1.23 of Tomcat, and we are downloading it from the Apache archive website.

### Note on Apache Tomcat

We could use the latest version of Tomcat by downloading it from the official Apache Tomcat website. To check the latest
version of Tomcat, navigate [here](https://dlcdn.apache.org/tomcat/). In this website you can see all the latest
versions of Tomcat. Since the version used in this report is v10, enter the `tomcat-10/` directory, then enter the
version directory that should look something like `v10.1.24/` (this number may vary depending on whatever is the latest
version), and finally enter the `bin/` directory. Here you will find the latest version of Tomcat. To use the latest
version on the provision script simply copy the URL from the browser and paste it in the `wget` command in the provision
script adding the correct version of tomcat to the URL. The rest of the script should remain the same. Just note that if
a future version of Tomcat is released, the URL will change and the script will most likely fail. The reason Ubuntu
package manager is not used to install Tomcat is because the packages are not up-to-date and the latest version of
Tomcat is not available in the Ubuntu repositories.

## Issue 5: Missing plugins and dependencies

In the application developed in CA2 Part2 a plugin and a dependency were missing from the build.gradle file. The plugin
was the `war` plugin and the dependency was the `spring-boot-starter-tomcat` dependency. The `war` plugin is used to
generate `.war` files necessary for deployment of the app to an external servlet (Tomcat in this instance).
The `spring-boot-starter-tomcat` is used to, in very simple terms, include he Tomcat servlet container during runtime,
saying that our application will be running on a servlet container. To address this issue, we need to open the
build.gradle file in the application and add the following lines:

```groovy
plugins {
    id 'war'
}

dependencies {
    providedRuntime 'org.springframework.boot:spring-boot-starter-tomcat'
}
```

## Issue 6: Application Configuration

The application didn't have a properly configured `application.properties` file to configure the H2 database connection
and the context path of the application. To address this issue, we need to change the `application.properties` file in
the `src/main/resources` directory of the application to the following content:

```properties
#server.servlet.context-path=/react-and-spring-data-rest-basic-0.0.1-SNAPSHOT
spring.data.rest.base-path=/api
spring.datasource.url=jdbc:h2:mem:jpadb
# In the following settings the h2 file is created in /home/vagrant folder
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
# So that spring will not drop the database on every execution.
spring.jpa.hibernate.ddl-auto=update
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
spring.h2.console.settings.web-allow-others=true
```

Notice here that the `server.servlet.context-path` property was commented out and the `spring.datasource.url` property
is pointing to an in memory database. This was done as a measure to allow the application contained in CA2 Part2 to
still be run without needing an external database server and without needing to be deployed to an external Tomcat
server.

However, if left as is, the Vagrant VMs will not be able to access the H2 console and, the user, the app via the
browser. This issue was addressed during the provisioning of the web VM. If we analyze the script we find two extra
commands right after cloning the repository and moving into the correct directory:

```Ruby
      # Uncomment the first line in application.properties
      sed -i '0,/^[ \t]*#server.servlet.context-path=/s//server.servlet.context-path=/g' src/main/resources/application.properties

      # Modify the application.properties file to set SPRING_DATASOURCE_URL
      sed -i 's|spring.datasource.url=.*|spring.datasource.url=jdbc:h2:tcp://192.168.56.11:9092/./jpadb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE|' src/main/resources/application.properties
```

This first command uncomment the `server.servlet.context-path` property in the `application.properties` file allowing us
to access the application via the correct context path. The second command changes the `spring.datasource.url` property
to point to the H2 database in the database VM. This way the application will be able to access the H2 database and the
H2 console.

The `sed` command is a stream editor that is used to perform basic text transformations on an input stream. It is a
powerful tool for editing text files and is commonly used in shell scripts to automate text processing tasks.

## Issue 7: App.js Configuration

The `App.js` file in the application must be changed in order to accommodate the new context path of the application. To
do this, we need to open the `App.js` file in the `src/main/js` directory of the application and change it. For the same
reasons mentioned above, we still want the CA2 Part2 application to run locally without needing to be deployed to an
external Tomcat server. Again here we make use of the `sed` command to change the file. Pay close attention to the
script right after the `chmod u+x gradlew` command in the provision script of the web VM:

```Ruby
      # Modify the app.js file to set the correct path
      sed -i "s|path: '/api/employees'|path: '/react-and-spring-data-rest-basic-0.0.1-SNAPSHOT/api/employees'|" src/main/js/app.js
```

This command changes the path in the `App.js` file to point to the correct context path of the application. It scans the
app.js file for the `path: '/api/employees'` string and replaces it
with `path: '/react-and-spring-data-rest-basic-0.0.1-SNAPSHOT/api/employees'`. This way we will be able to access the
correct context path of the application.

## Issue 8: Add ServletInitializer

The application didn't have a `ServletInitializer` class to configure the application for deployment in a servlet
container. To address this issue, we need to create a `ServletInitializer` class in the `src/main/java` directory of
the application with the following content:

```java
package com.greglturnquist.payroll;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ReactAndSpringDataRestApplication.class);
    }

}
```

## Issue 9: Small rendering issues

The application had some small rendering issues that were fixed by changing the `index.html` file in the
`src/main/resources/templates` directory of the application. The changes made to the `index.html` file to fix the issue
are outlined below:

```html

<link rel="stylesheet" href="main.css"/>
```

After performing all the above steps, the changes were committed and pushed to the repository in order for the
Vagrantfile to clone the updated application.

## Notes

Two small notes:

- The fixes made in issues 1 through 4 as well as issues 6 and 7 were made after an extensive search online and with a
  lot of trial and error, that being the reason why there were many commits and a lot of restructuring done during this
  assignment;
- The fixes made in issues 5 through 9 were complemented after carefully analyzing the project that was provided by the
  teacher and that can be found [here](https://bitbucket.org/pssmatos/tut-basic-gradle/src/master/).

## Running Vagrant file after changes

After making all the changes to the Vagrantfile and the application, we need to run the Vagrantfile again to see if the
application is running properly. To do this, we need to navigate to the directory containing the Vagrantfile and run the
following command:

```bash
vagrant up
```

After waiting several minutes for the VMs to be provisioned, we can access the application by opening a web browser and
navigating to `http://localhost:8080/react-and-spring-data-rest-basic-0.0.1-SNAPSHOT/`
or `http://192.168.56.10:8080/react-and-spring-data-rest-basic-0.0.1-SNAPSHOT/`. For the H2 console, we can navigate
to `http://localhost:8080/react-and-spring-data-rest-basic-0.0.1-SNAPSHOT/h2-console`
or `http://192.168.56.10:8080/react-and-spring-data-rest-basic-0.0.1-SNAPSHOT/h2-console`. For the connection string
use: `jdbc:h2:tcp://192.168.56.11:9092/./jpadb`.

Note here that since we changed the context path of the application, the URL to access the application and the H2
console changed as well from basic-0.0.1-SNAPSHOT to react-and-spring-data-rest-basic-0.0.1-SNAPSHOT.

After everything is working properly, we can stop the VMs by running the following command:

```bash
vagrant halt
```

### Another note

If after the VMs are "up" and we want to use their respective terminals, we can do so by running the following command:

```bash
vagrant ssh <vm-name>
```

Where `<vm-name>` is the name of the VM we want to access. In this case, the VMs are named `db` and `web`. After running
the above command, we will be inside the VM, and we can run any command we want. To exit the VM, we can run the `exit`
command.

# Alternative to VirtualBox: KVM/QEMU

An alternative to VirtualBox is KVM/QEMU, which is a set of open-source tools for virtualization on Linux. KVM (Kernel
Virtual Machine) is a Linux kernel module that allows the host machine to act as a hypervisor, providing hardware
virtualization support. QEMU (Quick Emulator) is a user-space application that provides emulation of various hardware
devices and can be used in conjunction with KVM to create virtual machines.

KVM/QEMU provides several advantages over VirtualBox. Firstly, it offers better performance and scalability, as it
leverages the Linux kernel's built-in virtualization capabilities. This results in lower overhead and better
performance for virtual machines. Secondly, KVM/QEMU is more lightweight and efficient, as it integrates directly with
the host operating system and does not require a separate virtualization layer. This makes it ideal for running
virtual machines on Linux servers and cloud environments. Lastly, KVM/QEMU is open-source and free to use, providing
flexibility and customization options for virtualization environments.

VirtualBox offers a user-friendly interface and wide cross-platform support, making it suitable for desktop or
small-scale virtualization environments. However, it may incur a performance overhead compared to hypervisors like
KVM/QEMU. VirtualBox is well-suited for running various guest operating systems and benefits from a large community for
support. On the other hand, KVM/QEMU provides near-native performance and scalability, leveraging kernel integration for
efficient resource management. While it may require more technical expertise due to its command-line interface and Linux
dependency, KVM/QEMU is ideal for enterprise-grade virtualization deployments requiring high performance and
scalability. Additionally, being open-source, KVM/QEMU offers transparency, flexibility, and hardware virtualization
support.

Since the OS used in this assignment is Ubuntu, KVM/QEMU would be a suitable alternative to VirtualBox for running
virtual machines. To use KVM/QEMU, we need to install the necessary packages and configure the virtual machines using
tools like `virt-manager` or `virsh`. KVM/QEMU provides a powerful and efficient virtualization solution for Linux
systems, offering performance benefits and flexibility for running virtual machines.

Let's now explore the necessary steps to install and configure KVM/QEMU on Ubuntu, as well as the changes required to
use KVM/QEMU as the provider for Vagrant.

## Installing KVM/QEMU on Ubuntu

To install KVM/QEMU on Ubuntu, we need to follow these steps:

- Install the necessary packages:

```bash
sudo apt update
sudo apt install -y qemu-kvm libvirt-daemon-system libvirt-clients bridge-utils libvirt-dev
```

- Install `vagrant-libvirt` plugin:

```bash
vagrant plugin install vagrant-libvirt
```

## Configuring Vagrant for KVM/QEMU

To configure Vagrant to use KVM/QEMU as the provider, we need to update the Vagrantfile with the following settings:

```ruby
Vagrant.configure("2") do |config|
  # Specify the box and the provider
  config.vm.box = "generic/ubuntu2204"
  config.vm.provider :libvirt do |libvirt|
    # Use QEMU as the driver
    libvirt.driver = "qemu"
    # Specify 1 core and 1024MB of RAM
    libvirt.cpus = 1
    libvirt.memory = 1024
  end
<rest of the Vagrantfile>
```

This configuration specifies the provider as `libvirt`. It sets the driver to `qemu`, the number of CPUs to 1, and the
amount of memory to 1024MB. These settings can be adjusted based on the requirements of the virtual machines.

Also, we need to remove the VirtualBox provider configuration from the Vagrantfile to avoid conflicts. Simply go to the
Vagrantfile and remove the following part:

```ruby
# This sets more RAM memory for this VM
config.vm.provider "virtualbox" do |v|
  v.memory = 1024
end
```

After updating the Vagrantfile, we can run the Vagrantfile by running the following command:

```bash
vagrant up
```

This command will start the virtual machines using KVM/QEMU as the provider. We can access the virtual machines using
the `vagrant ssh` command as before. To stop the virtual machines, we can run the `vagrant halt` command. If we want to
monitor the virtual machines using `virt-manager`, we can install the `virt-manager` package and open the application
to view and manage the virtual machines. Simply run the following command to install `virt-manager`:

```bash
sudo apt install -y virt-manager
```

KVM/QEMU provides a robust and efficient virtualization solution for Linux systems, offering performance benefits and
flexibility for running virtual machines. By configuring Vagrant to use KVM/QEMU as the provider, we can leverage the
power of hardware virtualization and kernel integration for efficient virtual machine management.

The result of the implementation of this alternative can be
found [here](https://github.com/J2PCastro/devops-23-24-PSM-1231836), more specifically in the
ca3/part2/virtualbox_alternative directory.

# Conclusion

In conclusion, this report has provided a detailed exploration of setting up a virtual development environment using
Vagrant as part of Class Assignment 3, Part 2. This proved to be the most challenging assignment yet, as it required
extensive troubleshooting, configuration adjustments, and compatibility checks to ensure the successful deployment of
the tutorial spring boot application but in the end, it was a very rewarding experience as I was able to come up with a
solution that pleased me and allowed me to grow as a student.

Vagrant proved to be a valuable tool for creating reproducible and isolated development environments. However,
challenges such as compatibility issues with software versions, configuration adjustments, and the learning curve
associated with Vagrant were encountered and addressed effectively.

Additionally, an alternative to VirtualBox, KVM/QEMU, was explored, highlighting its benefits, installation steps, and
configuration adjustments in the Vagrantfile. This alternative showcased the flexibility of Vagrant in adapting to
different virtualization environments and provided insights into optimizing performance and scalability.

Overall, this report serves as a valuable resource for understanding the implementation of Vagrant in setting up virtual
development environments, addressing challenges, and exploring alternative virtualization solutions. It reflects the
student's thorough analysis, problem-solving skills, and proficiency in DevOps practices.