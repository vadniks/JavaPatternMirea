package a;

import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

@SpringBootApplication
public class tsk12 {
    static String __arg1;
    static String __arg2;

    public static void main(String[] args) {
        //noinspection ConstantConditions
        final var a = tsk12
                .class
                .getClassLoader()
                .getResource(".")
                .getPath();

        __arg1 =  a + "/a.txt"; // args[0]
        __arg2 = a + "/b.hash"; // args[1]

        SpringApplication.run(tsk12.class, args);
    }

    @Component
    public static class A {
        final File a = new File(__arg1);
        final File b = new File(__arg2);

        @SuppressWarnings("StringConcatenationInLoop")
        @SneakyThrows
        @PostConstruct
        public void st() {
            var c = "";
            var e = a.exists();
            if (e) {
                final var r = new FileReader(a);
                var d = 0;

                while ((d = r.read()) != -1)
                    c += (char) d;
                r.close();
            } else c = "null";

            assert b.exists() || b.createNewFile();

            final var w = new FileWriter(b);
            w.write(String.valueOf(e ? c.hashCode() : c));
            w.close();
        }

        @SuppressWarnings("ResultOfMethodCallIgnored")
        @PreDestroy public void en() { a.delete(); }
    }
}
