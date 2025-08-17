package app.jobs.jobs;

import java.security.MessageDigest;
import java.util.UUID;

public class HashJob implements Job {
    @Override
    public void execute() throws Exception {
        String s = UUID.randomUUID().toString();
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] digest = md.digest(s.getBytes("UTF-8"));
        System.out.println("HashJob: SHA-256 of " + s + " = " + toHex(digest));
    }

    private String toHex(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (byte x : b) sb.append(String.format("%02x", x));
        return sb.toString();
    }
}
