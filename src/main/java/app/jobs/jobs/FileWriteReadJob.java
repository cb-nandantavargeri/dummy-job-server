package app.jobs.jobs;

import java.io.*;
import java.nio.file.*;
import java.util.UUID;

public class FileWriteReadJob implements Job {
    @Override
    public void execute() throws Exception {
        Path temp = Files.createTempFile("job-", ".txt");
        String line = "FileWriteReadJob " + UUID.randomUUID();
        Files.write(temp, (line + System.lineSeparator()).getBytes("UTF-8"));
        try (BufferedReader br = Files.newBufferedReader(temp)) {
            String first = br.readLine();
            System.out.println("FileWriteReadJob: first line = " + first);
        } finally {
            Files.deleteIfExists(temp);
        }
    }
}
