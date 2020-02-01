package com.github.balintrudas.commentparser.marker.group;

import com.github.balintrudas.commentparser.configuration.Configuration;
import com.github.balintrudas.commentparser.marker.Marker;
import com.github.javaparser.ast.Node;
import com.github.balintrudas.commentparser.marker.group.annotation.AnnotationElement;
import com.github.balintrudas.commentparser.marker.group.annotation.GroupAnnotationParser;
import com.github.balintrudas.commentparser.marker.CommentElement;
import com.github.balintrudas.commentparser.marker.group.comment.GroupCommentParser;

import java.util.List;

public class GroupMarkerParser {

    private GroupAnnotationParser annotationParser;
    private GroupCommentParser commentParser;

    public GroupMarkerParser(Configuration configuration) {
        this.annotationParser = new GroupAnnotationParser(configuration);
        this.commentParser = new GroupCommentParser(configuration);
    }

    public Marker parse(Node node) {
        List<AnnotationElement> annotationElements = this.annotationParser.parse(node);
        List<CommentElement> commentElements = this.commentParser.parse(node);
        return new Marker(annotationElements, commentElements);
    }

}
