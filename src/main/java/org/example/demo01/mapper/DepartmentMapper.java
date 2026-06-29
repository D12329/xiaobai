package org.example.demo01.mapper;

import org.example.demo01.entity.Department;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 閮ㄩ棬Mapper鎺ュ彛
 * 鎻愪緵閮ㄩ棬鏁版嵁鐨勬暟鎹簱璁块棶鑳藉姏
 */
@Mapper
public interface DepartmentMapper extends BaseMapper<Department> {
}
