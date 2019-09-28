package ru.rz.musicat.media.utility;

import org.hibernate.exception.LockAcquisitionException;
import ru.rz.musicat.media.interfaces.FeedbackConsumer;

public class DatabaseHelper {
    public static void TryDataChange(FeedbackConsumer consumer, Runnable operation, int attempts, int delay) throws InterruptedException {
        Boolean success = false;
        int attempt = 0;
        while (!success) {
            try {
                ++attempt;
                operation.run();
                success = true;
            } catch (LockAcquisitionException e) {
                e.printStackTrace();
                consumer.print("Database is locked");
                if (attempt == attempts)
                    throw e;
                consumer.print("Retrying..");
                Thread.sleep(delay);
            }
        }
    }
}
