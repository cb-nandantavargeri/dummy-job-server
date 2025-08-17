package app.jobs.jobs;

import java.util.*;

public class WordCountJob implements Job {
    @Override
    public void execute() throws Exception {
        String text = ("lorem ipsum dolor sit amet amet amet consectetur adipiscing elit "
                + "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua");
        Map<String, Integer> freq = new HashMap<>();
        for (String w : text.split("\\s+")) {
            freq.merge(w.toLowerCase(), 1, Integer::sum);
        }
        System.out.println("WordCountJob: unique=" + freq.size() + ", 'amet'=" + freq.getOrDefault("amet", 0));
    }
}
