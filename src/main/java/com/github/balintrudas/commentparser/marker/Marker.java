package com.github.balintrudas.commentparser.marker;

import com.github.balintrudas.commentparser.marker.group.annotation.AnnotationElement;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Contains all parsed group annotations and group comments.
 */
@Getter
public class Marker {

    private List<AnnotationElement> annotationElements;
    private List<CommentElement> commentElements;

    public Marker(List<AnnotationElement> annotationElements, List<CommentElement> commentElements) {
        this.annotationElements = annotationElements;
        this.commentElements = commentElements;
    }

    public Boolean isValid() {
        return (annotationElements != null && !annotationElements.isEmpty())
                || (commentElements != null && !commentElements.isEmpty());
    }

    /**
     * Get all group names for markers
     * @return All group names
     */
    public List<String> getGroupNames() {
        Set<String> groupNames = new HashSet<>();
        if (this.annotationElements != null) {
            this.annotationElements.forEach(annotationElement -> groupNames.addAll(annotationElement.getGroupName()));
        }
        if (this.commentElements != null) {
            this.commentElements.forEach(commentElement -> groupNames.addAll(commentElement.getGroupName()));
        }
        return new ArrayList<>(groupNames);
    }

    /**
     * Get all inherits for markers
     * @return All inherits
     */
    public List<String> getInherits() {
        Set<String> inherits = new HashSet<>();
        if (this.annotationElements != null) {
            this.annotationElements.forEach(annotationElement -> {
                if (annotationElement.getInherits() != null && !annotationElement.getInherits().isEmpty()) {
                    inherits.addAll(annotationElement.getInherits());
                }
            });
        }
        if (this.commentElements != null) {
            this.commentElements.forEach(commentElement -> {
                if (commentElement.getInherits() != null && !commentElement.getInherits().isEmpty()) {
                    inherits.addAll(commentElement.getInherits());
                }
            });
        }
        return new ArrayList<>(inherits);
    }

}

