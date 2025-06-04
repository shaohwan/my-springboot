package com.demo.daniel.convert;

import com.demo.daniel.model.dto.PositionUpsertDTO;
import com.demo.daniel.model.entity.Position;
import com.demo.daniel.model.vo.PositionVO;
import org.springframework.beans.BeanUtils;

public class PositionConvert {

    private PositionConvert() {
    }

    public static PositionVO convertToVO(Position position) {
        PositionVO positionVO = new PositionVO();
        BeanUtils.copyProperties(position, positionVO);
        return positionVO;
    }

    public static Position convertToEntity(PositionUpsertDTO dto, Position position) {
        Position newPosition = new Position();
        if (position != null) {
            newPosition = position;
        }
        BeanUtils.copyProperties(dto, newPosition);
        return newPosition;
    }
}
