[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.ethancommitpush/spring-boot-starter-openfeign/badge.png)](https://search.maven.org/artifact/com.github.ethancommitpush/spring-boot-starter-openfeign/)

Spring Boot Starter OpenFeign
=======================

This library makes it easier to automatically use Feign.builder() to construct API interfaces with your custom components declared with a `@FeignClient` annotation. 
There is no any configures about Ribbon or Eureka for this library, just simply visits the APIs with the domain URL and path.

# Artifacts



## What's New?
### [1.2.0](https://mvnrepository.com/artifact/com.github.ethancommitpush/spring-boot-starter-openfeign/1.2.0) - 25th January 2021
* Enhance the @FeignClient annotation customizations including fully configurable HTTP client, request encoder, response decoder, and error response decoder
* Enhance the properties file customizations including configurable logLevel and loggerType
### [1.1.3](https://mvnrepository.com/artifact/com.github.ethancommitpush/spring-boot-starter-openfeign/1.1.3) - 9th January 2021
* Fix CustomErrorDecoder error and upgrade to compatible with feign ^10.7.3 ~10.12

## Requirements
The following table shows the feign versions that are used by version of spring-boot-starter-openfeign:

| spring-boot-starter-openfeign        | feign  |
| :-------------: |:-------------:|
| 1.2.0<br />1.1.3      | ^10.7.3 |
| 1.1.2      | 10.7.2 |

## Maven Configuration

Add the Maven dependency:

```xml
<dependency>
  <groupId>com.github.ethancommitpush</groupId>
  <artifactId>spring-boot-starter-openfeign</artifactId>
  <version>1.2.0</version>
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

<dependency>
  <groupId>io.github.openfeign</groupId>
  <artifactId>feign-slf4j</artifactId>
  <version>10.7.3</version>
</dependency>
```

## Gradle 

```groovy
compile group: 'com.github.ethancommitpush', name: 'spring-boot-starter-openfeign', version: '1.2.0'
compile group: 'io.github.openfeign', name: 'feign-core', version: '10.7.3'
compile group: 'io.github.openfeign', name: 'feign-jackson', version: '10.7.3'
compile group: 'io.github.openfeign', name: 'feign-httpclient', version: '10.7.3'
compile group: 'io.github.openfeign', name: 'feign-slf4j', version: '10.7.3'
```

# Basic Usage

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
  # Set up logger type to append logs
  logger-type: SLF4J
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

# Advanced Usage

* The following table shows the components and iots default bean names which were used by spring-boot-starter-openfeign. If you want to customize some components of them, just implement the component interfaces and delare them as beans:

| Component Type        | Default Bean Name      | Component Interface      |
|:-------------:| :-------------: |:-------------:|
| HTTP Client      | feignClient      | [feign.Client](https://github.com/OpenFeign/feign/blob/10.7.3/core/src/main/java/feign/Client.java)      |
| Request Encoder      | feignEncoder      | [feign.codec.Encoder](https://github.com/OpenFeign/feign/blob/10.7.3/core/src/main/java/feign/codec/Encoder.java)      |
| Response Decoder      | feignDecoder      | [feign.codec.Decoder](https://github.com/OpenFeign/feign/blob/10.7.3/core/src/main/java/feign/codec/Decoder.java)      |
| Error Response Decoder      | feignErrorDecoder      | [feign.codec.ErrorDecoder](https://github.com/OpenFeign/feign/blob/10.7.3/core/src/main/java/feign/codec/ErrorDecoder.java)      |

* Use `@Configuration` to declare a default decoder for all API interfaces with `@FeignClient` annotation:

```java
@Configuration
@Slf4j
public class FeignConfig {
    @Bean
    public Decoder feignDecoder() {
        return new JacksonDecoder() {
            @Override
            public Object decode(Response response, Type type) throws IOException {
                log.info("inside overridden feignDecoder.decoder()");
                return super.decode(response, type);
            }
        };
    }
}
```

* Use `@Configuration` to declare a custom decoder, and assign to certain API interface with `@FeignClient` annotation:

```java
@Configuration
@Slf4j
public class FeignConfig {
    @Bean
    public Decoder myDecoder() {
        return new JacksonDecoder() {
            @Override
            public Object decode(Response response, Type type) throws IOException {
                log.info("inside myDecoder.decoder()");
                return super.decode(response, type);
            }
        };
    }
}
```

```java
@FeignClient(url = "${postman-echo.domain}", decoder = "myDecoder")
public interface PostmanEchoClient2 {

    @RequestLine("POST /post?foo1={foo1}&foo2={foo2}")
    PostPostRespDTO postPost(@Param("foo1") String foo1, @Param("foo2") String foo2);

}
```

---
