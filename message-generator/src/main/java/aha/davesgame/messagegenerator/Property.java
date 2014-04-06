package aha.davesgame.messagegenerator;

import javax.annotation.Nullable;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.sun.codemodel.JVar;

/**
 * Defines a property of a message bean.
 */
public class Property {
    private final String type;
    private final String name;

    @Nullable
    private JVar constrParam;

    /**
     * Constructs a new {@link Property}.
     * 
     * @param name
     *            the property name
     * @param type
     *            the property type
     */
    public Property(String name, String type) {
        this.type = Preconditions.checkNotNull(type);
        this.name = Preconditions.checkNotNull(name);
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public void setConstrParam(JVar constrParam) {
        this.constrParam = constrParam;
    }

    @Nullable
    public JVar getConstrParam() {
        return constrParam;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).add("type", type).add("name", name).toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(type, name);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Property other = (Property) obj;
        if (!name.equals(other.name)) {
            return false;
        }
        if (!type.equals(other.type)) {
            return false;
        }
        return true;
    }

}
