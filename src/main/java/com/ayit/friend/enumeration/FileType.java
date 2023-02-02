package com.ayit.friend.enumeration;

public enum FileType {

    TEXT("text", 2),

    APPLICATION("application", 2),


    IMAGE("image ",1),

    VIDEO("video",1),

    AUDIO("audio",3);


    private String value = "";
    private int ext = 1;

    FileType(String value) {
        this.value = value;
    }

    FileType(String value, int ext) {
        this(value);
        this.ext = ext;
    }

    public int getExt() {
        return ext;
    }

    public String getValue() {
        return value;
    }

}