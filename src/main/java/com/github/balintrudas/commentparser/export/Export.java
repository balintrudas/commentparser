package com.github.balintrudas.commentparser.export;

import com.github.balintrudas.commentparser.CommentStore;
import com.github.balintrudas.commentparser.configuration.ExportConfiguration;
import com.github.balintrudas.commentparser.export.ascii.AsciiExport;
import com.github.balintrudas.commentparser.export.csv.CsvExport;
import com.github.balintrudas.commentparser.export.markdown.MarkdownExport;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.io.IOException;

@AllArgsConstructor
public class Export {

    @NonNull
    private CommentStore commentStore;
    @NonNull
    private ExportConfiguration exportConfiguration;

    public Export() {
        this.exportConfiguration = new ExportConfiguration();
    }

    public Export(@NonNull CommentStore commentStore) {
        this.commentStore = commentStore;
    }

    public void export() throws IOException {
        switch (this.exportConfiguration.getExportType()) {
            case CSV:
                new CsvExport(this.exportConfiguration).export(this.commentStore);
                break;
            case EXCEL:
                new CsvExport(this.exportConfiguration).export(this.commentStore);
                break;
            case MARKDOWN:
                new MarkdownExport(this.exportConfiguration).export(this.commentStore);
                break;
            case ASCIIDOC:
                new AsciiExport(this.exportConfiguration).export(this.commentStore);
                break;
        }
    }

}
