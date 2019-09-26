package com.qly.myforum.enums;

import ch.qos.logback.core.joran.conditional.ElseAction;
import com.qly.myforum.pojo.Question;
import com.sun.xml.internal.ws.api.pipe.ContentType;

public enum ContentTypeEnum {
    QUESTION(1),
    COMMENT(2);
    private Integer type;

    ContentTypeEnum(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }

    public static boolean isExistType(Integer type) {
        for (ContentTypeEnum contentTypeEnum : ContentTypeEnum.values()) {
            if (type == contentTypeEnum.getType())
                return true;
        }
        return true;
    }
}
