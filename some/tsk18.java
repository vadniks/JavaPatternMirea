package a;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.val;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.*;
import javax.sql.DataSource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

/**
 ______  ____    ___   ____
 /_  __/ / __ \  / _ \ / __ \
 / /   / /_/ / / // // /_/ /
 /_/    \____/ /____/ \____/

 Add endpoints!
 */

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
*/
@SpringBootApplication
public class tsk18 {
    private static final String db = "db";
    private static final String tb1 = "urs";
    private static final String tb2 = "dgs";

    public static void main(String[] args)
    { SpringApplication.run(tsk18.class, args); }

    ////////////////////////////////////////////////////////////////////// configuration

    @Configuration @EnableJpaRepositories(considerNestedRepositories = true)
    public static class A {

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
                Objects.requireNonNull(a.getObject())); }
    }

    ////////////////////////////////////////////////////////////////////// service

    private interface $$<T extends _$, R extends $$$$<T>>
    { List<T> gal(); void dd(T a);
      void dl(int a); @TestOnly void $(); }

    public interface uss extends $$<usr, usp> {}
    public interface dgs extends $$<dg, dgp> {}

    @RequiredArgsConstructor
    private abstract static class
    $$$<T extends _$, R extends $$$$<T>> implements $$<T, R> {

        private final R rp;
        @Override public List<T> gal() { return rp.findAll(); }
        @Override public void dd(T a) { rp.save(a); }
        @Override public void dl(int a) { rp.deleteById(a); }

        @SuppressWarnings("unchecked")
        @TestOnly @Override
        public void $() {
            dd((T) (
                rp instanceof usp ?
                    new usr("fn", "ln") :
                    new dg("nm", "bd"))
            );
            System.out.println(gal());
        }
    }

    @Service public static class ussi extends $$$<usr, usp> implements uss
    { public ussi(usp a) { super(a); $(); } }

    @Service public static class dgsi extends $$$<dg, dgp> implements dgs
    { public dgsi(dgp a) { super(a); $(); } }

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
