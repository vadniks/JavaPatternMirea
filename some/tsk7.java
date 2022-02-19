package some;

public class tsk7 {

    @FunctionalInterface interface F<T, R> { R a(T a); }

    static <T> T prt(T a, F<T, String> d, String b) {
        System.out.println("" +
            (d != null ? d.a(a) : a) +
            (b != null ? b : ""));
        return a;
    }

    ////////////////////////////////////////

    @SuppressWarnings("StatementWithEmptyBody")
    public static void main(String[] args) {
        for (var i = 0; i <= _a; i++,
            prt(FlWgPrFct.a((int) (Math.random() * 9)).a(), null, null) );

        prt(((Any) new Prx(new Rl(5), a -> a * 1000)).a(), null, null);
    }

    ////////////////////////////////////////

    final static int _a = 9;

    interface IFlWgPr { int a(); }

    static record FlWgPr(int a) implements IFlWgPr
    { @Override public int a() { return a; } }

    static class FlWgPrFct {
        private static final IFlWgPr[] a = new IFlWgPr[_a];
        private FlWgPrFct() {}

        static IFlWgPr a(int c) { return
            a[c] == null ?
                a[c] = prt(new FlWgPr(c), b -> "" + b.a(), " created") :
                a[c]; }
    }

    ////////////////////////////////////////

    interface Any { int a(); }

    static record Rl(int b) implements Any
    { @Override public int a() { return b; } }

    static record Prx(Any a_, F<Integer, Integer> c) implements Any
    { @Override public int a() { return c.a(a_.a()); } }
}
