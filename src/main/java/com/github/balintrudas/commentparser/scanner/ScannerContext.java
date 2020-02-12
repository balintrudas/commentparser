package com.github.balintrudas.commentparser.scanner;

import com.github.balintrudas.commentparser.configuration.Configuration;
import com.github.balintrudas.commentparser.scanner.CommentStore;
import lombok.*;

import java.nio.file.Path;

@RequiredArgsConstructor
@Getter
@Setter
public class ScannerContext {
    @NonNull
    private CommentStore commentStore;
    @NonNull
    private Configuration configuration;
    private Path currentPath;
}
