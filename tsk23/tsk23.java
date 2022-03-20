package a;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.*;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.*;
import javax.sql.DataSource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

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
@SpringBootApplication public class tsk23 {
    private static final String db = "db";
    private static final String tb1 = "urs";
    private static final String tb2 = "dgs";
    private static final String rl = "user";

    @SneakyThrows public static void main(String[] args) {
        val a = new SpringApplication(tsk23.class);

        val b = new HashMap<String, Object>();
        b.put("management.endpoints.web.exposure.include", "*");
        b.put("spring.main.allow-bean-definition-overriding", "true");

        a.setDefaultProperties(b);
        a.run(args);
    }

    ////////////////////////////////////////////////////////////////////// configuration

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
            return new JpaTransactionManager(
                requireNonNull(a.getObject())); }
    }

    @EnableWebSecurity @Configuration
    public static class B extends WebSecurityConfigurerAdapter {

        @Override protected void
        configure(AuthenticationManagerBuilder a)
        { a.authenticationProvider(ap()); }

        @Override public void configure(WebSecurity a)
        { a.ignoring().antMatchers("/resources/**", "/static/**"); }

        @Override protected void configure(HttpSecurity a) throws Exception { a
            .csrf().disable().cors().disable()
            .authorizeRequests().antMatchers("/lg", "/", "rg").permitAll()
            .antMatchers("/ls").authenticated()
            .and().formLogin().usernameParameter("em").passwordParameter("ps")
                .loginPage("/lg").failureUrl("/lg?er=true").permitAll()
            .and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/lgo"))
                .logoutSuccessUrl("/").permitAll();
        }

        @Bean public PasswordEncoder en() { return new BCryptPasswordEncoder(); }

        @SneakyThrows @Bean public AuthenticationProvider ap() {
            val a = new DaoAuthenticationProvider();
            a.setUserDetailsService(getApplicationContext().getBean(ussi.class));
            a.setPasswordEncoder(en());
            return a;
        }
    }

    ////////////////////////////////////////////////////////////////////// controller

    @Controller public static class rnl {
        private static final String us = "usr";
        private static final String rg = "reg";
        private static final String lg = "log";
        private static final String ls = "lst";
        private static final String rs = "res";
        @Autowired private ussi rp;

        @GetMapping("") public String nx() { return "index"; }

        @GetMapping("/rg") public String rg(Model a) {
            a.addAttribute(us, new usr());
            return rg;
        }

        @PostMapping("/rrg") public String rgg(usr a, Model b) {
            val d = new ModelAndView();
            val e = rp.ctn(a.em);

            if (!e) {
                a.ps = new BCryptPasswordEncoder().encode(a.ps);
                rp.dd(a);
            }

            val f = !e ? "successfull" : "failed";
            b.addAttribute("rs", new rs(f, f));
            return rs;
        }

        @GetMapping("/lg") public String lg(Model a) {
            a.addAttribute("lg", new lg(null, null));
            return lg;
        }

        @GetMapping("/ls") public String ls(WebRequest a, Model b) {
            b.addAttribute("lst", rp.gal());
            return ls;
        }
    }

    ////////////////////////////////////////////////////////////////////// service

    @SuppressWarnings("unused") private interface $$<T extends _$, R extends $$$$<T>>
    { List<T> gal(); void dd(T a); void dl(int a); T nw(T a); String gt(); }

    public interface uss extends $$<usr, usp> {}
    public interface dgs extends $$<dg, dgp> {}

    @RequiredArgsConstructor public abstract static class
    asv<T extends _$, R extends $$$$<T>> implements $$<T, R> {
        protected final R rp;

        @Transactional(readOnly = true) @Override
        public List<T> gal() { return rp.findAll(); }

        @Transactional @Override public void dd(T a) { rp.save(a); }
        @Transactional @Override public void dl(int a) { rp.deleteById(a); }

        @Override public T nw(T a) { dd(a); return a; }
        @Override public String gt() { return gal().toString(); }
    }

    @Service public static class ussi extends asv<usr, usp>
    implements uss, UserDetailsService {
        @Autowired public ussi(usp a) { super(a); }

        public boolean ctn(String a) { return rp.ibe(a) != null; }
        @Nullable public usr ibe(String a) { return rp.ibe(a); }

        @Override public UserDetails loadUserByUsername(String a)
        throws UsernameNotFoundException {
            val b = rp.ibe(a);
            if (b == null) throw new UsernameNotFoundException(null);

            return User.withUsername(a).password(b.ps).authorities(rl).build();
//            return new User(b.em, b.ps,
//                Collections.singleton(new SimpleGrantedAuthority(rl)));
        }
    }

    @Service public static class dgsi
    extends asv<dg, dgp> implements dgs
    { public dgsi(dgp a) { super(a); } }

    ////////////////////////////////////////////////////////////////////// repository

    @NoRepositoryBean public interface
    $$$$<T extends _$> extends JpaRepository<T, Integer> {}

    @Repository public interface usp extends $$$$<usr> {
        @Query(value = "select * from " + tb1 + " where em = ?1",
               nativeQuery = true)
        @Nullable usr ibe(String a);
    }

    @Repository public interface dgp extends $$$$<dg> {}

    ////////////////////////////////////////////////////////////////////// data

    public static record rs(String tl, String hd) {}
    public static record lg(String l, String p) {}

    @MappedSuperclass
    public static abstract class _$ implements Serializable
    { @GeneratedValue(strategy = GenerationType.IDENTITY)
      @Nullable @Id Integer id; }

    @Table(name = tb1) @Entity
    @RequiredArgsConstructor @Getter @Setter
    public static class usr extends _$ {
        @NonNull public String fn;
        @NonNull public String ln;
        @NonNull public String ps;
        @NonNull public String em;

        @OneToMany(
            mappedBy = "rs",
            fetch = FetchType.EAGER)
        List<dg> dgs = new ArrayList<>();

        public usr() { fn = ""; ln = ""; ps = ""; em = ""; }

        @Override public String toString()
        { return "(%d %s %s %s %s %s)"
            .formatted(id, fn, ln, ps, em, dgs
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
