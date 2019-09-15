package ru.rz.musicat;

import ru.rz.musicat.interfaces.FeedbackConsumer;
import ru.rz.musicat.interfaces.ProgressReporter;

public class ConsoleProgressReporter implements ProgressReporter {

    FeedbackConsumer consumer;
    double prevPerc = -1;

    public ConsoleProgressReporter(FeedbackConsumer consumer){
        this.consumer = consumer;
    }

    @Override
    public void resetProgress() {
        prevPerc = -1;
    }

    @Override
    public void reportProgress(double perc) {
        if (perc - prevPerc >= 1.0) {
            consumer.print(String.format("Processing %.2f %% complete", perc));
            prevPerc = perc;
        }
    }
}
