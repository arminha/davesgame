package aha.davesgame.messagegenerator;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.sun.codemodel.JClassAlreadyExistsException;

public class Generator {

    private final MessageParser parser = new MessageParser();
    private final CodeGenerator generator;
    private final List<String> definitionFiles;

    public Generator(List<String> definitionFiles, String destSrcDir) {
        this.definitionFiles = definitionFiles;
        this.generator = new CodeGenerator(destSrcDir);
    }

    public void generateMessages() throws IOException, ParseException, ClassNotFoundException,
            JClassAlreadyExistsException {
        for (String file : definitionFiles) {
            Iterable<Message> messages;
            try (InputStream stream = new FileInputStream(file)) {
                messages = parser.parseDefinitions(stream);
            }
            for (Message message : messages) {
                generator.addMessage(message);
            }
        }
        generator.writeSourceFiles();
    }
}
