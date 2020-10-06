[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.ethancommitpush/spring-boot-starter-openfeign/badge.png)](https://search.maven.org/artifact/spring-boot-starter-openfeign/spring-boot-starter-openfeign/)

Spring Boot Starter OpenFeign
=======================

This library makes it easier to automatically use Feign.builder() to construct API interfaces with your custom components declared with a `@FeignClient` annotation. 

# Artifacts

## Maven configuration

Add the Maven dependency:

```xml
<dependency>
  <groupId>com.github.ethancommitpush</groupId>
  <artifactId>spring-boot-starter-openfeign</artifactId>
  <version>${version}</version>
</dependency>
```

## Gradle 

```groovy
compile group: 'com.github.ethancommitpush', name: 'spring-boot-starter-openfeign', version: '${version}'
```

# Usage

* Use `@FeignClient` to declare custom components to be generated as API interfaces:

```java
@Headers({"Content-Type: application/json"})
@FeignClient(url = "${postman-echo.domain}")
public interface PostmanEchoClient {

    @RequestLine("GET /time/object?timestamp={timestamp}")
    TimeObjectGetRespDTO getTimeObject(@Param("timestamp") String timestamp);

}
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
