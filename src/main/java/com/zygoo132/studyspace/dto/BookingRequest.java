package com.zygoo132.studyspace.dto;

import lombok.Data;

@Data
public class BookingRequest {
    private String date;        // yyyy-MM-dd
    private String startTime;   // HH:mm
    private Long comboId;       // Combo chọn
    private Integer peopleCount;
}
