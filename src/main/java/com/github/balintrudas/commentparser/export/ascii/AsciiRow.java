package com.github.balintrudas.commentparser.export.ascii;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Data
@Builder
public class AsciiRow {
    private String groupName;
    private String description;
    private List<MarkdownComment> comments;

    @Getter
    @AllArgsConstructor
    public static class MarkdownComment{
        private String comment;
        private String path;
    }
}