package com.github.balintrudas.commentparser.export.ascii;

import com.github.balintrudas.commentparser.CommentStore;
import com.github.balintrudas.commentparser.marker.MarkerElement;
import com.github.balintrudas.commentparser.util.CommentElementUtil;
import com.github.balintrudas.commentparser.configuration.ExportConfiguration;
import com.github.balintrudas.commentparser.export.Exporter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AsciiExport extends Exporter {

    public AsciiExport(ExportConfiguration exportConfiguration) {
        super(exportConfiguration);
    }

    @Override
    public void export(CommentStore commentStore) throws IOException {
        List<AsciiRow> exportRowList = this.getExportRows(commentStore);
        StringBuilder sb = new StringBuilder();

        if (!this.getExportConfiguration().getEnableGroups()) {
            sb.append(this.getTableHeader());
        }
        for (AsciiRow exportRow : exportRowList) {
            if (this.getExportConfiguration().getEnableGroups() && exportRow.getGroupName() != null) {
                if (!exportRow.getGroupName().isEmpty()) {
                    sb.append("== ").append(exportRow.getGroupName()).append("\n\n");
                }
                if (this.getExportConfiguration().getEnableDescription() && exportRow.getDescription() != null) {
                    sb.append(exportRow.getDescription()).append("\n\n");
                }
            }
            if (this.getExportConfiguration().getEnableGroups()) {
                sb.append(this.getTableHeader());
            }
            for (AsciiRow.MarkdownComment markdownComment : exportRow.getComments()) {
                if (this.getExportConfiguration().getEnablePath()) {
                    sb.append("|").append(markdownComment.getComment()).append("\n");
                    sb.append("|").append(markdownComment.getPath()).append("\n\n");
                } else {
                    sb.append("|").append(markdownComment.getComment()).append("\n\n");
                }
            }
            if (this.getExportConfiguration().getEnableGroups()) {
                sb.append("|===").append("\n");
            }
        }
        if (!this.getExportConfiguration().getEnableGroups()) {
            sb.append("|===").append("\n");
        }

        final OutputStream os = new FileOutputStream(this.getExportConfiguration().getSaveDirectory() +
                File.separator +
                this.getExportConfiguration().getFileName() +
                ".adoc");
        try (Writer w = new OutputStreamWriter(os, "UTF-8")) {
            w.write(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getTableHeader() {
        if (this.getExportConfiguration().getEnablePath()) {
            return "|===".concat("\n").concat("|" + this.getExportConfiguration().getCommentLabel() + " |" + this.getExportConfiguration().getPathLabel()).concat("\n\n");
        } else {
            return "|===".concat("\n").concat("|" + this.getExportConfiguration().getCommentLabel()).concat("\n\n");
        }
    }

    private List<AsciiRow> getExportRows(CommentStore commentStore) {
        List<AsciiRow> exportRowList = new ArrayList<>();
        commentStore.getComments().forEach((s, commentElements) -> {
            Optional<MarkerElement> markerElement = CommentElementUtil.getMarkerElementForGroup(s, commentElements);
            exportRowList.add(AsciiRow.builder()
                    .groupName(this.getExportConfiguration().getEnableGroups() ? s : null)
                    .description(this.getExportConfiguration().getEnableDescription() ? markerElement.map(MarkerElement::getDescription).orElse(null) : null)
                    .comments(commentElements.stream()
                            .map(commentElement ->
                                    new AsciiRow.MarkdownComment(
                                            commentElement.getValue().replace("\n", "").replace("\r", ""),
                                            commentElement.getPath() != null ? commentElement.getPath().getFileName() + (commentElement.getRange() != null ? " [" + commentElement.getRange().toString() + "]" : "") : null)).collect(Collectors.toList())
                    )
                    .build());
        });
        return exportRowList;
    }

}
