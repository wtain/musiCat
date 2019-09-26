package ru.rz.musicat.utility;

import java.io.File;

@FunctionalInterface
public interface FileProcessor {
    void processFile(File file) throws InterruptedException;
}
