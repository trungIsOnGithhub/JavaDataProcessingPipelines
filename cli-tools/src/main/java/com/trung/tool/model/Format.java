package com.trung.tool.model;

import java.util.Set;
import java.util.List;
import java.util.LinkedHashSet;

public enum Format {
    text("txt"),
    csv("csv"),
    tsv("tsv"),
    json(text, "json"),
    regex(text, "log"),
    parquet(true, "parquet");

    private final Format parent;
    private boolean alwayEmbedded;
    private final Set<String> fileExtensions;

    Format(String... extensions) {
        this.parent = this;
        this.alwayEmbedded = false;
        this.fileExtensions.addAll(List.of(extensions));
    }
    Format(Format parent, String... extensions) {
        this.parent = parent;
        this.alwayEmbedded = false;
        this.fileExtensions.addAll(List.of(extensions));
    }
    Format(boolean alwayEmbedded, String... extensions) {
        this.parent = this;
        this.alwayEmbedded = alwayEmbedded;
        this.fileExtensions.addAll(List.of(extensions));
    }
    Format(Format parent, boolean alwayEmbedded, String... extensions) {
        this.parent = parent;
        this.alwayEmbedded = alwayEmbedded;
        this.fileExtensions.addAll(List.of(extensions));
    }

    public Format getParent() {
        return this.parent;
    }
    public boolean isAlwayEmbedded() {
        return this.alwaysEmbedded;
    }
    public String getFileExtensions() {
        return this.fileExtensions;
    }
}