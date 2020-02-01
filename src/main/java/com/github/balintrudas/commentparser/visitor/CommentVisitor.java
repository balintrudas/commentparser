package com.github.balintrudas.commentparser.visitor;

import com.github.balintrudas.commentparser.marker.group.GroupMarkerParser;
import com.github.balintrudas.commentparser.marker.Marker;
import com.github.balintrudas.commentparser.util.NodeUtil;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.balintrudas.commentparser.Scanner;
import com.github.balintrudas.commentparser.marker.CommentMarkerParser;
import com.github.balintrudas.commentparser.marker.CommentElement;

/**
 * Only parse the non method comments
 * Get comments if configuration "getCommentsOnlyWithinMethod" is false
 */
public class CommentVisitor extends VoidVisitorAdapter<Scanner> {

    @Override
    public void visit(LineComment lineComment, Scanner arg) {
        this.processNonMethodComments(lineComment, arg);
    }

    @Override
    public void visit(BlockComment blockComment, Scanner arg) {
        this.processNonMethodComments(blockComment, arg);

    }

    @Override
    public void visit(JavadocComment javadocComment, Scanner arg) {
        this.processNonMethodComments(javadocComment, arg);

    }

    /**
     * Process all comment which are not inside environment.a method.
     * @param comment
     * @param arg
     */
    private void processNonMethodComments(Comment comment, Scanner arg) {
        if (NodeUtil.isInBoundaries(arg.getConfiguration(), comment) && !this.isMethodComment(comment) && !arg.getConfiguration().getCommentMarkerConfiguration().getIncludeOnlyWithinMethods()) {
            GroupMarkerParser groupMarkerParser = new GroupMarkerParser(arg.getConfiguration());
            Marker marker = groupMarkerParser.parse(comment);
            if (marker.isValid() || !arg.getConfiguration().getCommentMarkerConfiguration().getIncludeOnlyWithinGroup()) {
                CommentMarkerParser commentMarkerParser = new CommentMarkerParser(arg.getConfiguration(), arg.getConfiguration().getCommentMarkerConfiguration().getIncludeWithoutMarker());
                CommentElement commentElement = commentMarkerParser.parse(comment);
                if (commentElement != null) {
                    commentElement.setParent(marker);
                    commentElement.setPath(arg.getCurrentPath());
                    commentElement.setRange(comment.getRange().orElse(null));
                    commentElement.setNodeDeclaration(comment.getCommentedNode().orElse(null));
                    if (marker.getGroupNames() != null && !marker.getGroupNames().isEmpty()) {
                        marker.getGroupNames().forEach(s -> {
                            arg.getCommentStore().addComment(s, commentElement);
                        });
                    } else if (!arg.getConfiguration().getCommentMarkerConfiguration().getIncludeOnlyWithinGroup()) {
                        arg.getCommentStore().addComment(arg.getConfiguration().getGroupMarkerConfiguration().getDefaultGroupName(), commentElement);
                    }
                }
            }
        }
    }

    private Boolean isMethodComment(Comment comment) {
        if (comment.getCommentedNode().isPresent()) {
            Node targetNode = comment.getCommentedNode().get();
            return !(targetNode instanceof MethodDeclaration || targetNode instanceof FieldDeclaration || targetNode instanceof ClassOrInterfaceDeclaration);
        }
        return false;
    }

}
