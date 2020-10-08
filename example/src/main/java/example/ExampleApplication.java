package example;

import example.client.PostmanEchoClient;
import example.client.PostmanEchoClient2;
import example.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class ExampleApplication implements CommandLineRunner {

    @Autowired
    private PostmanEchoClient postmanEchoClient;
    @Autowired
    private PostmanEchoClient2 postmanEchoClient2;

    public static void main(String[] args) throws Exception {
        SpringApplication.run(ExampleApplication.class, args);
    }

    /** For runtime initialization usage */
    @Override
    public void run(String... args) throws Exception {
        TimeObjectGetRespDTO respDTO1 = postmanEchoClient.getTimeObject("2016-10-10");
        log.info("{}", respDTO1);

        PostPostRespDTO respDTO2 = postmanEchoClient2.postPost("bar1", "bar2");
        log.info("{}", respDTO2);

        HeadersGetRespDTO respDTO3 = postmanEchoClient.getHeaders("Lorem ipsum dolor sit amet");
        log.info("{}", respDTO3);

        TransformCollectionPostReqDTO reqDTO4 = new TransformCollectionPostReqDTO();
        reqDTO4.setName("Sample Postman Collection");
        reqDTO4.setDescription("A sample collection to demonstrate collections as a set of related requests");
        TransformCollectionPostRespDTO respDTO4 = postmanEchoClient.postTransformCollection(1, 2, reqDTO4);
        log.info("{}", respDTO4);
    }
    
}
