package some;

import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class tsk3 {

    static void prt(Object a) {
        System.out.println(a);
    }

    public static void main(String[] args) {
        var s = new set<Integer>();
        var a = new Thread(() -> {
            for (var i = 0; i < 10; i++)
                s.dd(i, false);
        });
        var b = new Thread(() -> {
            for (var i = 0; i < 10; i++)
                s.dd(i, true);
        });
        var ab = new Thread(() -> {
            for (var i = 0; i < 20; i++)
                prt("# " + i + " | " + s.gt(i));
        });
//        a.start();
//        b.start();
//        ab.start();

        var m = new map<Integer, Integer>();
        var c = new Thread(() -> {
            for (var i = 0; i < 10; i++)
                try { m.pt(i, i, false); }
                catch (InterruptedException ignored) {}
        });
        var d = new Thread(() -> {
            for (var i = 0; i < 10; i++)
                try { m.pt(i * 10, i * 10, true); }
                catch (InterruptedException ignored) {}
        });
        c.start();
        d.start();
    }

    static class set<T> implements Set<T> {
        private volatile Object[] ar = new Object[0];
        private final Lock lk = new ReentrantLock();

        @Override
        public boolean add(T t) {
            dd(t, false);
            return true;
        }

        @Override
        public boolean remove(Object o) {
            lk.lock();
            var a = new Object[ar.length - 1];
            var c = 0;

            for (var b : a)
                if (!b.equals(o))
                    a[c++] = b;

            ar = a;

            lk.unlock();
            return true;
        }

        @Override
        public boolean containsAll(Collection<?> b) {
            var c = true;
            var d = 0;
            lk.lock();
            for (var a : b)
                c &= ar[d++].equals(a);
            lk.unlock();
            return c;
        }

        @Override
        public boolean addAll(Collection<? extends T> b) {
            lk.lock();
            var c = 0;
            for (var a : b)
                add(a);
            lk.unlock();
            return false;
        }

        @Override
        public boolean retainAll(Collection<?> collection)
        { throw new UnsupportedOperationException(); }

        @Override
        public boolean removeAll(Collection<?> collection)
        { throw new UnsupportedOperationException(); }

        @Override
        public void clear() {
            lk.lock();
            ar = new Object[0];
            lk.unlock();
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean equals(Object a) {
            lk.lock();
            if (!(a instanceof set)) {
                lk.unlock();
                return false;
            }
            var b = Arrays.equals(ar, ((set<Object>) a).ar);
            lk.unlock();
            return b;
        }

        void dd(final T a, final boolean c) {
            lk.lock();
            var b = new Object[ar.length + 1];
            System.arraycopy(ar, 0, b, 0, ar.length);
            ar = b;
            ar[ar.length - 1] = a;
            prt((c ? "_ " : "  ") + a + " | " + ar.length);
            lk.unlock();
        }

        @SuppressWarnings("unchecked")
        T gt(final int a) {
            lk.lock();
            if (a < 0 || a >= ar.length) return null;
            var b = ar[a];
            lk.unlock();
            return (T) b;
        }

        @Override
        public int size() {
            lk.lock();
            var a = ar.length;
            lk.unlock();
            return a;
        }

        @Override
        public boolean isEmpty() {
            lk.lock();
            var a = size() > 0;
            lk.unlock();
            return a;
        }

        @Override
        public boolean contains(Object o) {
            lk.lock();
            for (var a : ar)
                if (a.equals(o)) {
                    lk.unlock();
                    return true;
                }
            lk.unlock();
            return false;
        }

        @SuppressWarnings("unchecked")
        @Override
        public Iterator<T> iterator() {
            lk.lock();
            var a = Arrays.stream(ar).iterator();
            lk.unlock();
            return (Iterator<T>) a;
        }

        @Override
        public Object[] toArray() {
            lk.lock();
            var a = ar;
            lk.unlock();
            return a;
        }

        @Override
        public <T1> T1[] toArray(T1[] t1s)
        { throw new UnsupportedOperationException(); }
    }

    static class map<K, V> implements Map<K, V> {
        private volatile Object[] ar = new Object[0];
        private final Semaphore sm = new Semaphore(1);

        private void ac() {
            try { sm.acquire(); }
            catch (Exception ignored) {}
        }

        @Override
        public int size() {
            ac();
            var a = ar.length;
            sm.release();
            return a;
        }

        @Override
        public boolean isEmpty() {
            ac();
            var a = ar.length == 0;
            sm.release();
            return a;
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean containsKey(Object o) {
            ac();
            for (var a : ar)
                if (((pr<K, V>) a).k.equals(o)) {
                    sm.release();
                    return true;
                }
            sm.release();
            return false;
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean containsValue(Object o) {
            ac();
            for (var a : ar)
                if (((pr<K, V>) a).v.equals(o)) {
                    sm.release();
                    return true;
                }
            sm.release();
            return false;
        }

        @SuppressWarnings("unchecked")
        @Override
        public V get(Object o) {
            try { return gt((K) o); }
            catch (InterruptedException e)
            { throw new RuntimeException(); }
        }

        @Override
        public V put(K k, V v) {
            try { pt(k, v, false); }
            catch (InterruptedException e)
            { throw new RuntimeException(); }
            return v;
        }

        @Override
        public V remove(Object o) {
            ac();
            var a = new Object[ar.length - 1];
            var c = 0;

            for (var b : a)
                if (!b.equals(o))
                    a[c++] = b;

            ar = a;

            sm.release();
            return null;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void putAll(Map<? extends K, ? extends V> b) {
            ac();
            for (var i = 0; i < b.size(); i++) {
                var k = b.keySet().toArray()[i];
                put((K) k, b.get(k));
            }
            sm.release();
        }

        @Override
        public void clear() {
            ac();
            ar = new Object[0];
            sm.release();
        }

        @SuppressWarnings("unchecked")
        @Override
        public Set<K> keySet() {
            ac();
            var a = new set<K>();
            for (Object o : ar)
                a.add(((pr<K, V>) o).k);
            sm.release();
            return a;
        }

        @SuppressWarnings("unchecked")
        @Override
        public Collection<V> values() {
            ac();
            var a = new set<V>();
            for (Object o : ar)
                a.add(((pr<K, V>) o).v);
            sm.release();
            return a;
        }

        @Override
        public Set<Entry<K, V>> entrySet()
        { throw new UnsupportedOperationException(); }

        void pt(final K a, final V b, final boolean d) throws InterruptedException {
            sm.acquire();
            var c = new Object[ar.length + 1];
            System.arraycopy(ar, 0, c, 0, ar.length);
            ar = c;
            ar[ar.length - 1] = new pr<K, V>(a, b);
            prt((d ? "_" : " ") + a + ' ' + b + ' ' + ar.length);
            sm.release();
        }

        @SuppressWarnings("unchecked")
        V gt(final K a) throws InterruptedException {
            sm.acquire();
            for (var i : ar) {
                var j = (pr<K, V>) i;
                if (j.k.equals(a)) {
                    var _a = j.v;
                    sm.release();
                    return _a;
                }
            }
            sm.release();
            return null;
        }

        private static record pr<K, V>(K k, V v) {}
    }
}
