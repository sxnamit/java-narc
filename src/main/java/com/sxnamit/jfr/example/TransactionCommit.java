package com.sxnamit.jfr.example;

import java.util.Random;

import com.sxnamit.jfr.event.TransactionCommitEvent;

public class TransactionCommit implements Runnable {

    private static final Random random = new Random();

    @Override
    public void run() {
        sessionCommit();
        transactionCommit();

    }

    public static void main(String[] args) {
        Runnable r = new TransactionCommit();
        for (int i = 0; i < 10; i++) {
            new Thread(r).start();
        }
    }

    public void sessionCommit() {
        TransactionCommitEvent event = new TransactionCommitEvent();
        event.begin();
        event.message = "Session commit";

        // Generate a random sleep time between 0 and 2000 milliseconds (2 seconds)
        int sleepTime = random.nextInt(2000);

        System.out.println("Sleeping for " + sleepTime + " milliseconds.");
        // Following try-catch signifies a session commit
        // that persists an entity into the database while keeping
        // the entity in app layer.
        try {
            // Sleep for the generated random time
            Thread.sleep(sleepTime);
        }
        catch (InterruptedException e) {
            // Handle interrupted exception
            System.err.println("Thread was interrupted during sleep.");
            Thread.currentThread().interrupt(); // Restore the interrupt status
        }
        event.end();
        event.commit();
    }

    public void transactionCommit() {
        TransactionCommitEvent event = new TransactionCommitEvent();
        event.begin();
        event.message = "Transaction commit";
        // Generate a random sleep time between 0 and 2000 milliseconds (2 seconds)
        int sleepTime = random.nextInt(2000);

        System.out.println("Sleeping for " + sleepTime + " milliseconds.");
        // Following try-catch signifies a transaction commit
        // that persists an entity into the database and flushes
        // the entity from the app layer.
        try {
            // Sleep for the generated random time
            Thread.sleep(sleepTime);
        }
        catch (InterruptedException e) {
            // Handle interrupted exception
            System.err.println("Thread was interrupted during sleep.");
            Thread.currentThread().interrupt(); // Restore the interrupt status
        }
        event.end();
        event.commit();
    }

}
