package com.void2.careermanagement.dao;

import com.void2.careermanagement.dto.ResumeSkillDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ResumeSkillDao {
    int insertResumeSkill(@Param("list") List<ResumeSkillDto> resumeSkillDtoList);
}
