package com.ppdai.raptor.codegen.java;

import com.google.common.collect.ImmutableList;
import com.ppdai.raptor.codegen.java.internal.AbstractProfileFileElement;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;
import com.ppdai.raptor.codegen.java.internal.AbstractTypeConfigElement;
import com.squareup.wire.schema.ProtoType;

/**
 * Describes how to map {@code .proto} to {@code .java}. A single repository of {@code .proto} files
 * may have multiple profiles; for example a project may target both Android and Java.
 */
public final class Profile {
  private final ImmutableList<AbstractProfileFileElement> profileFiles;

  Profile(ImmutableList<AbstractProfileFileElement> profileFiles) {
    this.profileFiles = profileFiles;
  }

  public Profile() {
    this(ImmutableList.<AbstractProfileFileElement>of());
  }

  public TypeName getTarget(ProtoType type) {
    AbstractTypeConfigElement typeConfig = typeConfig(type);
    return typeConfig != null ? ClassName.bestGuess(typeConfig.target()) : null;
  }

  public AdapterConstant getAdapter(ProtoType type) {
    AbstractTypeConfigElement typeConfig = typeConfig(type);
    return typeConfig != null ? new AdapterConstant(typeConfig.adapter()) : null;
  }

  /** Returns the config for {@code type}, or null if it is not configured. */
  private AbstractTypeConfigElement typeConfig(ProtoType type) {
    for (AbstractProfileFileElement element : profileFiles) {
      for (AbstractTypeConfigElement typeConfig : element.typeConfigs()) {
        if (typeConfig.type().equals(type.toString())) {
          return typeConfig;
        }
      }
    }
    return null;
  }
}
