package some;

import org.springframework.context.annotation.*;
import org.springframework.core.type.AnnotatedTypeMetadata;

/*
 * Libraries to make this #grazie suppression# shit work:
 *     apache:commons-logging:1.2,
 *     spring:spring-aop:5.3.16,
 *     spring:spring-beans:5.3.16,
 *     spring:spring-context:5.3.16,
 *     spring:spring-core:5.3.16,
 *     spring:spring-expression:5.3.16
 * */
@SuppressWarnings("GrazieInspection")
public class tsk10 {

    static void prt(Object a) { System.out.println(a); }

    /////////////////////////////////////////////////////

    static final String __KINDA_APP_ARGUMENT__ = bxr.class.getSimpleName();

    public static void main(String[] args) {
        new AnnotationConfigApplicationContext(bncnf.class)
                .getBean(rnr.class)
                .fgh();
    }

    static class rnr {
        private final fgh f;
        rnr(fgh _f) { f = _f; }
        void fgh() { f.fgh(); }
    }

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Configuration
    static class bncnf {
        @Bean @Conditional(stc.class) fgh stf() { return new stf(); }
        @Bean @Conditional(bxc.class) fgh bxr() { return new bxr(); }
        @Bean @Conditional(jdc.class) fgh jdk() { return new jdk(); }
        @Bean rnr rnr(/*suppression*/ fgh a) { return new rnr(a); }
    }

    static abstract class cnd implements Condition {
        private final Class<? extends fgh> a;
        cnd(Class<? extends fgh> b) { a = b; }

        @SuppressWarnings("NullableProblems")
        @Override
        public boolean matches(
            ConditionContext c, // suppression
            AnnotatedTypeMetadata d // suppression
        ) { return __KINDA_APP_ARGUMENT__.equals(a.getSimpleName()); }
    }

    static class stc extends cnd { stc() { super(stf.class); } }
    static class bxc extends cnd { bxc() { super(bxr.class); } }
    static class jdc extends cnd { jdc() { super(jdk.class); } }

    /////////////////////////////////////////////////////

    @FunctionalInterface
    interface F<T> { void a(T a); }

    static final
    F<Class<? extends fgh>> nm = a -> prt(a.getSimpleName());

    @SuppressWarnings("MethodNameSameAsClassName")
    interface fgh
    { void fgh(); }

    static class stf implements fgh
    { @Override public void fgh() { nm.a(getClass()); } }

    static class bxr implements fgh
    { @Override public void fgh() { nm.a(getClass()); } }

    static class jdk implements fgh
    { @Override public void fgh() { nm.a(getClass()); } }
}
