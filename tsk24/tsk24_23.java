package b;

@SpringBootTest(classes = tsk23.class)
public class tsk24_23 {
    @Autowired private ussi u;

    @Order(1) @Test public void gal() {
        var b = false;
        for (val a : u.gal())
            b |= a.em.equals("a@a.a");
        Assertions.assertTrue(b);

        Assertions.assertEquals(1, u.gal().size());
    }

    @Order(2) @Test public void dd() {
        Assertions.assertDoesNotThrow(() -> u.dd(new usr("test", "test",
            new BCryptPasswordEncoder().encode("test"), "t@t.t")));
        Assertions.assertEquals(2, u.gal().size());
        Assertions.assertEquals("test", u.gal().get(1).fn);
    }

    @Order(3) @Test public void dl() {
        var b = false;
        for (val a : u.gal())
            if (a.fn.equals("test")) {
                Assertions.assertDoesNotThrow(() -> {
                    u.dl(Objects.requireNonNull(a.id)); });
                b = true;
            }

        Assertions.assertTrue(b);
        Assertions.assertEquals(1, u.gal().size());
    }

    @Order(4) @Test public void ctn()
    { Assertions.assertTrue(u.ctn("a@a.a")); }

    ////////////////////////////////////////////////////////////////////////////////

    @Autowired private dgsi d;

    @Order(5) @Test public void _gal() {
        Assertions.assertEquals(2, d.gal().size());

        val c = new String[]{ "a", "b" };
        var b = 1;
        for (val a : d.gal()) {
            Assertions.assertEquals(b++, a.id);
            Assertions.assertEquals(c[0], a.nm);
            Assertions.assertEquals(c[1], a.bd);
        }
    }
}

