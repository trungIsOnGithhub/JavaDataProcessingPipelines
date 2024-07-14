class FieldType {
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

public class MainTest {
    public static void main(String... args) {

    }
}