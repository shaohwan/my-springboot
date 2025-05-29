package com.demo.daniel.util;

import com.demo.daniel.model.entity.ScheduleJob;
import jakarta.persistence.criteria.Predicate;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class ScheduleJobSpecifications {

    public static Specification<ScheduleJob> buildSpecification(String jobName, String jobGroup, Integer status) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.isNotEmpty(jobName)) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("jobName")),
                        "%" + jobName.toLowerCase() + "%"));
            }

            if (StringUtils.isNotEmpty(jobGroup)) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("jobGroup")),
                        "%" + jobGroup.toLowerCase() + "%"));
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
