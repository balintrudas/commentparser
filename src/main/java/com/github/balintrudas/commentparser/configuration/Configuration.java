package com.github.balintrudas.commentparser.configuration;

import lombok.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Configuration class for comment parser.
 */

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Configuration {

    @NonNull
    private CommentMarkerConfiguration commentMarkerConfiguration = new CommentMarkerConfiguration();
    @NonNull
    private GroupMarkerConfiguration groupMarkerConfiguration = new GroupMarkerConfiguration();
    @NonNull
    private List<String> baseDirs = new ArrayList<>(Arrays.asList(System.getProperty("user.dir")));
    @NonNull
    private List<String> sourceRoots = new ArrayList<>(Arrays.asList("src/main/java"));

}
