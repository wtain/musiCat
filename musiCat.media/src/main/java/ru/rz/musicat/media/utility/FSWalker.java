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
            for (File subfolder: folders)
                processPath(subfolder, exts, true);
        }

        File[] files = folder.listFiles(pathname -> {
            String ext = Files.getFileExtension(pathname.getName()).toLowerCase();
            return exts.contains(ext);
        });

        processFiles(files);
    }

    private void processFiles(File[] files) {
        int processed = 0;
        int total = files.length;
        progressReporter.resetProgress();
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
    }
}
