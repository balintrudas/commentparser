package com.github.balintrudas.commentparser.export;

public enum ExportType {

    EXCEL("Excel (.xlsx)"), CSV("Csv (.csv)"),MARKDOWN("Markdown (.md)"), ASCIIDOC("AsciiDoc (.adoc)");

    private String name;

    ExportType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
