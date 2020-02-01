package com.github.balintrudas.commentparser.marker;

import com.github.javaparser.javadoc.Javadoc;
import lombok.Data;

import java.util.List;
import java.util.Objects;

@Data
public class CommentElement extends MarkerElement {

    private String value;
    private Javadoc javaDoc;
    private Marker parent;
    private String marker;

    public CommentElement(String value, Javadoc javaDoc) {
        this.value = value;
        this.javaDoc = javaDoc;
    }

    public CommentElement(String value, List<String> groupNames) {
        this.value = value;
        this.setGroupName(groupNames);
    }

    public CommentElement(String value, List<String> groupNames, Javadoc javaDoc) {
        this.value = value;
        this.javaDoc = javaDoc;
        this.setGroupName(groupNames);
    }

    public CommentElement(String value, List<String> groupNames, List<String> inherits, Javadoc javaDoc) {
        this.value = value;
        this.javaDoc = javaDoc;
        this.setGroupName(groupNames);
        this.setInherits(inherits);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommentElement that = (CommentElement) o;
        return Objects.equals(value, that.value) &&
                Objects.equals(marker, that.marker) &&
                Objects.equals(getPath(), that.getPath());
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, marker, getPath());
    }
}
