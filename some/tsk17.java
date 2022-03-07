package a;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jetbrains.annotations.Nullable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.persistence.*;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.sql.DataSource;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;

@SpringBootApplication
public class tsk17 {
    private static final String db = "db";
    private static final String tb1 = "urs";
    private static final String tb2 = "dgs";

    public static void main(String[] args)
    { SpringApplication.run(tsk17.class, args); }

    @Configuration
    public static class A {

        @Bean public HikariDataSource ds() {
            val a = new HikariConfig();
            a.setJdbcUrl("jdbc:postgresql://localhost:5432/" + db);
            a.setDriverClassName("org.postgresql.Driver");
            a.setUsername("postgres");
            a.setPassword("postgres");
            return new HikariDataSource(a);
        }

        @Bean public LocalSessionFactoryBean fc(DataSource b) {
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

        @Bean public PlatformTransactionManager
        pt(LocalSessionFactoryBean a) {
            return new HibernateTransactionManager(
                Objects.requireNonNull(a.getObject())); }
    }

    @Service @RequiredArgsConstructor
    @Component @Endpoint(id = db)
    public static class sr {
        private final SessionFactory sf;
        private static Session s;

        @PostConstruct public void a() { s = sf.openSession(); }
        @PreDestroy public void b() { s.close(); }

        // curl -X POST -d '{"a":"a","b":"b"}' -H \
        //     "Content-type: application/json" 'http://localhost:8080/actuator/db'
        @WriteOperation @SneakyThrows
        public String nw(String a, String b)
        { return nw(usr.class, new usr(a, b)).toString(); }

        public <T> T nw(Class<?> b, T a) {
            val d = s.beginTransaction();
            s.persist(b.getSimpleName(), a);
            d.commit();
            return a;
        }

        // curl 'http://localhost:8080/actuator/db?a=usr&b=id&c=1'
        @SneakyThrows @ReadOperation
        public String
        gf(String a, String b, Object c) { return gf(
            Class.forName(tsk17.class.getCanonicalName() + '$' + a), b, c
        ).toString(); }

        public <T> T gt(int a, Class<T> b)
        { return s.get(b, a); }

        public <T> List<T> gsr(
            Class<T> e,
            String d,
            boolean f
        ) {
            val c = s.getCriteriaBuilder();
            val a = c.createQuery(e);
            val b = a.from(e);

            Function<Expression<?>, Order> g =
                f ? c::asc : c::desc;

            a.select(b).orderBy(g.apply(b.get(d)));
            return s.createQuery(a).getResultList();
        }

        public <T> List<T>
        gf(Class<T> a, String f, Object g) {
            val c = s.getCriteriaBuilder();
            val d = c.createQuery(a);
            val e = d.from(a);

            d.select(e).where(c.equal(e.get(f), g));
            return s.createQuery(d).getResultList();
        }
    }

    @Table(name = tb1) @Entity
    @RequiredArgsConstructor
    public static class usr
    implements Serializable {

        @GeneratedValue(strategy =
            GenerationType.IDENTITY)
        @Nullable @Id Integer id;

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

        public static final String[] fs =
            Arrays.stream(usr.class.getDeclaredFields())
                .map(Field::getName)
                .toArray(String[]::new);
    }

    @Table(name = tb2) @Entity @ToString
    @RequiredArgsConstructor
    public static class dg
    implements Serializable {

        @GeneratedValue(strategy =
            GenerationType.IDENTITY)
        @Id @Nullable Integer id;

        @NonNull String nm;
        @NonNull String bd;

        @ManyToOne
        @NonNull usr rs;

        public dg() {}

        public static final String[] fs =
            Arrays.stream(dg.class.getDeclaredFields())
                .map(Field::getName)
                .toArray(String[]::new);
    }
}

