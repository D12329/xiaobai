package org.example.demo01.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.demo01.common.PageRequest;
import org.example.demo01.common.PageResult;
import org.example.demo01.common.Result;
import org.example.demo01.entity.Position;
import org.example.demo01.mapper.PositionMapper;
import org.example.demo01.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PositionServiceImpl implements PositionService {

    @Autowired
    private PositionMapper positionMapper;

    @Override
    @Transactional(readOnly = true)
    public IPage<Position> page(Integer page, Integer size, String positionName, Integer status) {
        Page<Position> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Position> wrapper = new LambdaQueryWrapper<>();
        if (positionName != null && !positionName.isEmpty()) {
            wrapper.like(Position::getName, positionName);
        }
        if (status != null) {
            wrapper.eq(Position::getStatus, status);
        }
        wrapper.orderByDesc(Position::getCreatedAt);
        return positionMapper.selectPage(pageParam, wrapper);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Position> list() {
        LambdaQueryWrapper<Position> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Position::getStatus, 1);
        wrapper.orderByDesc(Position::getCreatedAt);
        return positionMapper.selectList(wrapper);
    }

    @Override
    @Transactional(readOnly = true)
    public Position getById(Long id) {
        return positionMapper.selectById(id);
    }

    @Override
    @Transactional
    public boolean create(Position position) {
        position.setCreatedAt(LocalDateTime.now());
        if (position.getStatus() == null) {
            position.setStatus(1);
        }
        return positionMapper.insert(position) > 0;
    }

    @Override
    @Transactional
    public boolean update(Position position) {
        LambdaQueryWrapper<Position> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Position::getId, position.getId());
        return positionMapper.update(position, wrapper) > 0;
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        return positionMapper.deleteById(id) > 0;
    }

    @Override
    @Transactional(readOnly = true)
    public Result<PageResult<Position>> getPositionPage(PageRequest pageRequest) {
        IPage<Position> pageResult = page(
                pageRequest.getPage(),
                pageRequest.getSize(),
                pageRequest.getKeyword(),
                pageRequest.getStatus()
        );
        PageResult<Position> result = new PageResult<>();
        result.setContent(pageResult.getRecords());
        result.setTotalElements(pageResult.getTotal());
        result.setTotalPages((int) pageResult.getPages());
        result.setCurrentPage(pageResult.getCurrent());
        result.setPageSize(pageResult.getSize());
        return Result.success(result);
    }

    @Override
    @Transactional
    public Result<Position> addPosition(Position position) {
        position.setCreatedAt(LocalDateTime.now());
        if (position.getStatus() == null) {
            position.setStatus(1);
        }
        positionMapper.insert(position);
        return Result.success(position);
    }

    @Override
    @Transactional
    public Result<Position> updatePosition(Position position) {
        LambdaQueryWrapper<Position> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Position::getId, position.getId());
        positionMapper.update(position, wrapper);
        return Result.success(position);
    }

    @Override
    @Transactional
    public Result<Void> deletePosition(Long id) {
        positionMapper.deleteById(id);
        return Result.success();
    }

    @Override
    @Transactional
    public Result<Void> updateStatus(Long id, Integer status) {
        Position position = positionMapper.selectById(id);
        if (position != null) {
            position.setStatus(status);
            LambdaQueryWrapper<Position> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Position::getId, id);
            positionMapper.update(position, wrapper);
        }
        return Result.success();
    }
}
