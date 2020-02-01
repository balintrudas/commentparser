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
    private String defaultGroupName = "";
    private String defaultGroupInherit = null;
    @NonNull
    private String defaultGroupNameKey = "value";
    @NonNull
    private String defaultGroupInheritKey = "inherit";

    @NonNull
    private Set<String> annotations = new HashSet<>(Arrays.asList("CommentGroup", "CmntGroup"));
    @NonNull
    private Set<String> tags = new HashSet<>(Arrays.asList("Group", "group", "CommentGroup", "commentgroup", "CmntGroup", "cmntGroup", "cmntgroup"));
    @NonNull
    private Set<String> inheritTags = new HashSet<>(Arrays.asList("Inherit", "inherit", "InheritGroup", "inheritgroup", "inheritGroup"));

    public static class GroupMarkerConfigurationBuilder {
        public GroupMarkerConfigurationBuilder setTags(String... tags) {
            this.tags = new HashSet<>(Arrays.asList(tags));
            return this;
        }

        public GroupMarkerConfigurationBuilder addTag(String tag) {
            this.tags.add(tag);
            return this;
        }

        public GroupMarkerConfigurationBuilder addTag(String... tags) {
            this.tags.addAll(Arrays.asList(tags));
            return this;
        }

        public GroupMarkerConfigurationBuilder setInheritTags(String... tags) {
            this.inheritTags = new HashSet<>(Arrays.asList(tags));
            return this;
        }

        public GroupMarkerConfigurationBuilder addInheritTag(String tag) {
            this.inheritTags.add(tag);
            return this;
        }

        public GroupMarkerConfigurationBuilder addInheritTag(String... tags) {
            this.inheritTags.addAll(Arrays.asList(tags));
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
