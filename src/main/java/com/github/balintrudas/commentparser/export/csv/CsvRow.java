package com.github.balintrudas.commentparser.export.csv;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class CsvRow {
    private String groupName;
    private String description;
    private String comment;
    private String path;
}
