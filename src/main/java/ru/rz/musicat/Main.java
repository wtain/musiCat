package ru.rz.musicat;


import org.apache.commons.cli.*;
import ru.rz.musicat.media.MCApplication;
import ru.rz.musicat.media.interfaces.FeedbackConsumer;

import java.util.HashSet;

public class Main {

    public static void main(String[] args) {

        Options options = new Options();

        Option input = new Option("i", "input", true, "input path");
        input.setRequired(true);
        options.addOption(input);

        Option ext = new Option("e", "ext", true, "extension");
        ext.setRequired(false);
        ext.setArgs(10);
        options.addOption(ext);

        Option rec = new Option("r", "recursive", false, "recursive");
        options.addOption(rec);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();

        try {
            CommandLine cmd = parser.parse(options, args);

            String inputPath = cmd.getOptionValue("input");
            boolean recursive = cmd.hasOption("recursive");

            HashSet<String> exts = new HashSet<>();
            String[] ee = cmd.getOptionValues("ext");
            for (String e: ee)
                exts.add(e);

            FeedbackConsumer consumer = new ConsoleFeedbackConsumer();
            new MCApplication(consumer, new ConsoleProgressReporter(consumer)).Run(inputPath, exts, recursive);

        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);

            System.exit(1);
        }
    }
}
