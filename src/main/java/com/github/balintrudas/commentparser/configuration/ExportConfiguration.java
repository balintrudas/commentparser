package com.github.balintrudas.commentparser.configuration;

import com.github.balintrudas.commentparser.export.ExportType;
import lombok.*;

import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ExportConfiguration {

    @NonNull
    private ExportType exportType = ExportType.ASCIIDOC;
    @NonNull
    private String saveDirectory = System.getProperty("user.home");
    @NonNull
    private Boolean enableGroups = true;
    @NonNull
    private Boolean enableDescription = true;
    @NonNull
    private Boolean enablePath = true;
    @NonNull
    private String fileName = new SimpleDateFormat("'comment_export_'yyyyMMddHHmm").format(new Date());
    @NonNull
    private String commentLabel = "Comment";
    @NonNull
    private String pathLabel = "Path";
}
