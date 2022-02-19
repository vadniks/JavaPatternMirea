package some;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class tsk4 {
    final static Lock _lk = new ReentrantLock();

    static void prt(Object a) {
        _lk.lock();
        System.out.println(a);
        _lk.unlock();
    }

    public static void main(String[] args) {
        var es = new exsr(3);

        var a = es.submit(() -> {
            try { Thread.sleep(4000); }
            catch (InterruptedException ignored) {}
            prt('a');
        });
        var b = es.submit(() -> {
            try { Thread.sleep(3000); }
            catch (InterruptedException ignored) {}
            prt('b');
        });
        var c = es.submit(() -> {
            try { Thread.sleep(2000); }
            catch (InterruptedException ignored) {}
            prt('c');
        });

        do {
            prt("" + a.isDone() + ' ' + b.isDone() + ' ' + c.isDone());
            try { Thread.sleep(250); }
            catch (InterruptedException ignored) {}
        } while (!a.isDone() || !b.isDone() || !c.isDone());

        es.sht(false);
    }

    static class exsr implements ExecutorService {
        private final int ln;
        private volatile boolean sht = false;
        private volatile boolean trm = false;
        private final ArrayList<Future<?>> tks = new ArrayList<>();
        private final Lock lk = new ReentrantLock();
        private final ArrayList<AtomicBoolean> ens = new ArrayList<>();
        private volatile boolean end = false;
        private final ArrayList<Thread> ths = new ArrayList<>();

        exsr(final int ln) { this.ln = ln; }

        private void sht(boolean a) {
            lk.lock();
            sht = true;
            for (var b : tks)
                b.cancel(a);
            trm = a;
            lk.unlock();
        }

        @Override
        public void shutdown() { sht(false); }

        @Override
        public List<Runnable> shutdownNow() {
            sht(true);
            return new ArrayList<>(0);
        }

        @Override
        public boolean isShutdown() { return sht; }

        @Override
        public boolean isTerminated() { return trm; }

        @Override
        public boolean awaitTermination(long timeout, TimeUnit unit)
        throws InterruptedException {
            if (end) throw new IllegalStateException();
            while (!end) Thread.onSpinWait();
            return false;
        }

        private void chk() {
            lk.lock();
            var a = true;
            for (var b : ens)
                a &= b.get();
            end = a;
            lk.unlock();
        }

        @Override
        public <T> Future<T> submit(Callable<T> a) {
            lk.lock();
            if (tks.size() == ln || sht) throw new IllegalStateException();

            final var bl = new AtomicBoolean(false);
            ens.add(bl);
            var c = new FutureTask<T>(() -> {
                var d = a.call();
                bl.set(true);
                chk();
                return d;
            });
            tks.add(c);

            var d = new Thread(c::run);
            ths.add(d);
            d.start();

            lk.unlock();
            return c;
        }

        @Override
        public <T> Future<T> submit(Runnable a, T b) {
            lk.lock();
            if (tks.size() == ln || sht) throw new IllegalStateException();

            final var bl = new AtomicBoolean(false);
            ens.add(bl);
            var c = new FutureTask<T>(() -> {
                a.run();
                bl.set(true);
                chk();
            }, b);
            tks.add(c);

            var d = new Thread(c::run);
            ths.add(d);
            d.start();

            lk.unlock();
            return c;
        }

        @Override
        public Future<?> submit(Runnable a) { return submit(a, null); }

        @SuppressWarnings("unchecked")
        @Override
        public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> a) {
            lk.lock();
            for (var i : a) {
                var j = new FutureTask<T>(i);
                tks.add(j);
                j.run();
            }
            var b = tks.clone();
            lk.unlock();
            return (List<Future<T>>) b;
        }

        @Override
        public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> a, long b, TimeUnit c)
        { return invokeAll(a); }

        @Override
        public <T> T invokeAny(Collection<? extends Callable<T>> tasks)
        { throw new UnsupportedOperationException(); }

        @Override
        public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
        { throw new UnsupportedOperationException(); }

        @Override
        public void execute(Runnable a) { submit(a); }
    }
}
