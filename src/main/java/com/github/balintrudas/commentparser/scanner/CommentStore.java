package com.github.balintrudas.commentparser.scanner;

import com.github.balintrudas.commentparser.marker.CommentElement;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

@Getter
public class CommentStore {

    //Store comments in groups
    private Map<String, LinkedHashSet<CommentElement>> comments = new LinkedHashMap<>();

    public void addComment(String key, CommentElement comment) {
        if (this.comments.containsKey(key)) {
            this.comments.get(key).add(comment);
        } else {
            this.comments.put(key, new LinkedHashSet<>(Arrays.asList(comment)));
        }
    }

    public void addComment(String key, List<CommentElement> comments) {
        if (this.comments.containsKey(key)) {
            this.comments.get(key).addAll(comments);
        } else {
            this.comments.put(key, new LinkedHashSet<>(comments));
        }
    }

    /**
     * Collect all inherited comments and merge with the exist comment collection.
     * Avoid infinite loop when two block inherits each other.
     */
    public void processInheritance() {
        this.getComments().forEach((s, commentElements) -> {
            Set<CommentElement> inheritedComments = this.getAllInheritedCommentElement(commentElements, new HashSet<>(Arrays.asList(s)));
            this.getComments().get(s).addAll(inheritedComments);
        });
    }

    /**
     * Collect all inherited comments for the given comment element recursively.
     * Avoid infinite loop when two block inherits each other.
     *
     * @param commentElements
     * @return All inherited comments
     */
    private Set<CommentElement> getAllInheritedCommentElement(Set<CommentElement> commentElements, Set<String> infiniteLoopControl) {
        Set<CommentElement> inheritedCommentsResult = new LinkedHashSet<>();
        if (commentElements != null && !commentElements.isEmpty()) {
            Optional<CommentElement> commentElement = commentElements.stream().filter(commentItem ->
                    commentItem.getParent() != null
                            && commentItem.getParent().getInherits() != null
                            && !commentItem.getParent().getInherits().isEmpty()
            ).findFirst();
            commentElement.ifPresent(commentElement1 -> commentElement1.getParent().getInherits().forEach(inheritItem -> {
                if (this.getComments().containsKey(inheritItem) && !infiniteLoopControl.contains(inheritItem)) {
                    //Add group name for control to avoid infinite loops
                    infiniteLoopControl.add(inheritItem);
                    Set<CommentElement> inheritedComments = this.getComments().get(inheritItem);
                    Set<CommentElement> multiLevelinheritedComments = this.getAllInheritedCommentElement(inheritedComments, infiniteLoopControl);
                    inheritedCommentsResult.addAll(inheritedComments);
                    inheritedCommentsResult.addAll(multiLevelinheritedComments);
                }
            }));
        }
        return inheritedCommentsResult;
    }

    public void sort() {
        comments = comments.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        Comparator<CommentElement> commentCompare = Comparator
                .comparing((CommentElement o1) -> o1.getPath().getFileName())
                .thenComparing((CommentElement o1) -> o1.getRange().begin.line);

        comments.entrySet().forEach(stringLinkedHashSetEntry -> {
            comments.put(stringLinkedHashSetEntry.getKey(), stringLinkedHashSetEntry.getValue().stream().sorted(commentCompare)
                    .collect(Collectors.toCollection(LinkedHashSet::new)));
        });

    }

}
