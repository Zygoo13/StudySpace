package com.zygoo132.studyspace.dto;

import lombok.Data;

import java.util.List;

@Data
public class BookingConfirmDTO {
    private String date;
    private String startTime;
    private Long comboId;
    private Integer peopleCount;

    private List<Long> deskIds;
    private Long customerId;

    private String note; // Ghi chú thêm của nhân viên
}
