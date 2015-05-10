package arminha.davesgame.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.primitives.Ints;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.Reflection;

@RunWith(Parameterized.class)
public class SerializableMessagesTest {

    private static final Logger LOGGER = Logger.getLogger(SerializableMessagesTest.class);

    @BeforeClass
    public static void setupClass() {
        Logger.getLogger(PodamFactoryImpl.class).setLevel(Level.WARN);
    }

    @Parameters(name = "{index} - {1}")
    public static Collection<Object[]> data() throws Exception {
        Collection<Object[]> data = new ArrayList<>();
        String msgPackage = Reflection.getPackageName(Message.class);
        ClassPath cp = ClassPath.from(Message.class.getClassLoader());
        for (ClassPath.ClassInfo ci : cp.getTopLevelClassesRecursive(msgPackage)) {
            Class<?> clazz = ci.load();
            if (Message.class.isAssignableFrom(clazz) && !clazz.isInterface()) {
                data.add(new Object[] {clazz, clazz.getSimpleName()});
            }
        }
        return data;
    }

    private Class<? extends Message> messageClass;

    public SerializableMessagesTest(Class<? extends Message> messageClass, String name) {
        this.messageClass = messageClass;
    }

    @Test
    public void canSerializeAndDeserialize() throws Exception {
        Message message = createMessage();
        String output = serialize(message);
        LOGGER.info("Serialized\n" + message + "\nas " + output);
        Message deserialized = deserialize(output);
        assertEquals(message, deserialized);
    }

    @Test
    public void equalProperties() throws Exception {
        Message message = createMessage();
        assertTrue(message.equals(message));
        assertFalse(message.equals(null));
        assertFalse(message.equals(new Object()));
        assertFalse(message.equals(createMessage()));
    }

    private Message createMessage() throws Exception {
        @SuppressWarnings("unchecked")
        Constructor<? extends Message> constructor = (Constructor<? extends Message>) messageClass.getConstructors()[0];
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        Object[] parameters = new Object[parameterTypes.length];
        PodamFactory factory = new PodamFactoryImpl();
        for (int i = 0; i < parameters.length; i++) {
            if (parameterTypes[i] == UUID.class) {
                parameters[i] = UUID.randomUUID();
            } else if (parameterTypes[i] == List.class) {
                parameters[i] = Ints.asList(1, 2, 3);
            } else {
                parameters[i] = factory.manufacturePojo(parameterTypes[i]);
            }
        }
        return constructor.newInstance(parameters);
    }

    private Message deserialize(String output) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(output, messageClass);
    }

    private String serialize(Message message) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        mapper.writeValue(out, message);
        return out.toString("utf-8");
    }

}
