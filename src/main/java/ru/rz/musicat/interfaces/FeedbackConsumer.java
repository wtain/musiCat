package ru.rz.musicat.interfaces;

@FunctionalInterface
public interface FeedbackConsumer {
    void print(String msg);
}
