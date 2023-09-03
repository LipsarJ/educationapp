package com.example.educationapp.repo.specification;


import com.example.educationapp.entity.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecifications {

    public static Specification<User> searchByFilterText(String filterText) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.or(
                criteriaBuilder.like(root.get("username"), "%" + filterText + "%"),
                criteriaBuilder.like(root.get("firstname"), "%" + filterText + "%"),
                criteriaBuilder.like(root.get("middlename"), "%" + filterText + "%"),
                criteriaBuilder.like(root.get("lastname"), "%" + filterText + "%")
        );
    }
}
