package app.jobs.fw;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPool {

    public static ThreadPoolExecutor threadPool;

    public static void initThreadPool() {
        threadPool = new ThreadPoolExecutor(
                5,
                5,
                1, TimeUnit.MINUTES,
                new SynchronousQueue<>()
        );
        threadPool.allowCoreThreadTimeOut(true);
    }
}
