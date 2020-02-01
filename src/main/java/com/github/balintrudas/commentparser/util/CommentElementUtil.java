package com.github.balintrudas.commentparser.util;

import com.github.balintrudas.commentparser.marker.MarkerElement;
import com.github.balintrudas.commentparser.marker.CommentElement;
import com.github.javaparser.javadoc.JavadocBlockTag;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class CommentElementUtil {

    public static Optional<MarkerElement> getMarkerElementForGroup(String group, Set<CommentElement> commentElements) {
        return commentElements.stream().filter(commentElement -> commentElement.getParent().getGroupNames().contains(group))
                .findFirst()
                .map(commentElement -> {
                    MarkerElement markerElement = null;
                    if (commentElement.getParent().getAnnotationElements() != null && !commentElement.getParent().getAnnotationElements().isEmpty()) {
                        markerElement = commentElement.getParent().getAnnotationElements().stream()
                                .filter(annotationElement -> annotationElement.getGroupName().contains(group))
                                .findFirst().orElse(null);
                    }
                    if (markerElement == null && commentElement.getParent().getCommentElements() != null && !commentElement.getParent().getCommentElements().isEmpty()) {
                        markerElement = commentElement.getParent().getCommentElements().stream()
                                .filter(commentElement1 -> commentElement1.getGroupName().contains(group))
                                .findFirst().orElse(null);
                    }
                    return markerElement;
                });
    }

    public static List<String> getTagValues(Set<String> targetTags, List<JavadocBlockTag> commentTags) {
        return commentTags.stream().filter(
                javadocBlockTag -> targetTags.stream()
                        .anyMatch(s -> s.equals(javadocBlockTag.getTagName())))
                .filter(javadocBlockTag -> javadocBlockTag.getContent() != null)
                .flatMap(javadocBlockTag -> parseTagValue(javadocBlockTag.getContent().toText()).stream())
                .collect(Collectors.toList());
    }

    public static List<String> parseTagValue(String tagValue) {
        List<String> tagValues = Arrays.asList(tagValue.split(","));
        return tagValues.stream().map(String::trim).collect(Collectors.toList());
    }
}
