package a;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class tsk13 {

    public static void main(String[] args) {
        SpringApplication.run(tsk13.class, args);
    }

    @Component
    public static class Student {
        @Value("${student.name}") private String nm;
        @Value("${student.last_name}") private String lnm;
        @Value("${student.group}") private String grp;

        @PostConstruct public void a() { System.out.println(nm + ' ' + lnm + ' ' + grp); }
    }
}
