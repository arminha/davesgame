package aha.davesgame.messagegenerator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.OptionHandlerFilter;

import com.sun.codemodel.JClassAlreadyExistsException;

/**
 * The main class.
 */
public class GeneratorMain {

    @Option(name = "-d", aliases = "--dest-src-dir", required = true,
            usage = "source folder for the generated Java sources", metaVar = "DIR")
    private String destSrcDir = ".";

    @Argument
    private List<String> definitionFiles = new ArrayList<>();

    public void doMain(String[] args) {
        CmdLineParser parser = new CmdLineParser(this);

        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            // if there's a problem in the command line,
            // you'll get this exception. this will report
            // an error message.
            System.err.println(e.getMessage());
            System.err.println("java GeneratorMain [options...] arguments...");
            // print the list of available options
            parser.printUsage(System.err);
            System.err.println();

            // print option sample. This is useful some time
            System.err.println(" Example: java SampleMain" + parser.printExample(OptionHandlerFilter.ALL));
            return;
        }

        try {
            new Generator(definitionFiles, destSrcDir).generateMessages();
        } catch (ClassNotFoundException | JClassAlreadyExistsException | IOException | ParseException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public static void main(String[] args) {
        new GeneratorMain().doMain(args);
    }

}
