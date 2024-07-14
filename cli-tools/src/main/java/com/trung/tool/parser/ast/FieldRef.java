package com.trung.tool.parser.ast;

public interface FieldRef {

    boolean isOrdinal();

    Comparable<?> asComparable();
}