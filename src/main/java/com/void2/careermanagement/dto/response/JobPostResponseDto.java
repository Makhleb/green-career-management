package com.void2.careermanagement.dto.response;

import com.void2.careermanagement.dto.GubnDto;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created on 2024-12-02 by 황승현
 */

@Data
public class JobPostResponseDto {
    private int jobPostNo;
    private String companyId;
    private String companyName;
    private String companyAddress;
    private String companyAddressDetail;
    private LocalDate companyBirth;
    private byte[] companyImage;
    private String companyImageBase64;
    private String companyEmail;
    private int companyEmployee;
    private String companyInfo;
    private String companyWebsite;
    private String companyZonecode;
    private String title;
    private int jobHistory;
    private int jobSalary;
    private String content;
    private String method;
    private String addNotice;
    private String managerName;
    private String managerEmail;
    private String managerPhone;
    private String educationCode;
    private String jobRankCode;
    private String workCode;
    private String workTypeCode;
    private String educationName;
    private String jobRankName;
    private String workName;
    private String workTypeName;
    private int process;
    private List<String> benefitList;
    private List<GubnDto> skillList;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
