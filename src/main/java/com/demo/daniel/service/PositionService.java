package com.demo.daniel.service;

import com.demo.daniel.convert.PositionConvert;
import com.demo.daniel.exception.BusinessException;
import com.demo.daniel.model.ErrorCode;
import com.demo.daniel.model.dto.PositionQueryDTO;
import com.demo.daniel.model.dto.PositionUpsertDTO;
import com.demo.daniel.model.entity.Position;
import com.demo.daniel.repository.PositionRepository;
import com.demo.daniel.repository.UserRepository;
import com.demo.daniel.util.PositionSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PositionService {

    @Autowired
    private PositionRepository positionRepository;
    @Autowired
    private UserRepository userRepository;

    public Page<Position> getPositions(PositionQueryDTO request) {
        Specification<Position> spec = PositionSpecifications.buildSpecification(request.getCode(), request.getName());
        return positionRepository.findAll(spec, PageRequest.of(request.getPage(), request.getSize()));
    }

    public Position getPosition(Long id) {
        return positionRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.POSITION_NOT_EXIST.getCode(), "Position ID " + id + " not found"));
    }

    public void upsertPosition(PositionUpsertDTO request) {
        Position position = Optional.ofNullable(request.getId())
                .flatMap(id -> positionRepository.findById(id))
                .map(r -> PositionConvert.convertToEntity(request, r))
                .orElseGet(() -> PositionConvert.convertToEntity(request, null));

        positionRepository.save(position);
    }

    public void deletePositions(List<Long> ids) {
        List<String> errors = new ArrayList<>();

        ids.forEach(id -> positionRepository.findById(id).ifPresentOrElse(position -> userRepository.findByPositionsContaining(position)
                .filter(users -> !users.isEmpty())
                .ifPresentOrElse(users -> errors.add("Position " + position.getName() + " (ID: " + id + ") is used by " + users.size() + " users")
                        , () -> positionRepository.deleteById(id)), () -> errors.add("Position ID " + id + " not found")));

        Optional.of(errors)
                .filter(errs -> !errs.isEmpty())
                .ifPresent(errs -> {
                    throw new BusinessException(ErrorCode.POSITION_IN_USE.getCode(),
                            "Position(s) cannot be deleted: " + String.join("; ", errs));
                });
    }
}
