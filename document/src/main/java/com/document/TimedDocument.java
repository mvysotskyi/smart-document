package com.document;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TimedDocument implements Document {
    private String path;

    @Override
    public String parse() {
        Long start = System.currentTimeMillis();
        String result = new SmartDocument(path).parse();
        Long end = System.currentTimeMillis();

        System.out.println("Time taken: " + (end - start) + "ms");
        return result;
    }
}
