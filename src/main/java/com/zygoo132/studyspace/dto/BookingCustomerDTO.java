package com.zygoo132.studyspace.dto;

import lombok.Data;

import java.util.List;

@Data
public class BookingCustomerDTO {
    private String date;
    private String startTime;
    private Long comboId;
    private Integer peopleCount;

    private List<Long> deskIds;

    private String phone;
    private String name; // dùng khi tạo khách mới
}
