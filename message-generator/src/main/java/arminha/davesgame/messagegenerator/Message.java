package arminha.davesgame.messagegenerator;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

import java.util.List;

import javax.annotation.Nullable;

/**
 * Defines a message bean.
 */
public class Message {

  private final String name;
  private final List<Property> properties;
  private final String iface;
  private final String pkg;

  /**
   * Construct a new {@link Message}.
   * 
   * @param iface
   *          the marker interface for the message bean
   * @param pkg
   *          the Java package for the message bean
   * @param name
   *          the name for the message bean
   * @param properties
   *          the properties of the message bean
   */
  public Message(String iface, String pkg, String name, List<Property> properties) {
    this.iface = Preconditions.checkNotNull(iface);
    this.pkg = Preconditions.checkNotNull(pkg);
    this.name = Preconditions.checkNotNull(name);
    this.properties = Preconditions.checkNotNull(properties);
  }

  public String getName() {
    return name;
  }

  public List<Property> getProperties() {
    return properties;
  }

  public String getInterface() {
    return iface;
  }

  public String getPackage() {
    return pkg;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("name", name).add("interface", iface)
        .add("package", pkg).add("properties", properties).toString();
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(name, iface, pkg, properties);
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
    Message other = (Message) obj;
    if (!iface.equals(other.iface)) {
      return false;
    }
    if (!name.equals(other.name)) {
      return false;
    }
    if (!pkg.equals(other.pkg)) {
      return false;
    }
    if (!properties.equals(other.properties)) {
      return false;
    }
    return true;
  }

}
