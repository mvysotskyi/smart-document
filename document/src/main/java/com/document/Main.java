package com.document;

public class Main {
    public static void main(String[] args) {
        String path1 = "gs://cloud-samples-data/vision/ocr/sign.jpg";
        String path2 = "gs://cloud-samples-data/vision/ocr/sign2.jpg";

        Document dc1 = new CachedDocument(path1);
        Document dc2 = new CachedDocument(path2);
        Document dc3 = new CachedDocument(path1);

        System.out.println(dc1.parse() == dc3.parse());
        System.out.println(dc1.parse() == dc2.parse());
    }
}