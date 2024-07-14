import com.trung.tool.parser.Printer;

import java.util.Optional;

public class FieldTypeParam {
    String param1;
    String param2;

    public FieldTypeParam(String param1, Optional<String> param2) {
        this.param1 = param1;
        this.param2 = param2.orElse(null);
    }

    public String param1() {
        return param1;
    }
    public String param2() {
        return param2;
    }

    @Override
    public String toString() {
        return Printer.withParams(param1, param2);
    }
}