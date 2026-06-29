package org.example.demo01.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.example.demo01.common.PageRequest;
import org.example.demo01.common.PageResult;
import org.example.demo01.common.Result;
import org.example.demo01.entity.Position;

import java.util.List;

public interface PositionService {

    IPage<Position> page(Integer page, Integer size, String positionName, Integer status);

    List<Position> list();

    Position getById(Long id);

    boolean create(Position position);

    boolean update(Position position);

    boolean delete(Long id);

    Result<PageResult<Position>> getPositionPage(PageRequest pageRequest);

    Result<Position> addPosition(Position position);

    Result<Position> updatePosition(Position position);

    Result<Void> deletePosition(Long id);

    Result<Void> updateStatus(Long id, Integer status);
}
