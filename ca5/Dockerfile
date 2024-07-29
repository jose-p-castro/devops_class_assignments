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