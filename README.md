# ROLE BASED ACCESS CONTROL EXAMPLE

[![Github Repo](https://img.shields.io/badge/GitHub-Repo-green.svg?longCache=true&style=flat)](https://github.com/lombocska/role-based-access-control-example)
- TODO place for the CI/CD, test coverage, license  mark

## DESCRIPTION


## USAGE 
- TODO: proper description how to launch

## TECH STACK
- TODO list of the used frameworks, dependencies

## APPROACHES

### dockerfile
> Also, there is a clean separation between dependencies and application resources in a Spring Boot fat jar file, 
>and we can use that fact to improve performance. 
>The key is to create layers in the container filesystem. The layers are cached both at build time 
>and at runtime (in most runtimes) so we want the most frequently changing resources, 
>usually the class and static resources in the application itself, to be layered after the more slowly changing resources. 


## INSPIRATIONS

- [Spring Boot Docker Layers](https://springframework.guru/why-you-should-be-using-spring-boot-docker-layers/)
- [JarLauncher](https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/loader/JarLauncher.html)
## TODO
- docker plugin for building app image (publish it on hub?!)
- ci/cd github actions?
- reactive?
- react? 
- mybatist/querydsl? -> over engineering
- actuator
- graphite
- sec test
- tracing
- service discovery?
- app event handling -> audit
- horizontal scalability
- maven wrapper
- logging
- dockerfile run with user not default


