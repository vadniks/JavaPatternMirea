package a;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.*;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.Objects;
import java.util.Properties;

/*
sudo mkdir /var/lib/postgres
sudo chown postgres /var/lib/postgres
sudo chgrp postgres /var/lib/postgres
sudo mkdir /run/postgresql
sudo chgrp postgres /run/postgresql
sudo chown postgres /run/postgresql

initdb -D data/
pg_ctl -D /var/lib/postgres/data -l logfile start
createdb usrs
psql

CREATE TABLE usrs(
    id INT PRIMARY KEY,
    fn VARCHAR(45) NOT NULL,
    ln VARCHAR(45) NOT NULL
);

psql -h localhost -p 5432 -U postgres -d usrs
*/
@SpringBootApplication
public class tsk15 {
    private static final String dbnm = "usrs";

    public static void main(String[] args)
    { SpringApplication.run(tsk15.class, args); }

    @Configuration
    public static class A {

        @Bean public HikariDataSource ds() {
            var a = new HikariConfig();
            a.setJdbcUrl("jdbc:postgresql://localhost:5432/" + dbnm);
            a.setDriverClassName("org.postgresql.Driver");
            a.setUsername("postgres");
            a.setPassword("postgres");
            return new HikariDataSource(a);
        }

        @Bean public LocalSessionFactoryBean fc(DataSource b) {
            var a = new LocalSessionFactoryBean();
            a.setDataSource(b);
            a.setPackagesToScan(tsk15.class.getPackageName());

            var c = new Properties();
            c.setProperty("hibernate.dialect",
                "org.hibernate.dialect.PostgreSQL92Dialect");
            a.setHibernateProperties(c);
            return a;
        }

        @Bean public PlatformTransactionManager
        pt(LocalSessionFactoryBean a) {
            return new HibernateTransactionManager(
                Objects.requireNonNull(a.getObject())); }
    }

    @Component @RequiredArgsConstructor
    public static class ursv {
        private final SessionFactory sf;
        private Session s;

        @SneakyThrows @PostConstruct
        public void a() {
            s = sf.openSession();
//            nw(null, "test", "test");
            System.out.println("testo " + gt(1));
        }

        public void nw(@Nullable Integer a, String b, String c) {
            var d = s.beginTransaction();
            s.persist(new usr(a == null ? 1 : a, b, c));
            d.commit();
        }

        public usr gt(@NotNull Integer a)
        { return s.get(usr.class, a); }
    }

    @Table(name = dbnm) @Entity @ToString
    @Getter @Setter @AllArgsConstructor                     // <-- Annotation madness
    @EntityListeners(AuditingEntityListener.class)
    public static class usr {
        @Id private int id;
        @Column(name = "fn") private String fn;
        @Column(name = "ln") private String ln;

        @SuppressWarnings("unused") public usr() {}
    }
}
