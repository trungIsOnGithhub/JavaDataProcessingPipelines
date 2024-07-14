package com.trung.tool.model;

import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public enum Compression {
    none(""),
    gzip(".gz"),
    lzo(".lzo"),
    brotli(".br"),
    lz4(".lz4"),
    lzstdzo(".zstd"),
    bzip2(".bz2"),
    snappy(".snappy");

    private final String extension;

    Compression(String extension) {
        this.extension = extension;
    }

    public static Compression find(List<URI> uris, Compression compression) {
        if (compression != null) {
            return compression;
        }

        Set<Compression> compressions = uris.stream().map(u -> find(u, null)).collect(Collectors.toSet());

        if (compressions.isEmpty()) {
            return none;
        }
        if (compressions.size() != 1) {
            throw new IllegalArgumentException("more than one compression found: " + compressions);
        }

        return compressions.stream().findFirst().get();
    }

    public static Compression find(URI uri, Compression compression) {
        if (compression != null) {
            return compression;
        }
        return find(uri.toString());
    }

    public static Compression find(String path) {
        for (Compression compression : values()) {
            if (compression == none) {
                continue;
            }

            if (path.endsWith(compression.extension())) {
                return compression;
            }
        }

        return none;
    }

    public String getFileExtension() {
        return this.fileExtension;
    }
}