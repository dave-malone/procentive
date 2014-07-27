Procentive 2.0
==========

Procentive 2.0 is meant to allow for customers to design their own entities with custom fields and validators. Customer defined workflows are also essential
to the design of this next gen version of the platform. 

Instead of being a data-driven architecture, this application is being built as both a resource oriented architecture (REST with RESTful Service APIs)
and an event-driven architecture. Events are the basic drivers of interaction between components. Producers of events will
be UIs, such as patient forms, which result in updated Entities and/or Fields. Those updates will lead to the creation and propagation of
EntityCreatedEvent, EntityUpdatedEvent, and FieldUpdatedEvent instances, which will be consumed by "workers" which will perform various 
tasks based on the events they receive, and based on whether the event was of any interest to them. 

UIs will (likely) be simple thin-clients; HTML pages built with AngularJS which rely on calls to underlying RESTful APIs. There's
also a potential for the use of Promise APIs in place of or in addition to RESTful APIs. Promise APIs bring the added benefit of being highly scalable
with a single application instance, whereas RESTful APIs, which rely completely on the request-response model, and therefore a thread per request, are
not as scalable in comparison.    

This project uses Gradle, the Spring Framework, and has a dependency on RabbitMQ. There's also a possibility that this platform will be built to utilize
Cassandra, MongoDB, and MySQL. Following are instructions on how to configure your development environment for use with this project. 

Another useful tool when using Git as our DVCS is SourceTree. It is recommended that developers download and install SourceTree and use it to help with merges, 
commits, and pushes to the central Github.com repo.   

#

## Install the Gradle Plugin 

In order to build this project using Eclipse, you will need the Gradle plugin installed.
If you are using the Groovy Grails Tools Suite (GGTS) or SpringSource Tools Suite (STS), you can obtain this plugin via the GGTS/STS dashboard's extension manager. 
Simply select the Gradle plugin and click install. Your IDE will likely need to restart in order for the changes to become effective.

## Importing into Eclipse

Once the plugin is installed, pull this project down to your own machine. After you've done that, you can import the projects into your workspace with the following steps:

1. From your Eclipse menu, select File > Import
2. From the Import dialog, select Gradle > Gradle Project (if you don't see this option, you need to install the Gradle plugin using the instructions above)
3. From the Import Gradle Project dialog, in the Root Folder option, select the location-pilot parent folder. Next, click the "Build Model" button.
4. After clicking "Build Model" you should see all of the various subprojects in the dialog window. Select all of the projects, then click the Finish button.
5. Each of the projects should now be visible in your workspace. In order for the projects to compile, you will need to select each of the projects, right click on them and select Gradle > Refresh All

## Gradle Wrapper

The projects each come with the Gradle Wrapper installed. This allows you to run various Gradle build commands without having to install and configure Gradle.
To get started on the command line, navigate to your location-pilot folder using your shell, and then execute the following command:

```
gradlew clean build
```

This will ensure that the Gradle Wrapper is working on your machine and that you can build the project (including installation of dependencies via Gradle's dependency management).

## Installing RabbitMQ

TODO - add instructions; or link to RabbitMQ documentation


### RabbitMQ Management Console
http://localhost:15672/
Default username & password: guest:guest

Documentation: http://www.rabbitmq.com/management.html
