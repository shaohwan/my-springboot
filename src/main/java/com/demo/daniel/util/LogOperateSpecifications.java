package com.demo.daniel.util;

import com.demo.daniel.model.entity.LogOperate;
import jakarta.persistence.criteria.Predicate;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class LogOperateSpecifications {

    public static Specification<LogOperate> buildSpecification(String username, String module, String requestUri, Integer status) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.isNotEmpty(username)) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("username")),
                        "%" + username.toLowerCase() + "%"));
            }

            if (StringUtils.isNotEmpty(module)) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("module")),
                        "%" + module.toLowerCase() + "%"));
            }

            if (StringUtils.isNotEmpty(requestUri)) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("requestUri")),
                        "%" + requestUri.toLowerCase() + "%"));
            }

            if (status != null) {
                predicates.add(criteriaBuilder.equal(
                        root.get("status"),
                        status));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
