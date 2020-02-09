package com.github.balintrudas.commentparser.configuration;

import lombok.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class GroupMarkerConfiguration {

    @NonNull
    private Set<String> annotations = new HashSet<>(Arrays.asList("CommentGroup", "CmntGroup"));

    @NonNull
    private String defaultGroupName = "";
    private String defaultGroupInherit = null;
    @NonNull
    private String annotationGroupNameKey = "value";
    @NonNull
    private String annotationGroupInheritKey = "inherit";

    @NonNull
    private Set<String> commentGroupTags = new HashSet<>(Arrays.asList("Group", "group", "CommentGroup", "commentgroup", "CmntGroup", "cmntGroup", "cmntgroup"));
    @NonNull
    private Set<String> commentGroupInheritTags = new HashSet<>(Arrays.asList("Inherit", "inherit", "InheritGroup", "inheritgroup", "inheritGroup"));

    public static class GroupMarkerConfigurationBuilder {
        public GroupMarkerConfigurationBuilder setCommentGroupTags(String... tags) {
            this.commentGroupTags = new HashSet<>(Arrays.asList(tags));
            return this;
        }

        public GroupMarkerConfigurationBuilder addCommentGroupTag(String tag) {
            this.commentGroupTags.add(tag);
            return this;
        }

        public GroupMarkerConfigurationBuilder addCommentGroupTag(String... tags) {
            this.commentGroupTags.addAll(Arrays.asList(tags));
            return this;
        }

        public GroupMarkerConfigurationBuilder setCommentGroupInheritTags(String... tags) {
            this.commentGroupInheritTags = new HashSet<>(Arrays.asList(tags));
            return this;
        }

        public GroupMarkerConfigurationBuilder addCommentGroupInheritTag(String tag) {
            this.commentGroupInheritTags.add(tag);
            return this;
        }

        public GroupMarkerConfigurationBuilder addCommentGroupInheritTag(String... tags) {
            this.commentGroupInheritTags.addAll(Arrays.asList(tags));
            return this;
        }

        public GroupMarkerConfigurationBuilder setAnnotations(Class... annotations) {
            this.annotations = new HashSet<>(Arrays.asList(annotations).stream().map(Class::getSimpleName).collect(Collectors.toList()));
            return this;
        }

        public GroupMarkerConfigurationBuilder addAnnotation(Class annotationClass) {
            this.annotations.add(annotationClass.getSimpleName());
            return this;
        }

        public GroupMarkerConfigurationBuilder setAnnotations(String... annotations) {
            this.annotations = new HashSet<>(Arrays.asList(annotations));
            return this;
        }

        public GroupMarkerConfigurationBuilder addAnnotation(String annotationClass) {
            this.annotations.add(annotationClass);
            return this;
        }
    }
}
