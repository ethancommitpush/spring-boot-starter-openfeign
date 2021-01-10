[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.ethancommitpush/spring-boot-starter-openfeign/badge.png)](https://search.maven.org/artifact/com.github.ethancommitpush/spring-boot-starter-openfeign/)

Spring Boot Starter OpenFeign
=======================

This library makes it easier to automatically use Feign.builder() to construct API interfaces with your custom components declared with a `@FeignClient` annotation. 
There is no any configures about Ribbon or Eureka for this library, just simply visits the APIs with the domain URL and path.

# Artifacts



## What's new?
### Update 01/09/2021: Version 1.1.3 Release Includes
* Fix CustomErrorDecoder error and upgrade to compatible with feign ^10.7.3 ~10.12
### Update 11/17/2020: Version 1.1.2 Release Includes
* Replace default encoder with JacksonEncoder

## Requirements
The following table shows the feign versions that are used by version of spring-boot-starter-openfeign:

| spring-boot-starter-openfeign        | feign  |
| ------------- |:-------------:|
| 1.1.3      | ^10.7.3 |
| 1.1.2      | 10.7.2 |

## Maven configuration

Add the Maven dependency:

```xml
<dependency>
  <groupId>com.github.ethancommitpush</groupId>
  <artifactId>spring-boot-starter-openfeign</artifactId>
  <version>1.1.3</version>
</dependency>

<dependency>
  <groupId>io.github.openfeign</groupId>
  <artifactId>feign-core</artifactId>
  <version>10.7.3</version>
</dependency>

<dependency>
  <groupId>io.github.openfeign</groupId>
  <artifactId>feign-jackson</artifactId>
  <version>10.7.3</version>
</dependency>

<dependency>
  <groupId>io.github.openfeign</groupId>
  <artifactId>feign-httpclient</artifactId>
  <version>10.7.3</version>
</dependency>
```

## Gradle 

```groovy
compile group: 'com.github.ethancommitpush', name: 'spring-boot-starter-openfeign', version: '1.1.3'
compile group: 'io.github.openfeign', name: 'feign-core', version: '10.7.3'
compile group: 'io.github.openfeign', name: 'feign-jackson', version: '10.7.3'
compile group: 'io.github.openfeign', name: 'feign-httpclient', version: '10.7.3'
```

# Usage

* Use `@FeignClient` to declare custom components to be generated as API interfaces:

```java
package example.client;

@Headers({"Content-Type: application/json"})
@FeignClient(url = "${postman-echo.domain}")
public interface PostmanEchoClient {

    @RequestLine("GET /time/object?timestamp={timestamp}")
    TimeObjectGetRespDTO getTimeObject(@Param("timestamp") String timestamp);

}
```

* `application.yml` Properties configuration:

```yaml
feign:
  # Set up log level for feign behaviors
  log-level: BASIC
  # Packages to be scanned for interfaces declared with @FeignClient
  base-packages: example.client

postman-echo:
  # The base url for your API interface, e. g. @FeignClient(url = "${postman-echo.domain}")
  domain: https://postman-echo.com
```

* Use `@Autowired` to autowire the API interfaces and further use it:

```java
    @Autowired
    private PostmanEchoClient postmanEchoClient;

    public void run(String... args) throws Exception {
        TimeObjectGetRespDTO respDTO1 = postmanEchoClient.getTimeObject("2016-10-10");
        log.info("{}", respDTO1);
    }
```

### Examples
Look up the [example](https://github.com/ethancommitpush/spring-boot-starter-openfeign/tree/master/example).

---
