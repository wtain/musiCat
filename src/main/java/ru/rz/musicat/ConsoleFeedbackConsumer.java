package ru.rz.musicat;

import ru.rz.musicat.media.interfaces.FeedbackConsumer;

public class ConsoleFeedbackConsumer implements FeedbackConsumer {

    @Override
    public void print(String msg) {
        System.out.println(msg);
    }
}
