package com.ppdai.raptor.codegen.java.internal;

import com.google.auto.value.AutoValue;
import com.squareup.wire.schema.Location;

import static com.squareup.wire.schema.internal.Util.appendDocumentation;

/**
 * Configures how Wire will generate code for a specific type. This configuration belongs in a
 * {@code build.wire} file that is in the same directory as the configured type.
 */
@AutoValue
public abstract class AbstractTypeConfigElement {
  public static Builder builder(Location location) {
    return new AutoValue_AbstractTypeConfigElement.Builder()
        .location(location)
        .documentation("");
  }

  public abstract Location location();
  public abstract String type();
  public abstract String documentation();
  public abstract String target();
  public abstract String adapter();

  public final String toSchema() {
    StringBuilder builder = new StringBuilder();
    appendDocumentation(builder, documentation());
    builder.append("type ").append(type()).append(" {\n");
    builder.append("  target ").append(target()).append(" using ").append(adapter()).append("\n");
    builder.append("}\n");
    return builder.toString();
  }

  @AutoValue.Builder
  public interface Builder {
    Builder location(Location location);
    Builder type(String type);
    Builder documentation(String documentation);
    Builder target(String target);
    Builder adapter(String adapter);
    AbstractTypeConfigElement build();
  }
}
