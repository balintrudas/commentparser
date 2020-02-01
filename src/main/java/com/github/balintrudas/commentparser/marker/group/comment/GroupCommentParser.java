package com.github.balintrudas.commentparser.marker.group.comment;

import com.github.balintrudas.commentparser.configuration.Configuration;
import com.github.balintrudas.commentparser.marker.CommentElement;
import com.github.balintrudas.commentparser.util.CommentElementUtil;
import com.github.balintrudas.commentparser.util.NodeUtil;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.comments.Comment;
import com.github.balintrudas.commentparser.marker.group.GroupMarkerElementParser;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.javadoc.Javadoc;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Used for collect all comment based group marker.
 */
public class GroupCommentParser implements GroupMarkerElementParser<CommentElement> {

    private Configuration configuration;

    public GroupCommentParser(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public List<CommentElement> parse(Node node) {
        Optional<Comment> comment = NodeUtil.getComment(node);
        if (comment.isPresent()) {
            if (comment.get() instanceof JavadocComment) {
                Javadoc javaDoc = ((JavadocComment) comment.get()).parse();

                List<String> tagValues = CommentElementUtil.getTagValues(this.configuration.getGroupMarkerConfiguration().getTags(), javaDoc.getBlockTags());
                List<String> inheritTagValues = CommentElementUtil.getTagValues(this.configuration.getGroupMarkerConfiguration().getInheritTags(), javaDoc.getBlockTags());

                if (!tagValues.isEmpty()) {
                    CommentElement commentElement = new CommentElement(javaDoc.getDescription().toText(),
                            !tagValues.isEmpty() ? tagValues : Collections.singletonList(this.configuration.getGroupMarkerConfiguration().getDefaultGroupName()),
                            !inheritTagValues.isEmpty() ? inheritTagValues : Stream.of(this.configuration.getGroupMarkerConfiguration().getDefaultGroupInherit())
                                    .filter(Objects::nonNull).collect(Collectors.toList()),
                            javaDoc);
                    commentElement.setMarker(null);
                    commentElement.setDescription(commentElement.getValue());
                    return Collections.singletonList(commentElement);
                }
            }
        }
        return Collections.emptyList();
    }

}
