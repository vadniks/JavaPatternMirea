package a;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import javax.management.ObjectName;
import javax.persistence.*;
import javax.sql.DataSource;
import java.io.File;
import java.io.FileWriter;
import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.util.*;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

/*
sudo mkdir /run/postgresql
sudo chgrp postgres /run/postgresql
sudo chown postgres /run/postgresql
sudo su - postgres
    pg_ctl -D /var/lib/postgres/data start
    psql -d db
        select * from urs;
        select * from dgs;
    pg_ctl -D /var/lib/postgres/data stop

management.endpoints.web.exposure.include=usr,dg
*/
@SpringBootApplication @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
public class tsk22 {
    private static final String db = "db";
    private static final String tb1 = "urs";
    private static final String tb2 = "dgs";

    @SneakyThrows
    public static void main(String[] args) {
        val a = new SpringApplication(tsk22.class);
        a.setDefaultProperties(
            Collections.singletonMap("server.port", 8081));
        val b = a.run(args);

        ManagementFactory
            .getPlatformMBeanServer()
            .registerMBean(
                new nb(b.getBean(sch.class)),
                new ObjectName(tsk22.class.getName() + ":a=b"));
    }

    ////////////////////////////////////////////////////////////////////// management

    public interface nbMBean { void wd(); }

    public record nb(sch sc) implements nbMBean
    { @Override public void wd() { sc.a(); } }

    ////////////////////////////////////////////////////////////////////// configuration

    @EnableAspectJAutoProxy @Slf4j @EnableAsync @EnableScheduling
    @EnableJpaRepositories(considerNestedRepositories = true)
    @Configuration public static class A {

        @Bean public DataSource ds() {
            val a = new HikariConfig();
            a.setJdbcUrl("jdbc:postgresql://localhost:5432/" + db);
            a.setDriverClassName("org.postgresql.Driver");
            a.setUsername("postgres");
            a.setPassword("postgres");
            return new HikariDataSource(a);
        }

        @Bean(name = "entityManagerFactory")
        public LocalSessionFactoryBean fc(DataSource b) {
            val a = new LocalSessionFactoryBean();
            a.setDataSource(b);
            a.setPackagesToScan(this.getClass().getPackageName());

            val c = new Properties();
            c.setProperty("hibernate.dialect",
                "org.hibernate.dialect.PostgreSQL92Dialect");
            c.setProperty("hibernate.show_sql", "true");
            c.setProperty("hibernate.hbm2ddl.auto", "update");
            a.setHibernateProperties(c);
            return a;
        }

        @Bean(name = "transactionManager")
        public PlatformTransactionManager
        pt(LocalSessionFactoryBean a) {
            return new HibernateTransactionManager(
                requireNonNull(a.getObject())); }
    }

    @Aspect @Slf4j @Component
    public static class asp {

        @Before("within(a.tsk22$asv+)")
        public void lg(JoinPoint a) { log.info(
            "proxy logging %s".formatted(a.toString())
        ); }
    }

    ////////////////////////////////////////////////////////////////////// service

    @Service public static class sch {
        @Autowired private ussi uu;
        @Autowired private dgsi dd;

        @Scheduled(fixedRate = 30 * 60 * 60 * 1000)
        @Async @SneakyThrows public void a() {
            val b = new File("/home/admin/IdeaProjects/sprBoot/src/main/resources/aa");
            assert b.exists() && b.isDirectory();

            Arrays.stream(requireNonNull(b.listFiles()))
                .map(File::delete).close();

            Stream.of(usr.class, dg.class).forEach(d -> { try {
                System.out.println("gyhbymk " + d);
                val e = d.getSimpleName();

                val f = new File(b, e);
                assert f.createNewFile();

                val g = new FileWriter(f);
                g.write((
                    d.equals(usr.class) ?
                        uu : dd
                ).gt());

                g.flush();
                g.close();
            } catch (Exception ignored) {} });
        }
    }

    @SuppressWarnings("unused") private interface $$<T extends _$, R extends $$$$<T>>
    { List<T> gal(); void dd(T a); void dl(int a); T nw(T a); String gt(); }

    public interface uss extends $$<usr, usp> {}
    public interface dgs extends $$<dg, dgp> {}

    @RequiredArgsConstructor public abstract static class
    asv<T extends _$, R extends $$$$<T>> implements $$<T, R> {
        private final R rp;

        @Transactional(readOnly = true) @Override
        public List<T> gal() { return rp.findAll(); }

        @Transactional @Override public void dd(T a) { rp.save(a); }
        @Transactional @Override public void dl(int a) { rp.deleteById(a); }

        @Override public T nw(T a) { dd(a); return a; }
        @Override public String gt() { return gal().toString(); }
    }

    // GET curl http://localhost:8081/actuator/usr
    @Endpoint(id = "usr") @Service
    public static class ussi extends asv<usr, usp> implements uss {
        public ussi(usp a) { super(a); }

        @ReadOperation public String a() { return gt(); }

        // curl -X POST -d '{"a":"a","b":"b"}' -H \
        //     "Content-type: application/json" 'http://localhost:8081/actuator/usr'
        @WriteOperation public String b(String a, String b)
        { return nw(new usr(a, b)).toString(); }
    }

    // GET curl http://localhost:8081/actuator/dg
    @Endpoint(id = "dg") @Service
    public static class dgsi extends asv<dg, dgp> implements dgs {
        public dgsi(dgp a) { super(a); }

        @ReadOperation public String a() { return gt(); }

        // curl -X POST -d '{"a":"a","b":"b"}' -H \
        //     "Content-type: application/json" 'http://localhost:8081/actuator/dg'
        @WriteOperation public String b(String a, String b)
        { return nw(new dg(a, b)).toString(); }
    }

    ////////////////////////////////////////////////////////////////////// repository

    @NoRepositoryBean public interface
    $$$$<T extends _$> extends JpaRepository<T, Integer> {}

    @Repository public interface usp extends $$$$<usr> {}
    @Repository public interface dgp extends $$$$<dg> {}

    ////////////////////////////////////////////////////////////////////// data

    @MappedSuperclass
    public static abstract class _$ implements Serializable
    { @GeneratedValue(strategy = GenerationType.IDENTITY)
      @Nullable @Id Integer id; }

    @Table(name = tb1) @Entity
    @RequiredArgsConstructor
    public static class usr extends _$ {
        @NonNull String fn;
        @NonNull String ln;

        @OneToMany(
            mappedBy = "rs",
            fetch = FetchType.EAGER)
        List<dg> dgs = new ArrayList<>();

        public usr() {}

        @Override public String toString()
        { return "(%d %s %s %s)"
            .formatted(id, fn, ln, dgs
                .stream()
                .map(a -> "(%d %s %s)"
                    .formatted(a.id, a.nm, a.bd))
                .toList()); }
    }

    @Table(name = tb2) @Entity @ToString
    @RequiredArgsConstructor
    public static class dg extends _$ {
        @NonNull String nm;
        @NonNull String bd;

        @ManyToOne
        @Nullable usr rs;

        public dg() {}
    }
}
