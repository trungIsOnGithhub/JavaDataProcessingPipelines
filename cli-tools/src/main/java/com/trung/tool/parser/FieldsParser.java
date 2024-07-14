package com.trung.tool.parser;

import java.util.List;
import java.util.Optional;
import org.jparsec.Parser;
import org.jparsec.Parsers;
import org.jparsec.Scanners;
import org.jparsec.pattern.CharPredicate;
import org.jparsec.pattern.CharPredicates;

import static org.jparsec.pattern.CharPredicates.*;

import com.trung.tool.parser.ast.*;

// Set of Parser(s) rule to parse text data
public class FieldParser {
    private static final CharPredicate PARAM_EXTRA = CharPredicates.not(among("|+"));
    private static final CharPredicate FIELD_NAME_EXTRA = CharPredicates.among("~@#$%^&_");

    private static final Parser<FieldName> FIELD_NAMES = Scanners.isChar(
        or(CharPredicates.IS_ALPHA, FIELD_NAME_EXTRA)
    ).followedBy(Scanners.many(
        or(CharPredicates.IS_ALPHA_NUMERIC, FIELD_NAME_EXTRA)
    )).source().map(FieldName::new);

    private static final Parser<FieldOrdinal> FIELD_ORDINAL = Scanners.many1(
        CharPredicates.range('0','9')
    ).source().map(FieldOrdinal::new);

    private static final Parser<FieldTypeName> TYPE_NAME = Scanners.isChar(CharPredicates.IS_ALPHA)
        .followedBy(Scanners.many(CharPredicates.IS_ALPHA_NUMERIC_))
        .source().map(FieldTypeName::new);

    private static final Parser<String> TYPE_PARAM =
        Scanners.isChar(
            or(CharPredicates.IS_ALPHA, CharPredicates.isChar('-'))
        )
        .followedBy(Scanners.many(or(CharPredicates.IS_ALPHA_NUMERIC_, PARAM_EXTRA)))
        .source();

    private static final Parser<Void> FIELD_DELIM =
        Parsers.sequence(Scanners.many(IS_WHITESPACE), Scanners.isChar('+'), Scanners.many(IS_WHITESPACE));

    private static final Parser<Optional<FieldTypeParam>> typeParam =
        Parsers.sequence(
            Scanners.isChar('|'), TYPE_PARAM,
            Parsers.sequence(Scanners.isChar('|'), TYPE_PARAM).asOptional(),
            (unused, s, d) -> new FieldTypeParam(s, d)
        ).asOptional();

    private static final Parser<FieldTypeName> typeName =
        Parsers.sequence(Scanners.isChar('|'), TYPE_NAME);

    private static final Parser<FieldType> types =
        Parsers.sequence(typeName, typeParam, FieldType::new);

    public static final Parser<Field> fullFieldDeclaration =
        Parsers.sequence(Parsers.or(FIELD_NAME, FIELD_ORDINAL), types.asOptional(), Field::new);

    public static final Parser<List<Field>> fieldList =
        fullFieldDeclaration.sepBy(FIELD_DELIM);

    public static Field parseField(String field) {
        return BaseParser.parse(fullFieldDeclaration, field);
    }

    public static List<Field> parseFieldList(String field) {
        return BaseParser.parse(fieldList, field);
    }
}