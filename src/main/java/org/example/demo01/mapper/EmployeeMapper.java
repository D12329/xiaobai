package org.example.demo01.mapper;

import org.example.demo01.entity.Employee;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 鍛樺伐Mapper鎺ュ彛
 * 鎻愪緵鍛樺伐鏁版嵁鐨勬暟鎹簱璁块棶鑳藉姏
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
