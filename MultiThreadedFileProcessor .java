import java.io.*;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MultiThreadedFileProcessor {
    private static final int NUM_THREADS = 4;
    private static int totalWordCount = 0;
    private static final Lock lock = new ReentrantLock(); // for synchronization

    public static void main(String[] args) throws IOException, InterruptedException {
        File file = new File("largefile.txt"); // Replace with your file path
        List<String> lines = new ArrayList<>();

        // Step 1: Read the file line-by-line
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }

        // Step 2: Divide work among threads
        int chunkSize = lines.size() / NUM_THREADS;
        Thread[] threads = new Thread[NUM_THREADS];

        System.out.println("Creating and starting threads...");

        for (int i = 0; i < NUM_THREADS; i++) {
            int start = i * chunkSize;
            int end = (i == NUM_THREADS - 1) ? lines.size() : (i + 1) * chunkSize;

            List<String> chunk = lines.subList(start, end);
            WordCountTask task = new WordCountTask(chunk);
            threads[i] = new Thread(task, "Worker-" + i);
            threads[i].start(); // thread is now running
        }

        // Step 3: Wait for all threads to complete
        for (Thread thread : threads) {
            thread.join(); // wait for termination
        }

        System.out.println("All threads completed.");
        System.out.println("Total word count: " + totalWordCount);
    }

    // Runnable Task Class
    static class WordCountTask implements Runnable {
        private final List<String> lines;

        public WordCountTask(List<String> lines) {
            this.lines = lines;
        }

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + " started.");
            int localCount = 0;

            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    localCount += line.trim().split("\\s+").length;
                }
            }

            // Synchronize access to shared totalWordCount
            lock.lock();
            try {
                totalWordCount += localCount;
                System.out.println(Thread.currentThread().getName() + " counted " + localCount + " words.");
            } finally {
                lock.unlock();
            }

            System.out.println(Thread.currentThread().getName() + " finished.");
        }
    }
}
