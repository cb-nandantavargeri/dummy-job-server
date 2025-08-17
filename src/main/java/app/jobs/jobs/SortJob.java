package app.jobs.jobs;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class SortJob implements Job {
    @Override
    public void execute() throws Exception {
        int size = ThreadLocalRandom.current().nextInt(20, 60);
        List<Integer> data = new ArrayList<>(size);
        for (int i = 0; i < size; i++) data.add(ThreadLocalRandom.current().nextInt(0, 1000));
        Collections.sort(data);
        System.out.println("SortJob: size=" + size + ", min=" + data.get(0) + ", max=" + data.get(size - 1));
    }
}
