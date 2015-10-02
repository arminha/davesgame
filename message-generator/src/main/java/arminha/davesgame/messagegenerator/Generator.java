package arminha.davesgame.messagegenerator;

import com.sun.codemodel.JClassAlreadyExistsException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class Generator {

  private final MessageParser parser = new MessageParser();
  private final CodeGenerator generator;
  private final List<String> definitionFiles;

  public Generator(List<String> definitionFiles) {
    this.definitionFiles = definitionFiles;
    this.generator = new CodeGenerator();
  }

  public void generateMessages(String destSrcDir) throws IOException, ParseException,
      ClassNotFoundException, JClassAlreadyExistsException {
    for (String file : definitionFiles) {
      Iterable<Message> messages;
      try (InputStream stream = new FileInputStream(file)) {
        messages = parser.parseDefinitions(stream);
      }
      for (Message message : messages) {
        generator.addMessage(message);
      }
    }
    generator.writeSourceFiles(destSrcDir);
  }
}
