package com.github.balintrudas.commentparser.export;

import com.github.balintrudas.commentparser.scanner.CommentStore;
import com.github.balintrudas.commentparser.configuration.ExportConfiguration;

import java.io.IOException;

public abstract class Exporter {

    private ExportConfiguration exportConfiguration;

    public Exporter() {
        this.exportConfiguration = new ExportConfiguration();
    }

    public Exporter(ExportConfiguration exportConfiguration) {
        this.exportConfiguration = exportConfiguration;
    }

    public abstract void export(CommentStore commentStore) throws IOException;

    public ExportConfiguration getExportConfiguration() {
        return exportConfiguration;
    }

}
