package a;

//import com.fasterxml.jackson.annotation.JsonBackReference;
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.persistence.*;
import javax.sql.DataSource;
import javax.transaction.Synchronization;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;

/*
sudo mkdir /run/postgresql
sudo chown postgres /run/postgresql
sudo chgrp postgres /run/postgresql
sudo su - postgres
    pg_ctl -D /var/lib/postgres/data start
    createdb db
    psql -d db
psql -h localhost -p 5432 -U postgres -d db
    pg_ctl -D /var/lib/postgres/data stop

# Hibernate: create sequence hibernate_sequence start 1 increment 1
# Hibernate: create table dgs (id int4 not null, bd varchar(255), nm varchar(255), rs_id int4, primary key (id))
# Hibernate: create table urs (id int4 not null, fn varchar(255), ln varchar(255), primary key (id))
# Hibernate: alter table if exists dgs add constraint FK1n8n2m20onaxgxi49a460eb6e foreign key (rs_id) references urs
# Hibernate: select tsk15_usr0_.id as id1_2_0_, tsk15_usr0_.fn as fn2_2_0_, tsk15_usr0_.ln as ln3_2_0_ from usrs tsk15_usr0_ where tsk15_usr0_.id=?
*/
@SpringBootApplication
public class tsk16 {
    private static final String db = "db";
    private static final String tb1 = "urs";
    private static final String tb2 = "dgs";

    public static void main(String[] args)
    { SpringApplication.run(tsk16.class, args); }

    @SuppressWarnings("DuplicatedCode") @Configuration
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
            c.setProperty("hibernate.hbm2ddl.auto",
                DEBUG ? "create" : "update");
            a.setHibernateProperties(c);
            return a;
        }

        @Bean public PlatformTransactionManager
        pt(LocalSessionFactoryBean a) {
            return new HibernateTransactionManager(
                Objects.requireNonNull(a.getObject())); }
    }

private static final boolean DEBUG = false; // #define

    @Service @RequiredArgsConstructor
    public static class sr {
        private final SessionFactory sf;
        private Session s;

        @PostConstruct public void a()
        { s = sf.openSession(); $(); }

        @PreDestroy public void b() { s.close(); }

        @TestOnly public void $() {
if (!DEBUG) { // #if
            System.out.println(gt(1, usr.class));
            System.out.println(gt(1, dg.class));
            System.out.println(gt(2, dg.class));
} else { // #else
            nw(
                usr.class,
                new usr(
                    "name",
                    "surname"),
                null,
                null);
            nw(
                dg.class,
                new dg(
                    "dog",
                    "breed",
                    gt(1, usr.class)),
                null,
                null);
            nw(
                dg.class,
                new dg(
                    "dog",
                    "breed",
                    gt(1, usr.class)),
                null,
                null);
}       } // #endif

        @FunctionalInterface
        public interface clb { void a(); }

        @SuppressWarnings("UnusedReturnValue")
        public <T> T nw(
            Class<?> b,
            T a,
            @Nullable clb c,
            @Nullable AtomicReference<Transaction> e
        ) {
            val d = s.beginTransaction();
            s.persist(b.getSimpleName(), a);

            d.registerSynchronization(new Synchronization()
            { @Override public void beforeCompletion() {}
              @Override public void afterCompletion(int i)
              { if (c != null) c.a(); } });

            if (e != null) e.set(d);

            d.commit();
            return a;
        }

        public <T> T gt(int a, Class<T> b)
        { return s.get(b, a); }

        @Deprecated(forRemoval = true)
        public usr gu(int a) { return ((dg) s
            .createSQLQuery("select * from %s where id = :a"
                .formatted(tb2)
            ).setParameter("a", a).uniqueResult()).rs; }

        @Deprecated(forRemoval = true)
        @SuppressWarnings("unchecked")
        public List<dg> gds(int a) { return (List<dg>) s
            .createSQLQuery("select dgs from %s where id = :a"
                .formatted(tb1)
            ).setParameter("a", a).getResultList(); }
    }

    @Table(name = tb1) @Entity
    @RequiredArgsConstructor
    public static class usr {

        @GeneratedValue(strategy =
            GenerationType.IDENTITY)
        @Nullable @Id Integer id;

        @NonNull String fn;
        @NonNull String ln;

        @OneToMany(
            mappedBy = "rs",
            fetch = FetchType.EAGER)
//        @JsonIgnoreProperties("rs")
//        @JsonManagedReference
        List<dg> dgs = new ArrayList<>();

        public usr() {}

        @Override public String toString()
        { return "(%d %s %s %s)"
              .formatted(
                  id,
                  fn,
                  ln,
                  dgs
                      .stream()
                      .map(a -> "(%d %s %s)"
                          .formatted(a.id, a.nm, a.bd))
                      .toList()); }
    }

    @Table(name = tb2) @Entity @ToString
    @RequiredArgsConstructor
    public static class dg {

        @GeneratedValue(strategy =
            GenerationType.IDENTITY)
        @Id @Nullable Integer id;

        @NonNull String nm;
        @NonNull String bd;

        @ManyToOne
//        @JoinColumn(
//            name = "rs_id",
//            nullable = false,
//            updatable = false)
//        @JsonBackReference
        @NonNull usr rs;

        public dg() {}
    }
}
