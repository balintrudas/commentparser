package com.github.balintrudas.commentparser.marker;

import com.github.javaparser.Range;
import com.github.javaparser.ast.Node;
import lombok.Data;

import java.nio.file.Path;
import java.util.List;

/**
 * Base class for marker elements. (e.q AnnotationElement, CommentElement)
 */
@Data
public abstract class MarkerElement {

    private List<String> groupName;
    private List<String> inherits;
    private Range range;
    private Node nodeDeclaration;
    private String description;
    private Path path;
}
