package com.demo.daniel.util;

import com.demo.daniel.model.entity.ScheduleJobLog;
import jakarta.persistence.criteria.Predicate;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class ScheduleJobLogSpecifications {

    public static Specification<ScheduleJobLog> buildSpecification(Long jobId, String jobName, String jobGroup) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (ObjectUtils.isNotEmpty(jobId)) {
                predicates.add(criteriaBuilder.equal(root.get("jobId"), jobId));
            }

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

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
