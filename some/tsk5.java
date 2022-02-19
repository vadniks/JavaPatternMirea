package some;

public class tsk5 {

    static void prt(Object a) { System.out.println(a); }

    public static void main(String[] args) {
        //noinspection ConstantConditions
        prt(A.a == null);
        prt(B.gb() == null);
        prt(C.gc() == null);
    }

    @SuppressWarnings("InstantiationOfUtilityClass")
    static class A {
        static final A a = new A();
        private A() {}
    }

    static class B {
        private static B b = null;
        private B() { b  = this; }
        static B gb() { return b; }
    }

    static {
        try {
            final var b = B.class.getDeclaredFields()[0];
            b.setAccessible(true);
            b.set(null, B.class.getDeclaredConstructors()[0].newInstance());
        } catch (Exception a) { a.printStackTrace(); }
    }

    static class C {
        private static C c = null;
        private C() {}
        static C gc() { return c == null ? c = new C() : c; }
    }
}
