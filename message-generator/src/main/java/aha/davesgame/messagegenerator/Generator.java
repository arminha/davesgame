package aha.davesgame.messagegenerator;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.sun.codemodel.JClassAlreadyExistsException;

public class Generator {

    private final MessageParser parser = new MessageParser();
    private final List<String> definitionFiles;
    private final String destSrcDir;

    public Generator(List<String> definitionFiles, String destSrcDir) {
        this.definitionFiles = definitionFiles;
        this.destSrcDir = destSrcDir;
    }

    public void generateMessages() throws IOException, ParseException, ClassNotFoundException,
            JClassAlreadyExistsException {
        for (String file : definitionFiles) {
            Iterable<Message> messages;
            try (InputStream stream = new FileInputStream(file)) {
                messages = parser.parseDefinitions(stream);
            }
            for (Message message : messages) {
                CodeGenerator generator = new CodeGenerator(message);
                generator.generateJavaSource(destSrcDir);
            }
        }
    }
}
