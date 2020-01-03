package ru.rz.musicat.media.utility;

import com.google.common.io.Files;
import ru.rz.musicat.media.interfaces.ProgressReporter;

import java.io.File;
import java.util.Set;

public class FSWalker {

    private ProgressReporter progressReporter;
    private FileProcessor processor;

    public FSWalker(ProgressReporter progressReporter, FileProcessor processor) {
        this.progressReporter = progressReporter;
        this.processor = processor;
    }

    public void WalkPath(String path, Set<String> exts, boolean recursive) {
        processPath(new File(path), exts, recursive);
    }

    private void processPath(File folder, Set<String> exts, boolean recursive) {

        if (recursive) {
            File[] folders = folder.listFiles(pathname -> pathname.isDirectory());
            progressReporter.pushSection(folders.length+1);
            for (File subfolder: folders) {
                progressReporter.startSection();
                processPath(subfolder, exts, true);
                progressReporter.endSection();
            }
        } else
            progressReporter.pushSection(1);

        File[] files = folder.listFiles(pathname -> {
            String ext = Files.getFileExtension(pathname.getName()).toLowerCase();
            return exts.contains(ext);
        });

        progressReporter.startSection();
        processFiles(files);
        progressReporter.endSection();
        progressReporter.popSection();
    }

    private void processFiles(File[] files) {
        int processed = 0;
        int total = files.length;
        //progressReporter.resetProgress();
        progressReporter.reportProgress(0);
        try {
            for (File file : files)
                try {
                    processor.processFile(file);
                } finally {
                    ++processed;
                    double perc = 100 * (double) processed / total;
                    progressReporter.reportProgress(perc);
                }
        }
        catch (Exception he) {
            he.printStackTrace();
        }
        progressReporter.reportProgress(100);
    }
}
