package org.example.demo01.mapper;

import org.example.demo01.entity.Resume;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ResumeMapper extends BaseMapper<Resume> {

    @Select("SELECT r.* FROM rec_resume r " +
            "LEFT JOIN rec_candidate c ON r.id = c.resume_id " +
            "WHERE c.id IS NULL " +
            "AND (r.name LIKE CONCAT('%', #{keyword}, '%') OR r.phone LIKE CONCAT('%', #{keyword}, '%'))")
    List<Resume> selectUnconvertedResumes(@Param("keyword") String keyword);
}
