package com.ppdai.raptor.codegen.java.internal;

import com.google.common.collect.ImmutableList;
import com.squareup.wire.schema.Location;
import com.squareup.wire.schema.internal.parser.SyntaxReader;

/** Parses {@code .wire} files. */
public final class ProfileParser {
  private final SyntaxReader reader;

  private final AbstractProfileFileElement.Builder fileBuilder;
  private final ImmutableList.Builder<String> imports = ImmutableList.builder();
  private final ImmutableList.Builder<AbstractTypeConfigElement> typeConfigs = ImmutableList.builder();

  /** Output package name, or null if none yet encountered. */
  private String packageName;

  public ProfileParser(Location location, String data) {
    this.reader = new SyntaxReader(data.toCharArray(), location);
    this.fileBuilder = AbstractProfileFileElement.builder(location);
  }

  public AbstractProfileFileElement read() {
    String label = reader.readWord();
    if (!"syntax".equals(label)) {
        throw reader.unexpected("expected 'syntax'");
    }
    reader.require('=');
    String syntaxString = reader.readQuotedString();
    if (!"wire2".equals(syntaxString)) {
        throw reader.unexpected("expected 'wire2'");
    }
    reader.require(';');

    while (true) {
      String documentation = reader.readDocumentation();
      if (reader.exhausted()) {
        return fileBuilder.packageName(packageName)
            .imports(imports.build())
            .typeConfigs(typeConfigs.build())
            .build();
      }

      readDeclaration(documentation);
    }
  }

  private void readDeclaration(String documentation) {
    Location location = reader.location();
    String label = reader.readWord();

    if ("package".equals(label)) {
      if (packageName != null) {
          throw reader.unexpected(location, "too many package names");
      }
      packageName = reader.readName();
      reader.require(';');
    } else if ("import".equals(label)) {
      String importString = reader.readString();
      imports.add(importString);
      reader.require(';');
    } else if ("type".equals(label)) {
      typeConfigs.add(readTypeConfig(location, documentation));
    } else {
      throw reader.unexpected(location, "unexpected label: " + label);
    }
  }

  /** Reads a type config and returns it. */
  private AbstractTypeConfigElement readTypeConfig(Location location, String documentation) {
    String name = reader.readDataType();
    String target = null;
    String adapter = null;

    reader.require('{');
    while (!reader.peekChar('}')) {
      Location wordLocation = reader.location();
      String word = reader.readWord();
      switch (word) {
        case "target":
          if (target != null) {
              throw reader.unexpected(wordLocation, "too many targets");
          }
          target = reader.readWord();
          if (!"using".equals(reader.readWord())) {
              throw reader.unexpected("expected 'using'");
          }
          String adapterType = reader.readWord();
          reader.require('#');
          String adapterConstant = reader.readWord();
          reader.require(';');
          adapter = adapterType + '#' + adapterConstant;
          break;

        default:
          throw reader.unexpected(wordLocation, "unexpected label: " + word);
      }
    }

    return AbstractTypeConfigElement.builder(location)
        .type(name)
        .documentation(documentation)
        .target(target)
        .adapter(adapter)
        .build();
  }
}
