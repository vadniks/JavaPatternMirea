package some;

public class tsk6 {

    static void prt(Object a) { System.out.println(a); }

    @SuppressWarnings("unused")
    public static void main(String[] args) {
        FctPrd a = FctCrt.crt();

        Pra b = Fct.a.pra();
        Prb c = Fct.a.prb();

        Obj d = new Obj.ObjBld()
                .stA(0)
                .stB(0)
                .stC(0)
                .bld();

        PrtSb e = new PrtSb();
        e.b = 3;
        PrtSb f = (PrtSb) e.cln();
        prt("" + (e.b == f.b) + ' ' + (e == f));
    }

    ////////////////////////////////////////////////////

    interface FctPrd {}

    static class FctActPrd implements FctPrd {}

    static class FctCrt {
        static FctPrd crt() { return new FctActPrd(); }
    }

    ////////////////////////////////////////////////////

    interface Pra {}
    interface Prb {}

    static class Prda implements Pra {}
    static class Prdb implements Prb {}

    interface IFct {
        Pra pra();
        Prb prb();
    }

    static class Fct implements IFct {
        static final IFct a = new Fct();

        private Fct() {}

        @Override public Pra pra() { return new Prda(); }
        @Override public Prb prb() { return new Prdb(); }
    }

    ////////////////////////////////////////////////////

    static class Obj {
        Obj(int a, int b, int c) {}

        static class ObjBld {
            private int a;
            private int b;
            private int c;

            ObjBld stA(int _a) { a = _a; return this; }
            ObjBld stB(int _b) { b = _b; return this; }
            ObjBld stC(int _c) { c = _c; return this; }

            Obj bld() { return new Obj(a, b, c); }
        }
    }

    ////////////////////////////////////////////////////

    interface IPrt {
        IPrt cln();
    }

    static class Prt implements IPrt {
        private int a = 1;

        Prt() {}
        Prt(Prt b) { a = b.a; }

        @Override public IPrt cln() { return new Prt(this); }
    }

    static class PrtSb extends Prt {
        private int b = 2;

        PrtSb() {}
        public PrtSb(PrtSb c) { super(c); b = c.b; }

        @Override public IPrt cln() { return new PrtSb(this); }
    }
}
