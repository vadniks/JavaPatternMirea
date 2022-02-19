package some;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class tsk2 {

    static void prt(Object a) {
        System.out.println(a);
    }

    static record hm(
            int ag,
            String fn,
            String ln,
            LocalDate bd,
            int w
    ) {}

    public static void main(String[] args) {
        final var a = new ArrayList<hm>();
        a.add(new hm(1, "a", "a",
                LocalDate.of(1999, 2, 1), 10));
        a.add(new hm(2, "b", "b",
                LocalDate.of(1998, 1, 1), 20));
        a.add(new hm(3, "c", "c",
                LocalDate.of(1997, 1, 1), 30));
        a.add(new hm(4, "d", "d",
                LocalDate.of(1996, 1, 1), 40));
        a.add(new hm(5, "e", "e",
                LocalDate.of(1999, 1, 5), 50));

        var c = a.stream().map(b -> new hm(b.ag, b.fn, b.ln, b.bd, b.w - 5));
        c = c.filter(d -> d.bd().isBefore(LocalDate.of(1999, 1, 3)));

        var _a = c.toList();
        prt(_a);

        var d = new AtomicReference<String>();
        d.set("");
        _a.forEach(e -> d.set(d.get() + e.ln + ' '));
        prt(d.get());
    }
}
