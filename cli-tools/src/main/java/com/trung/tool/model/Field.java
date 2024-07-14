package com.trung.tool.model;

import java.util.Objects;
import cascading.tuple.Fields;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.trung.tool.parser.FieldsParser;

public class Field implements IBaseModel {
    @JsonIgnore
    private Fields fields;
    private final String declaration;

    @JsonCreator
    public Field(String declaration) {
        Objects.requireNonNull(declaration, "field may not be null");

        this.declaration = declaration;
        this.fields = FieldsParser.INSTANCE.parseSingleFields(this.declaration, null);
    }

    public Fields fields() {
        return fields;
    }

    @Override
    public String toString() {
        return declaration;
    }
}