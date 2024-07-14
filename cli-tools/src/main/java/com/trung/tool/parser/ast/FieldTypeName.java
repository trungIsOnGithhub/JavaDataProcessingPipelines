package com.trung.tool.parser.ast;

public class FieldTypeName {
    String name;

    public FieldTypeName(CharSequence name) {
        this.name = name.toString();
    }
    public String name() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
