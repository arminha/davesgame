package arminha.davesgame.domain;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import arminha.davesgame.domain.event.Event;

import com.google.common.base.Throwables;

/**
 * Base class for aggregate roots.
 */
public abstract class AggregateRoot {

    /**
     * NULL value for an UUID.
     */
    protected static final UUID NULL_ID = UUID.fromString("00000000-0000-0000-0000-000000000000");

    private final List<Event> changes = new ArrayList<Event>();
    private int version;

    /**
     * The ID of this aggregate. Uniquely identifies it across bounded contexts.
     */
    public abstract UUID getId();

    public final int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public final void markChangesAsCommited() {
        changes.clear();
    }

    public final Iterable<Event> getUncommitedChanges() {
        return Collections.unmodifiableCollection(changes);
    }

    public final void loadsFromHistory(Iterable<Event> history) {
        for (Event event : history) {
            applyChange(event, false);
        }
    }

    protected final void applyChange(Event event) {
        applyChange(event, true);
    }

    private void applyChange(Event event, boolean isNew) {
        try {
            Class<?> thisClass = getClass();
            Method method = thisClass.getDeclaredMethod("apply", event.getClass());
            method.setAccessible(true);
            method.invoke(this, event);
            if (isNew) {
                changes.add(event);
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | SecurityException t) {
            throw Throwables.propagate(t);
        }
    }
}
