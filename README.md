# ROLE BASED ACCESS CONTROL EXAMPLE

[![Github Repo](https://img.shields.io/badge/GitHub-Repo-green.svg?longCache=true&style=flat)](https://github.com/lombocska/role-based-access-control-example)
- TODO place for the CI/CD, test coverage, license  mark

## DESCRIPTION


## USAGE 
- TODO: proper description how to launch

## TECH STACK
- TODO list of the used frameworks, dependencies
- Spring 2.3.1
## APPROACHES


### layered dockerfile for efficiency

> Also, there is a clean separation between dependencies and application resources in a Spring Boot fat jar file, 
>and we can use that fact to improve performance. 
>The key is to create layers in the container filesystem. The layers are cached both at build time 
>and at runtime (in most runtimes) so we want the most frequently changing resources, 
>usually the class and static resources in the application itself, to be layered after the more slowly changing resources. 

### prometheus for metrics

> Prometheus is an open-source systems monitoring and alerting toolkit originally built at SoundCloud. 
> Since its inception in 2012, many companies and organizations have adopted Prometheus, and the project has a very active developer and account community.




## INSPIRATIONS

- [Spring Boot Docker Layers](https://springframework.guru/why-you-should-be-using-spring-boot-docker-layers/)
- [JarLauncher](https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/loader/JarLauncher.html)
- [Prometheus](https://prometheus.io/docs/introduction/overview/)


## TODO
- docker plugin for building app image (publish it on hub?!)
- ci/cd github actions?
- reactive?
- react? 
- mybatist/querydsl? -> over engineering
- actuator
- sec test
- tracing
- service discovery?
- app event handling -> audit
- maven wrapper
- logging
- dockerfile run with account not default


