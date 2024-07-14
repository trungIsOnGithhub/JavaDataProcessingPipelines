package com.trung.tool.parser.ast;

import com.trung.tool.parser.Printer;
import java.util.Optional;

public class FieldType {
    FieldTypeName name;
    FieldTypeParam param;

    public FieldType(FieldTypeName name, Optional<FieldTypeParam> param) {
        this.name = name;
        this.param = param.orElse(null);
    }

    public FieldTypeName getName() {
        return name;
    }
    public Optional<FieldTypeParam> getParam() {
        return Optional.ofNullable(param);
    }

    @Override
    public String toString() {
        return Printer.withParams(name, param);
    }
}