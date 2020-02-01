package com.github.balintrudas.commentparser.export.csv;

import com.coreoz.windmill.Windmill;
import com.coreoz.windmill.exports.config.ExportHeaderMapping;
import com.coreoz.windmill.exports.config.ExportRowsConfig;
import com.coreoz.windmill.exports.exporters.csv.ExportCsvConfig;
import com.github.balintrudas.commentparser.CommentStore;
import com.github.balintrudas.commentparser.configuration.ExportConfiguration;
import com.github.balintrudas.commentparser.export.ExportType;
import com.github.balintrudas.commentparser.export.Exporter;
import com.github.balintrudas.commentparser.marker.MarkerElement;
import com.github.balintrudas.commentparser.util.CommentElementUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CsvExport extends Exporter {

    public CsvExport() {
        super();
    }

    public CsvExport(ExportConfiguration exportConfiguration) {
        super(exportConfiguration);
    }

    @Override
    public void export(CommentStore commentStore) throws FileNotFoundException {

        List<CsvRow> csvRowList = this.getExportRows(commentStore);

        ExportHeaderMapping<CsvRow> exportHeaderMapping = new ExportHeaderMapping<>();
        if (this.getExportConfiguration().getEnableGroups()) {
            exportHeaderMapping.add("Group name", CsvRow::getGroupName);
            if (this.getExportConfiguration().getEnableDescription()) {
                exportHeaderMapping.add("Description", CsvRow::getDescription);
            }
        }
        exportHeaderMapping.add(this.getExportConfiguration().getCommentLabel(), CsvRow::getComment);
        if(this.getExportConfiguration().getEnablePath()){
            exportHeaderMapping.add(this.getExportConfiguration().getPathLabel(), CsvRow::getPath);
        }

        ExportRowsConfig<CsvRow> csvRowExportRowsConfig = Windmill
                .export(csvRowList)
                .withHeaderMapping(exportHeaderMapping);

        if (this.getExportConfiguration().getExportType().equals(ExportType.CSV)) {
            csvRowExportRowsConfig.asCsv(ExportCsvConfig.builder().separator(';').escapeChar('"').build())
                    .writeTo(new FileOutputStream(this.getExportConfiguration().getSaveDirectory() +
                            File.separator +
                            this.getExportConfiguration().getFileName() +
                            ".csv"));
        } else {
            csvRowExportRowsConfig.asExcel()
                    .writeTo(new FileOutputStream(this.getExportConfiguration().getSaveDirectory() +
                            File.separator +
                            this.getExportConfiguration().getFileName() +
                            ".xlsx"));
        }

    }

    private List<CsvRow> getExportRows(CommentStore commentStore) {
        List<CsvRow> exportRowList = new ArrayList<>();
        commentStore.getComments().forEach((s, commentElements) -> {
            if (this.getExportConfiguration().getEnableGroups()) {
                Optional<MarkerElement> markerElement = CommentElementUtil.getMarkerElementForGroup(s, commentElements);
                exportRowList.add(CsvRow.builder()
                        .groupName(s)
                        .description(markerElement.map(MarkerElement::getDescription).orElse(null))
                        .build());
            }
            commentElements.forEach(commentElement -> {
                exportRowList.add(CsvRow.builder().comment(commentElement.getValue().replace("\n", "").replace("\r", ""))
                        .path(commentElement.getPath() != null ? commentElement.getPath().getFileName() + (commentElement.getRange() != null ? " [" + commentElement.getRange().toString() + "]" : "") : null)
                        .build());
            });
        });
        return exportRowList;
    }
}
