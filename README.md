# Odometer Analysis Micro-Service

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; This service analyzes the odometer and provides the information about "Odometer Rollback".
Since an Odometer should always increase in value over time, an "Odometer Rollback" is defined as an event in which the Odometer no longer grows in an ascending manner.

# Build & Run Instructions
  To run the project do the following steps from the project home folder.

  * mvn clean compile package
  * java -Dspring.profiles.active= **[*environment*]** -jar target/odometer-analysis-service-  **[*version*]** .jar

This will start the server on port 8801 by default and the application will be pre-deployed. If you wish to change the port, please edit **server.port** property in the appropriate properties file (application.yml).
Alternatively you can add another system property to the execution like this. This will override the value in properties file.

* java -Dspring.profiles.active= **[*environment*]** -Dserver.port= **[*port*]** -jar target/odometer-analysis-service-  **[*version*]** .jar

The VM argument **spring.profiles.active** indicate the environment where you are deploying(possible values are **dev**, **stage** and **live**).
Depending on the deployment strategy and profile, appropriate properties file needs to be changed.

Credentials for api and mon users are in application properties

_Local:_  http://localhost:**[*port*]**

_Staging:_

_Live:_

_Jenkins Job:_

To test rest api calls navigate by swagger link:
_Swagger:_ http://localhost:8801/swagger-ui.html

To monitor app or gather metrics navigate by actuator link:
_Actuator_ http://localhost:8801/actuator
