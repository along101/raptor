package com.ppdai.raptor.codegen.java.internal;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.squareup.wire.schema.Location;
import com.squareup.wire.schema.internal.parser.Nullable;

/**
 * A single {@code .wire} file. This file is structured similarly to a {@code .proto} file, but with
 * different elements.
 *
 * <h3>File Structure</h3>
 *
 * A project may have 0 or more {@code .wire} files. These files should be in the same directory as
 * the {@code .proto} files so they may be automatically discovered by Wire.
 *
 * <p>Each file starts with a syntax declaration. The syntax must be "wire2". This is followed by an
 * optional package declaration, which should match to the package declarations of the {@code
 * .proto} files in the directory.
 *
 * <p>Profiles may import any number of proto files. Note that it is an error to import {@code
 * .wire} files. These imports are used to resolve types specified later in the file.
 *
 * <p>Profiles may specify any number of type configurations. These specify a fully qualified
 * type, its target Java type, and an adapter to do the encoding and decoding.
 *
 * <pre>   {@code
 *
 *   syntax = "wire2";
 *   package squareup.dinosaurs;
 *
 *   import "squareup/geology/period.proto";
 *
 *   // Roar!
 *   type squareup.dinosaurs.Dinosaur {
 *     target com.squareup.dino.Dinosaur using com.squareup.dino.Dinosaurs#DINO_ADAPTER;
 *   }
 * }</pre>
 */
@AutoValue
public abstract class AbstractProfileFileElement {
  public static Builder builder(Location location) {
    return new AutoValue_AbstractProfileFileElement.Builder()
        .location(location)
        .imports(ImmutableList.<String>of())
        .typeConfigs(ImmutableList.<AbstractTypeConfigElement>of());
  }

  public abstract Location location();
  @Nullable public abstract String packageName();
  public abstract ImmutableList<String> imports();
  public abstract ImmutableList<AbstractTypeConfigElement> typeConfigs();

  public final String toSchema() {
    StringBuilder builder = new StringBuilder();
    builder.append("// ").append(location()).append('\n');
    builder.append("syntax \"wire2\";\n");
    if (packageName() != null) {
      builder.append("package ").append(packageName()).append(";\n");
    }
    if (!imports().isEmpty()) {
      builder.append('\n');
      for (String file : imports()) {
        builder.append("import \"").append(file).append("\";\n");
      }
    }
    if (!typeConfigs().isEmpty()) {
      builder.append('\n');
      for (AbstractTypeConfigElement typeConfigElement : typeConfigs()) {
        builder.append(typeConfigElement.toSchema());
      }
    }
    return builder.toString();
  }

  @AutoValue.Builder
  public interface Builder {
    Builder location(Location location);
    Builder packageName(@Nullable String packageName);
    Builder imports(ImmutableList<String> imports);
    Builder typeConfigs(ImmutableList<AbstractTypeConfigElement> typeConfigs);
    AbstractProfileFileElement build();
  }
}
