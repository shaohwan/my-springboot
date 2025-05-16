package com.demo.daniel.util;

import com.demo.daniel.model.entity.Attachment;
import jakarta.persistence.criteria.Predicate;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class AttachmentSpecifications {

    public static Specification<Attachment> buildSpecification(String name, String platform) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.isNotEmpty(name)) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")),
                        "%" + name.toLowerCase() + "%"));
            }

            if (StringUtils.isNotEmpty(platform)) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("platform")),
                        "%" + platform.toLowerCase() + "%"));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
