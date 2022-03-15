package some;

import java.util.ArrayList;

public class tsk8 {

    static void prt(Object a) { System.out.println(a); }

    @SuppressWarnings("StatementWithEmptyBody")
    public static void main(String[] args) throws Exception {
        var a = new Obvl() { @Override void nf() {
            prt("notifying...");
            super.nf();
        } };

        for (var i = 0; i < 5; i++,
            a.dd(() -> prt("update")) );
        a.nf();

        new Ctx(1);
        new Ctx(2);
    }

    ////////////////////////////////////////

    interface Obsr { void up(); }

    static abstract class Obvl {
        private final ArrayList<Obsr> _a = new ArrayList<>();
        void nf() { _a.forEach(Obsr::up); }
        final void dd(Obsr a) { _a.add(a); }
    }

    ////////////////////////////////////////

    static abstract class St {
        abstract int a();
        @Override public String toString() { return "" + a(); }
    }

    @SuppressWarnings("unused")
    static class St1 extends St { @Override public int a() { return 1; } }
    @SuppressWarnings("unused")
    static class St2 extends St { @Override public int a() { return 2; } }

    static class Ctx {
        Ctx(int a) throws Exception
        { prt(Class
            .forName(
                tsk8.class.getCanonicalName() +
                '$' +
                St.class.getSimpleName() + a)
            .getDeclaredConstructors()[0]
            .newInstance()); }
    }
}
