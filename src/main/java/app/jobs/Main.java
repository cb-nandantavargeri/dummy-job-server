package app.jobs;

import app.jobs.db.ConnPool;
import app.jobs.queue.Queue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Main {
    public static void main(String[] args) {
        Queue.initQueue();
        Queue.sendMessage("Hello from main");
        String message = Queue.receiveMessage();
        System.out.println(message);
//        ConnPool.initConnPool();

//        try (Connection c = ConnPool.getConnection()) {
//            PreparedStatement ps = c.prepareStatement("SELECT VERSION(), DATABASE()");
//            ResultSet rs = ps.executeQuery();
//            if (rs.next()) {
//                System.out.println("MySQL: " + rs.getString(1) + " | DB: " + rs.getString(2));
//            }
//        } catch (Exception e) {
//            System.out.println("Failed to get connection");
//        }
    }

}