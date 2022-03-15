package some;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;

public class tsk1 {

    static void prt(Object a) {
        System.out.println(a);
    }

    public static void main(String[] args) {
        t1();
    }
/*
    static void t1() {
        prt(((Function<Integer[], String>) a -> {
            final var b = a.length;
            var c = "";

            var d = a.clone();
            Arrays.sort(d);

            for (int i = 0; i < b; i++)
                c += d[i];

            if (c.charAt(0) == '0') {
                var e = c.getBytes(StandardCharsets.UTF_8);
                var f = e[0];
                e[0] = e[1];
                e[1] = f;
                return new String(e);
            }

            return c;
        }).apply(new Integer[]{5, 1, 0, 1}));
    }

    static void t2() {
        class st {
            final String nm;
            final String gp;
            st(String n, String g)
            { nm = n; gp = g; }
        }

        st[] aa = {
                new st("a", "aa"),
                new st("b", "aa"),
                new st("c", "bb"),
                new st("d", "cc"),
                new st("e", "bb"),
        };

        var e = ((Function<st[], Map<String, List<st>>>) a -> {
            var b = new HashMap<String, List<st>>();

            for (var c : a) {
                if (b.containsKey(c.gp))
                    b.get(c.gp).add(c);
                else {
                    var d = new ArrayList<st>();
                    d.add(c);
                    b.put(c.gp, d);
                }
            }

            return b;
        }).apply(null);
    }
 */

    static class st {
        final String a;
        final int b;
        st(String c, int d) { a = c; b = d; }

        @Override
        public String toString() { return "(" + a + ' ' + b + ')'; }
    }

    static void t1() {
        var a = new ArrayList<st>();
        a.add(new st("c", 5));
        a.add(new st("b", 1));
        a.add(new st("a", 0));
        a.add(new st("d", 6));

        Comparator<st> b = (st c, st d) -> Integer.compare(c.b, d.b);
        Collections.sort(a, b);
        prt(a);
    }
}
