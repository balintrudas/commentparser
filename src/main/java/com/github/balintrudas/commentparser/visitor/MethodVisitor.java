package com.github.balintrudas.commentparser.visitor;

import com.github.balintrudas.commentparser.configuration.Configuration;
import com.github.balintrudas.commentparser.util.NodeUtil;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.balintrudas.commentparser.Scanner;
import com.github.balintrudas.commentparser.marker.CommentMarkerParser;
import com.github.balintrudas.commentparser.marker.group.GroupMarkerParser;
import com.github.balintrudas.commentparser.marker.Marker;
import com.github.balintrudas.commentparser.marker.CommentElement;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MethodVisitor extends VoidVisitorAdapter<Scanner> {

    @Override
    public void visit(MethodDeclaration md, Scanner arg) {

        if (NodeUtil.isInBoundaries(arg.getConfiguration(), md)) {

            GroupMarkerParser groupMarkerParser = new GroupMarkerParser(arg.getConfiguration());
            Marker marker = groupMarkerParser.parse(md);

            if (marker.isValid() || !arg.getConfiguration().getCommentMarkerConfiguration().getIncludeOnlyWithinGroup()) {
                List<CommentElement> parsedComments = this.parseMethodComments(md.getAllContainedComments(), md, marker, arg.getConfiguration(), arg);
                if (parsedComments != null && !parsedComments.isEmpty() &&
                        marker.getGroupNames() != null && !marker.getGroupNames().isEmpty()) {
                    marker.getGroupNames().forEach(s -> {
                        arg.getCommentStore().addComment(s, parsedComments);
                    });
                } else if (parsedComments != null && !parsedComments.isEmpty() && !arg.getConfiguration().getCommentMarkerConfiguration().getIncludeOnlyWithinGroup()) {
                    arg.getCommentStore().addComment(arg.getConfiguration().getGroupMarkerConfiguration().getDefaultGroupName(), parsedComments);
                }
            }
        }
    }

    /**
     * Process all comment which are inside environment.a method.
     * @param comments
     * @param md
     * @param parent
     * @param configuration
     * @param arg
     * @return Method comments
     */
    private List<CommentElement> parseMethodComments(List<Comment> comments, MethodDeclaration md, Marker parent, Configuration configuration, Scanner arg) {
        if (comments == null || comments.isEmpty()) {
            return null;
        }
        CommentMarkerParser commentMarkerParser = new CommentMarkerParser(configuration, configuration.getCommentMarkerConfiguration().getIncludeWithoutMarker());
        return comments.stream()
                .map(commentMarkerParser::parse)
                .filter(Objects::nonNull)
                .peek(commentElement -> {
                    commentElement.setParent(parent);
                    commentElement.setNodeDeclaration(md);
                    commentElement.setPath(arg.getCurrentPath());
                })
                .collect(Collectors.toList());
    }

}
