package com.trung.tool.model;

import java.util.List;
import java.util.ArrayList;
import com.trung.tool.util.Compression;
import com.trung.tool.util.Format;
import com.trung.tool.util.json.JsonSimpleView;

public class Schema implements IBaseModel {
    private String name;
    private Format format;
    private String pattern;
    private boolean embedded;

    public String getName() {
        return this.name;
    }
    public boolean isEmbedded() {
        return this.embedded;
    }
    public Format getFormat() {
        return this.format;
    }
    public String getPattern() {
        return this.pattern;
    }
    public static Builder getBuilder() {
        return new Builder();
    }


    public static final class Builder {
        private String name;
        private Format format;
        private String pattern;
        private boolean embedded;

        private Builder() {}

        public Builder withName(String name) {
            this.name = name;
            return this;
        }
        public Builder withFormat(Format format) {
            this.format = format;
            return this;
        }
        public Builder withCompression(Compression compression) {
            this.compression = compression;
            return this;
        }
        public Builder withEmbedded() {
            this.embedded = true;
            return this;
        }
        public Builder withPattern(String pattern) {
            this.pattern = pattern;
            return this;
        }

        public Schema build() {
            Schema schema = new Schema();

            schema.pattern = this.pattern;
            schema.format = this.format;
            schema.compression = this.compression;
            schema.name = this.name;
            schema.embedded = this.embedded;

            return schema;
        }
    }
}