package com.demo.daniel.util;

import com.demo.daniel.model.entity.User;
import jakarta.persistence.criteria.Predicate;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class UserSpecifications {

    public static Specification<User> buildSpecification(String username, String email) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.isNotEmpty(username)) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("username")),
                        "%" + username.toLowerCase() + "%"));
            }

            if (StringUtils.isNotEmpty(email)) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("email")),
                        "%" + email.toLowerCase() + "%"));
            }

            predicates.add(criteriaBuilder.equal(root.get("superAdmin"), false));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
