package com.github.balintrudas.commentparser.configuration;

import lombok.*;

import java.util.*;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class CommentMarkerConfiguration {

    @NonNull
    private Boolean includeWithoutMarker = false;
    @NonNull
    private Boolean includeOnlyWithinMethods = true;
    @NonNull
    private Boolean includeOnlyWithinGroup = true;

    @NonNull
    private Set<String> contains = new HashSet<>(Arrays.asList("@cmnt", "@Cmnt", "@comment", "@Comment"));
    @NonNull
    private Set<String> tags = new HashSet<>(Arrays.asList("cmnt", "Cmnt", "comment", "Comment"));
    @NonNull
    private Set<String> startWith = new HashSet<>(Arrays.asList("/"));
    private String regex;

    @NonNull
    private Set<String> excludeContains = new HashSet<>();
    @NonNull
    private Set<String> excludeTags = new HashSet<>();
    @NonNull
    private Set<String> excludeStartWith = new HashSet<>();
    private String excludeRegex;

    @NonNull
    private Boolean enableContains = true;
    @NonNull
    private Boolean enableStartWith = true;
    @NonNull
    private Boolean enableTags = true;

    @NonNull
    private Boolean removeMarkers = true;

    public static class CommentMarkerConfigurationBuilder {
        public CommentMarkerConfigurationBuilder addContains(String text) {
            this.contains.add(text);
            return this;
        }


        public CommentMarkerConfigurationBuilder addContains(String... text) {
            this.contains.addAll(Arrays.asList(text));
            return this;
        }

        public CommentMarkerConfigurationBuilder addTag(String tag) {
            this.tags.add(tag);
            return this;
        }

        public CommentMarkerConfigurationBuilder addTag(String... tag) {
            this.tags.addAll(Arrays.asList(tag));
            return this;
        }

        public CommentMarkerConfigurationBuilder addStartWith(String text) {
            this.startWith.add(text);
            return this;
        }

        public CommentMarkerConfigurationBuilder addStartWith(String... text) {
            this.startWith.addAll(Arrays.asList(text));
            return this;
        }

        public CommentMarkerConfigurationBuilder addExcludeContains(String text) {
            this.excludeContains.add(text);
            return this;
        }


        public CommentMarkerConfigurationBuilder addExcludeContains(String... text) {
            this.excludeContains.addAll(Arrays.asList(text));
            return this;
        }

        public CommentMarkerConfigurationBuilder addExcludeTag(String tag) {
            this.excludeTags.add(tag);
            return this;
        }

        public CommentMarkerConfigurationBuilder addExcludeTag(String... tag) {
            this.excludeTags.addAll(Arrays.asList(tag));
            return this;
        }

        public CommentMarkerConfigurationBuilder addExcludeStartWith(String text) {
            this.excludeStartWith.add(text);
            return this;
        }

        public CommentMarkerConfigurationBuilder addExcludeStartWith(String... text) {
            this.excludeStartWith.addAll(Arrays.asList(text));
            return this;
        }
    }
}
