package ru.rz.musicat;

import ru.rz.musicat.interfaces.FeedbackConsumer;

public class ConsoleFeedbackConsumer implements FeedbackConsumer {

    @Override
    public void print(String msg) {
        System.out.println(msg);
    }
}
