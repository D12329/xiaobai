package org.example.demo01.mapper;

import org.example.demo01.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 鐢ㄦ埛Mapper鎺ュ彛
 * 鎻愪緵鐢ㄦ埛鏁版嵁鐨勬暟鎹簱璁块棶鑳藉姏
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
