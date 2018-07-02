package com.ppdai.raptor.codegen.java;

import com.squareup.javapoet.ClassName;
import com.squareup.wire.ProtoAdapter;

/**
 * A constant field that identifies a {@link ProtoAdapter}. This should be a string like like {@code
 * com.squareup.dinosaurs.Dinosaur#ADAPTER} with a fully qualified class name, a {@code #}, and a
 * field name.
 */
public final class AdapterConstant {
  public final ClassName className;
  public final String memberName;

  public AdapterConstant(ClassName className, String memberName) {
    this.className = className;
    this.memberName = memberName;
  }

  public AdapterConstant(String adapter) {
    String[] names = adapter.split("#");
    if (names.length != 2) {
      throw new IllegalArgumentException("Illegally formatted adapter: " + adapter + ".");
    }
    this.className = ClassName.bestGuess(names[0]);
    this.memberName = names[1];
  }

  @Override public boolean equals(Object o) {
    return o instanceof AdapterConstant
        && ((AdapterConstant) o).className.equals(className)
        && ((AdapterConstant) o).memberName.equals(memberName);
  }

  @Override public int hashCode() {
    return className.hashCode() * 37 + memberName.hashCode();
  }
}
