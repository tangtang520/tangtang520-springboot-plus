package com.tfy.framework.common.utils.time;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.format.DateTimeFormatter;

@AllArgsConstructor
@Getter
public enum TimeFormatEnum {
    /*格式 YYYY-MM-dd 2018-09-10*/
    YYYY_MM_DD(DateTimeFormatter.ofPattern("YYYY-MM-dd")),
    /*格式 YYYY.MM.dd 2018.09.10*/
    SECOND(DateTimeFormatter.ofPattern("YYYY.MM.dd"));

    private DateTimeFormatter timeFormatter;
}
