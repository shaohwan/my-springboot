package com.demo.daniel.controller;

import com.demo.daniel.annotation.OperateLog;
import com.demo.daniel.convert.PositionConvert;
import com.demo.daniel.model.ApiResponse;
import com.demo.daniel.model.dto.PositionQueryDTO;
import com.demo.daniel.model.dto.PositionUpsertDTO;
import com.demo.daniel.model.entity.LogOperateType;
import com.demo.daniel.model.entity.Position;
import com.demo.daniel.model.vo.PositionVO;
import com.demo.daniel.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/post")
public class PositionController {

    @Autowired
    private PositionService positionService;

    @GetMapping
    // @PreAuthorize("hasAuthority('post:search')")
    public ApiResponse<Page<PositionVO>> getPositions(@ModelAttribute PositionQueryDTO request) {
        Page<PositionVO> positions = positionService.getPositions(request).map(PositionConvert::convertToVO);
        return ApiResponse.ok(positions);
    }

    @GetMapping("/{id}")
    public ApiResponse<PositionVO> getPosition(@PathVariable Long id) {
        Position position = positionService.getPosition(id);
        return ApiResponse.ok(PositionConvert.convertToVO(position));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('post:add')")
    @OperateLog(module = "岗位管理", name = "新增岗位", type = LogOperateType.ADD)
    public ApiResponse<Void> createPosition(@RequestBody PositionUpsertDTO request) {
        positionService.upsertPosition(request);
        return ApiResponse.ok();
    }

    @PutMapping
    @PreAuthorize("hasAuthority('post:edit')")
    @OperateLog(module = "岗位管理", name = "编辑岗位", type = LogOperateType.EDIT)
    public ApiResponse<Void> updatePosition(@RequestBody PositionUpsertDTO request) {
        positionService.upsertPosition(request);
        return ApiResponse.ok();
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('post:delete')")
    @OperateLog(module = "岗位管理", name = "删除岗位(们)", type = LogOperateType.DELETE)
    public ApiResponse<Void> deletePositions(@RequestBody List<Long> ids) {
        positionService.deletePositions(ids);
        return ApiResponse.ok();
    }
}
