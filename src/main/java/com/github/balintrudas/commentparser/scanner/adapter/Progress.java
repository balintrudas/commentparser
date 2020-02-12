package com.github.balintrudas.commentparser.scanner.adapter;

import lombok.Data;

@Data
public class Progress {
    private int count = 0;
    private int current = 0;

    public Progress next() {
        this.current = this.current + 1;
        return this;
    }

}
