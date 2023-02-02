package com.ayit.friend.enumeration;

public enum MessageType {
    HEADPORTRAIT_TYPE("头像"),
    IMAGE_TYPE("图片/视频"),
    FILE_TYPE("文件"),
    AUDIO_TYPE("音频"),
    OTHER_TYPE("其它");

    private final String name;

    private MessageType(String name)
    {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
