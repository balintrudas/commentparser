package com.github.balintrudas.commentparser.marker;

import com.github.balintrudas.commentparser.configuration.Configuration;
import com.github.balintrudas.commentparser.util.CommentElementUtil;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.javadoc.Javadoc;

import java.util.*;

public class CommentMarkerParser {

    private Configuration configuration;
    private Boolean includeAll = false;

    public CommentMarkerParser(Configuration configuration, Boolean includeAll) {
        this.configuration = configuration;
        this.includeAll = includeAll;
    }

    public CommentMarkerParser(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * Parse three type of comment (inline comment, block comment, javadoc comment)
     * Search for comment markers with the given contains, start with and regex configuration.
     *
     * @param comment
     * @return Parsed comment
     */
    public CommentElement parse(Comment comment) {
        if (this.isExcluded(comment)) {
            return null;
        }

        CommentElement commentElement;
        Javadoc javaDoc = null;
        String commentText;

        if (comment.isJavadocComment()) {
            javaDoc = ((JavadocComment) comment).parse();
            commentElement = new CommentElement(
                    javaDoc.getDescription().toText(),
                    Collections.emptyList(),
                    javaDoc);
            commentText = javaDoc.getDescription().toText();
        } else {
            commentElement = new CommentElement(comment.getContent(), Collections.emptyList());
            commentText = comment.getContent();
        }

        commentElement.setRange(comment.getRange().orElse(null));

        if (this.configuration.getCommentMarkerConfiguration().getEnableTags() && comment.isJavadocComment()) {
            List<String> tagValues = CommentElementUtil.getTagValues(this.configuration.getCommentMarkerConfiguration().getTags(), javaDoc.getBlockTags());
            if (!tagValues.isEmpty()) {
                commentElement.setMarker(null);
                return commentElement;
            }
        }

        if (this.configuration.getCommentMarkerConfiguration().getEnableContains()) {
            Optional<String> containMarker = this.configuration.getCommentMarkerConfiguration().getContains().stream().filter(commentText::contains).findFirst();
            if (containMarker.isPresent()) {
                String preparedComment = this.prepareCommentElement(this.configuration.getCommentMarkerConfiguration().getContains(), commentText, false);
                commentElement.setValue(preparedComment);
                commentElement.setMarker(containMarker.get());
                return commentElement;
            }
        }

        if (this.configuration.getCommentMarkerConfiguration().getEnableStartWith()) {
            Optional<String> startWithMarker = this.configuration.getCommentMarkerConfiguration().getStartWith().stream().filter(commentText::startsWith).findFirst();
            if (startWithMarker.isPresent()) {
                String preparedComment = this.prepareCommentElement(this.configuration.getCommentMarkerConfiguration().getStartWith(), commentText, true);
                commentElement.setValue(preparedComment);
                commentElement.setMarker(startWithMarker.get());
                return commentElement;
            }
        }

        if (this.configuration.getCommentMarkerConfiguration().getRegex() != null) {
            Boolean isMatchWithMarker = commentText.matches(this.configuration.getCommentMarkerConfiguration().getRegex());
            if (isMatchWithMarker) {
                commentElement.setValue(commentText.trim());
                commentElement.setMarker(this.configuration.getCommentMarkerConfiguration().getRegex());
            }
        }

        return this.includeAll ? commentElement : null;
    }

    /**
     * Check exclude markers in the comment.
     *
     * @param comment
     * @return Boolean
     */
    private Boolean isExcluded(Comment comment) {
        Optional<String> containMarker = this.configuration.getCommentMarkerConfiguration().getExcludeContains().stream().filter(comment.getContent()::contains).findFirst();
        if (containMarker.isPresent()) {
            return true;
        }
        Optional<String> startWithMarker = this.configuration.getCommentMarkerConfiguration().getExcludeStartWith().stream().filter(comment.getContent()::startsWith).findFirst();
        if (startWithMarker.isPresent()) {
            return true;
        }
        if (this.configuration.getCommentMarkerConfiguration().getExcludeRegex() != null) {
            return comment.getContent().matches(this.configuration.getCommentMarkerConfiguration().getExcludeRegex());
        }
        return false;
    }

    /**
     * Remove markers from the final comment text.
     *
     * @param targets
     * @param comment
     * @param onlyStart
     * @return Prepared comment text
     */
    private String prepareCommentElement(Set<String> targets, String comment, Boolean onlyStart) {
        if (comment == null) {
            comment = "";
        }
        if (this.configuration.getCommentMarkerConfiguration().getRemoveMarkers()) {
            for (String target : targets) {
                if (onlyStart) {
                    if (comment.indexOf(target) == 0) {
                        comment = comment.replace(target, "");
                    }
                } else {
                    comment = comment.replace(target, "");
                }
            }
        }
        return comment.trim();
    }

}
