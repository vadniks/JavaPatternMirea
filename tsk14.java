package a;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.endpoint.annotation.DeleteOperation;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

// .../res/application.properties:
//     management.endpoints.web.exposure.include=hm,ps,us
@SpringBootApplication
public class tsk14 {

    public static void main(String[] args)
    { SpringApplication.run(tsk14.class, args); }

    public static class ps {
        private String tx;
        private long cd;

        @Override public String toString() { return tx + ' ' + cd; }
    }

    public static class us {
        private String fn;
        private String ln;
        private String mn;
        private long bd;

        @Override public String toString()
        { return fn + ' ' + ln + ' ' + mn + ' ' + bd; }
    }

    @SuppressWarnings("unused") @Component @Endpoint(id = "hm")
    public static class A {
        // curl localhost:8080/actuator/hm
        @ReadOperation public String a() { return """
            <html>
            <body>
                <span id="ln">ln</span>
                <span id="nm">nm</span>
                <span id="gp">ik</span>
                <span id="vr">06</span>
            </body>
            """; }
    }

    @SuppressWarnings("unused") @Component @Endpoint(id = "ps")
    public static class B {
        private ps p = null;

        // curl localhost:8080/actuator/ps
        @ReadOperation public String gt()
        { return p == null ? "null" : p.toString(); }

        // curl -X POST -d '{"a":"ps","b":123}' \
        //    -H "Content-type:application/json" http://localhost:8080/actuator/ps
        @WriteOperation
        public void st(String a, long b) {
            var q = p == null ? (p = new ps()) : p;
            q.tx = a;
            q.cd = b;
        }

        // curl -X DELETE localhost:8080/actuator/ps
        @DeleteOperation public void dl() { p = null; }
    }

    @SuppressWarnings("unused") @Component @Endpoint(id = "us")
    public static class C {
        private us u = null;

        // curl localhost:8080/actuator/us
        @ReadOperation public String gt()
        { return u == null ? "null" : u.toString(); }

        // curl -X POST -d '{"a":"fn","b":"ln","c":"mn","d":123}' \
        //    -H "Content-type:application/json" http://localhost:8080/actuator/us
        @WriteOperation
        public void st(String a, String b, String c, long d) {
            var q = u == null ? (u = new us()) : u;
            q.fn = a;
            q.ln = b;
            q.mn = c;
            q.bd = d;
        }

        // curl -X DELETE localhost:8080/actuator/us
        @DeleteOperation public void dl() { u = null; }
    }
}
