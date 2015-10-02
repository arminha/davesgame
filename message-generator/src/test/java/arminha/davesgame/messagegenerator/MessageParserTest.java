package arminha.davesgame.messagegenerator;

import static org.hamcrest.collection.IsEmptyIterable.emptyIterable;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.util.Arrays;

public class MessageParserTest {

  private MessageParser parser;

  @Before
  public void setup() {
    parser = new MessageParser();
  }

  @Test(expected = ParseException.class)
  public void failParseEmptyDefinition() throws Exception {
    parser.parseDefinitions("");
  }

  @Test(expected = ParseException.class)
  public void failMissingInterface() throws Exception {
    parser.parseDefinitions("package: com.example.target");
  }

  @Test(expected = ParseException.class)
  public void failEmptyInterface() throws Exception {
    parser.parseDefinitions("interface: ''\npackage: com.example.target");
  }

  @Test(expected = ParseException.class)
  public void failInvalidInterfaceType() throws Exception {
    parser.parseDefinitions("interface: 1\npackage: com.example.target");
  }

  @Test(expected = ParseException.class)
  public void failMissingPackage() throws Exception {
    parser.parseDefinitions("interface: com.example.Message");
  }

  @Test(expected = ParseException.class)
  public void failEmptyPackage() throws Exception {
    parser.parseDefinitions("interface: com.example.Message\npackage: ''");
  }

  @Test(expected = ParseException.class)
  public void failInvalidPackageType() throws Exception {
    parser.parseDefinitions("interface: com.example.Message\npackage: 2");
  }

  @Test
  public void canParseInterfaceAndPackage() throws Exception {
    String definitions = "interface: com.example.Message\npackage: com.example.target";
    Iterable<Message> messages = parser.parseDefinitions(definitions);
    assertThat(messages, emptyIterable());
  }

  @Test(expected = ParseException.class)
  public void failInvalidInterface() throws Exception {
    parser.parseDefinitions("interface:  \npackage: com.example.target");
  }

  @Test(expected = ParseException.class)
  public void failInvalidPackage() throws Exception {
    parser.parseDefinitions("interface: com.example.Message\npackage: ");
  }

  @Test
  public void canParseSimpleMessage() throws Exception {
    String definitions = "interface: com.example.Message\n" + "package: com.example.target\n\n"
        + "SimpleMessage:\n" + "  id: int";
    Iterable<Message> messages = parser.parseDefinitions(definitions);
    Message expected = new Message("com.example.Message", "com.example.target", "SimpleMessage",
        Arrays.asList(new Property("id", "int")));
    assertThat(messages, containsInAnyOrder(expected));
  }

  @Test
  public void canParseSimpleMessageStream() throws Exception {
    String definitions = "interface: com.example.Message\n" + "package: com.example.target\n\n"
        + "SimpleMessage:\n" + "  id: int";
    Iterable<Message> messages = parser.parseDefinitions(new ByteArrayInputStream(definitions
        .getBytes("utf-8")));
    Message expected = new Message("com.example.Message", "com.example.target", "SimpleMessage",
        Arrays.asList(new Property("id", "int")));
    assertThat(messages, containsInAnyOrder(expected));
  }

  @Test
  public void canParseComplexMessage() throws Exception {
    String definitions = "interface: com.example.Message\n" + "package: com.example.target\n\n"
        + "SimpleMessage:\n" + "  id: int\n" + "  name: String\n\n" + "OtherMessage:\n"
        + "  id: UUID\n" + "  param1: long\n" + "  param2: com.example.Type";
    Iterable<Message> messages = parser.parseDefinitions(definitions);
    Message simpleMsg = new Message("com.example.Message", "com.example.target", "SimpleMessage",
        Arrays.asList(new Property("id", "int"), new Property("name", "String")));
    Message otherMsg = new Message("com.example.Message", "com.example.target", "OtherMessage",
        Arrays.asList(new Property("id", "java.util.UUID"), new Property("param1", "long"),
            new Property("param2", "com.example.Type")));
    assertThat(messages, containsInAnyOrder(simpleMsg, otherMsg));
  }

}
