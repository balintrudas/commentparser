package com.github.balintrudas.commentparser.marker.group.annotation;

import com.github.balintrudas.commentparser.marker.MarkerElement;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class AnnotationElement extends MarkerElement {

    private String annotationName;

    public AnnotationElement(String annotationName) {
        this.annotationName = annotationName;
    }

    public AnnotationElement(String annotationName, List<String> groupNames) {
        this.annotationName = annotationName;
        this.setGroupName(groupNames);
    }

    public AnnotationElement(String annotationName, List<String> groupNames, List<String> inherits) {
        this.annotationName = annotationName;
        this.setGroupName(groupNames);
        this.setInherits(inherits);
    }

    public AnnotationElement(String annotationName, List<String> groupNames, String description) {
        this.annotationName = annotationName;
        this.setGroupName(groupNames);
        this.setDescription(description);
    }

}
