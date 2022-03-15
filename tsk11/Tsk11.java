package a;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class Tsk11 {

    public static void main(String[] args) {
        SpringApplication.run(Tsk11.class, args);
        System.out.println("Hello World!");
    }

    @SuppressWarnings("unused")
    @Component
    @Endpoint(id = "test")
    public static class A {
        private String d = "a";
        // curl localhost:8080/actuator/test
        @ReadOperation public String a() { return d; }
        // curl -X POST -d '{"c":"hello"}' -H "Content-type:application/json" http://localhost:8080/actuator/test
        @WriteOperation public void b(String c) { d = c; }
    }
}
