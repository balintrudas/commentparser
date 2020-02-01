package com.github.balintrudas.commentparser.marker.group;

import com.github.balintrudas.commentparser.marker.MarkerElement;
import com.github.javaparser.ast.Node;

import java.util.List;

public interface GroupMarkerElementParser<T extends MarkerElement> {
    List<T> parse(Node md);
}
