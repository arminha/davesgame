package aha.davesgame.messagegenerator;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nullable;

import org.yaml.snakeyaml.Yaml;

import com.google.common.collect.ImmutableSet;

/**
 * Parses {@link Message} definitions in a YAML format.
 * <p>
 * The format is a follows:
 * <pre>
 * # The package for the messages in this file
 * package: com.example.messages
 * # A marker interface that all messages in this file implement
 * interface: com.example.messages.Message
 * 
 * # A message definition
 * ExampleMessage:
 *   property1: int
 *   property2: String
 * 
 * AnotherMessage:
 *   property3: UUID
 * [...]
 * </pre>
 */
public class MessageParser {

    private static final String PACKAGE = "package";
    private static final String INTERFACE = "interface";
    private static final Set<String> RESERVED_KEYS = ImmutableSet.of(PACKAGE, INTERFACE);

    private final Map<String, String> typeMap = new HashMap<String, String>();

    /**
     * Constructs a new {@link MessageParser}.
     */
    public MessageParser() {
        addToTypeMap(UUID.class);
    }

    private String extendType(String type) {
        if (type.indexOf('.') == -1) {
            // search in type map
            String extendedType = typeMap.get(type);
            if (extendedType != null) {
                return extendedType;
            }
        }
        return type;
    }

    private void addToTypeMap(Class<?> clazz) {
        typeMap.put(clazz.getSimpleName(), clazz.getName());
    }

    /**
     * Read and parse message definitions from an {@link InputStream}. The stream is not closed.
     * 
     * @param stream
     * @return a list of {@link Message}s
     * @throws ParseException
     *             if a parse error occurs
     */
    public Iterable<Message> parseDefinitions(InputStream stream) throws ParseException {
        Yaml yaml = new Yaml();
        Object object = yaml.load(stream);
        return parseDefinitions(object);
    }

    /**
     * Parse message definitions given as a {@linkplain String}.
     * 
     * @param definitions
     * @return a list of {@link Message}s
     * @throws ParseException
     *             if a parse error occurs
     */
    public Iterable<Message> parseDefinitions(String definitions) throws ParseException {
        Yaml yaml = new Yaml();
        Object object = yaml.load(definitions);
        return parseDefinitions(object);
    }

    /**
     * Parse message definitions given as a deserialized Object from {@link Yaml}.
     * 
     * @param yamlObject
     * @return a list of {@link Message}s
     * @throws ParseException
     *             if a parse error occurs
     */
    protected Iterable<Message> parseDefinitions(@Nullable Object yamlObject) throws ParseException {
        List<Message> messages = new ArrayList<>();
        if (yamlObject != null) {
            Map<String, ?> definitions = checkIsMap(yamlObject);
            String iface = readInterface(definitions);
            String pkg = readPackage(definitions);
            for (Entry<String, ?> definition : definitions.entrySet()) {
                if (!RESERVED_KEYS.contains(definition.getKey())) {
                    messages.add(createMessage(iface, pkg, definition));
                }
            }
        } else {
            throw new ParseException("empty definition");
        }
        return messages;
    }

    @SuppressWarnings("unchecked")
    private Map<String, ?> checkIsMap(@Nullable Object object) throws ParseException {
        if (!(object instanceof Map)) {
            throw new ParseException("Failed to parse message definition from [" + object + "]");
        }
        return (Map<String, ?>) object;
    }

    private String readInterface(Map<String, ?> definitions) throws ParseException {
        Object iface = definitions.get(INTERFACE);
        if (iface == null) {
            throw new ParseException("Missing interface definition");
        }
        if (iface instanceof String && !((String) iface).isEmpty()) {
            return (String) iface;
        } else {
            throw new ParseException("Failed to parse interface definition");
        }
    }

    private String readPackage(Map<String, ?> definitions) throws ParseException {
        Object pkg = definitions.get(PACKAGE);
        if (pkg == null) {
            throw new ParseException("Missing package definition");
        }
        if (pkg instanceof String && !((String) pkg).isEmpty()) {
            return (String) pkg;
        } else {
            throw new ParseException("Failed to parse package definition");
        }
    }

    private Message createMessage(String iface, String pkg, Entry<String, ?> definition) throws ParseException {
        String name = definition.getKey();
        Map<String, ?> propertyDefinitions = checkIsMap(definition.getValue());

        List<Property> properties = new ArrayList<>();
        for (Entry<String, ?> propertyDefinition : propertyDefinitions.entrySet()) {
            String propName = propertyDefinition.getKey();
            String propType = (String) propertyDefinition.getValue();
            properties.add(new Property(propName, extendType(propType)));
        }
        if (properties.isEmpty()) {
            throw new ParseException(name + " has no properties.");
        }
        return new Message(iface, pkg, name, properties);
    }
}
