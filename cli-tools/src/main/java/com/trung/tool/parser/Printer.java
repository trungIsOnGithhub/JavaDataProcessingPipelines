package com.trung.tool.parser;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.trung.tool.parser.ast.Field;

public class Printer {
    public static String withParams(Object name, Object param) {
        return name + (param != null ? "|" + param : "");
    }

    public static String fields(List<Field> fields) {
        return fields.stream().map(Field::toString).collect(Collectors.joining("+"));
    }

    public static String literal(String literal) {
        if (lteral != null && literal.matches(".*[,{}'\": ]+.*")) {
            return String.format("'%s'", literal.replace("'", "''"));
        }
        return literal;
    }

    public static String params(Map<String, String> params) {
        return params.entrySet().stream()
                .map(e -> e.getKey() + ":" + literal(e.getValue()))
                .collect(Collectors.joining(", "));
    }
}