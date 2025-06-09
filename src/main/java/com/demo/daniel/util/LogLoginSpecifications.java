package com.demo.daniel.util;

import com.demo.daniel.model.entity.LogLogin;
import jakarta.persistence.criteria.Predicate;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class LogLoginSpecifications {

    public static Specification<LogLogin> buildSpecification(String username, String startTime, String endTime) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.isNotEmpty(username)) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("username")),
                        "%" + username.toLowerCase() + "%"));
            }

            LocalDateTime start = DateUtils.parseDate(startTime, false);
            if (start != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.get("createTime"), start));
            }

            LocalDateTime end = DateUtils.parseDate(endTime, true);
            if (end != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                        root.get("createTime"), end));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
