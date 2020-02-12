package com.github.balintrudas.commentparser.export.markdown;

import com.github.balintrudas.commentparser.scanner.CommentStore;
import com.github.balintrudas.commentparser.configuration.ExportConfiguration;
import com.github.balintrudas.commentparser.export.Exporter;
import com.github.balintrudas.commentparser.marker.MarkerElement;
import com.github.balintrudas.commentparser.util.CommentElementUtil;
import net.steppschuh.markdowngenerator.table.Table;
import net.steppschuh.markdowngenerator.text.Text;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MarkdownExport extends Exporter {

    public MarkdownExport(ExportConfiguration exportConfiguration) {
        super(exportConfiguration);
    }

    @Override
    public void export(CommentStore commentStore) throws IOException {
        List<MarkdownRow> exportRowList = this.getExportRows(commentStore);
        StringBuilder sb = new StringBuilder();

        Table.Builder tableBuilder = this.getTableHeader();
        for (MarkdownRow exportRow : exportRowList) {
            if (this.getExportConfiguration().getEnableGroups() && exportRow.getGroupName() != null) {
                if (!exportRow.getGroupName().isEmpty()) {
                    sb.append("# ").append(exportRow.getGroupName()).append("  \n");
                }
                if (this.getExportConfiguration().getEnableDescription() && exportRow.getDescription() != null) {
                    sb.append(new Text(exportRow.getDescription())).append("  \n");
                    sb.append("  \n");
                }
            }
            if (this.getExportConfiguration().getEnableGroups()) {
                tableBuilder = this.getTableHeader();
            }
            for (MarkdownRow.MarkdownComment markdownComment : exportRow.getComments()) {
                if (this.getExportConfiguration().getEnablePath()) {
                    tableBuilder.addRow(markdownComment.getComment(), markdownComment.getPath());
                } else {
                    tableBuilder.addRow(markdownComment.getComment());
                }
            }
            if (this.getExportConfiguration().getEnableGroups()) {
                sb.append(tableBuilder.build()).append("  \n");
                sb.append("  \n");
            }
        }
        if (!this.getExportConfiguration().getEnableGroups()) {
            sb.append(tableBuilder.build()).append("  \n");
            sb.append("  \n");
        }

        final OutputStream os = new FileOutputStream(this.getExportConfiguration().getSaveDirectory() +
                File.separator +
                this.getExportConfiguration().getFileName() +
                ".md");
        try (Writer w = new OutputStreamWriter(os, "UTF-8")) {
            w.write(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Table.Builder getTableHeader() {
        if (this.getExportConfiguration().getEnablePath()) {
            return new Table.Builder().addRow(this.getExportConfiguration().getCommentLabel(), this.getExportConfiguration().getPathLabel());
        } else {
            return new Table.Builder().addRow(this.getExportConfiguration().getCommentLabel());
        }
    }

    private List<MarkdownRow> getExportRows(CommentStore commentStore) {
        List<MarkdownRow> exportRowList = new ArrayList<>();
        commentStore.getComments().forEach((s, commentElements) -> {
            Optional<MarkerElement> markerElement = CommentElementUtil.getMarkerElementForGroup(s, commentElements);
            exportRowList.add(MarkdownRow.builder()
                    .groupName(this.getExportConfiguration().getEnableGroups() ? s : null)
                    .description(this.getExportConfiguration().getEnableDescription() ? markerElement.map(MarkerElement::getDescription).orElse(null) : null)
                    .comments(commentElements.stream()
                            .map(commentElement ->
                                    new MarkdownRow.MarkdownComment(
                                            commentElement.getValue().replace("\n", "").replace("\r", ""),
                                            commentElement.getPath() != null ? commentElement.getPath().getFileName() + (commentElement.getRange() != null ? " [" + commentElement.getRange().toString() + "]" : "") : null)).collect(Collectors.toList())
                    )
                    .build());
        });
        return exportRowList;
    }

}
