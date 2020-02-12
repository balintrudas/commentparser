package com.github.balintrudas.commentparser.scanner.adapter;

import com.github.balintrudas.commentparser.scanner.CommentStore;

public interface ParseProcessAdapter {

    void onSuccess(CommentStore commentStore);

    default void onError(Exception exception) {
    }

    default void onProgress(Progress progress) {
    }

    default void onCancel() {
    }

    default boolean isCanceled() {
        return false;
    }

}
