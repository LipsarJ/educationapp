package com.example.educationapp.repo.specification;


import com.example.educationapp.entity.User;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecifications {

    public static Specification<User> searchByFilterText(String filterText) {
        return (root, query, criteriaBuilder) -> {
            String filterTextLowerCase = "%" + filterText.toLowerCase() + "%"; // Преобразование поискового текста в нижний регистр

            Predicate usernamePredicate = criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("username")),
                    filterTextLowerCase
            );

            Predicate firstnamePredicate = criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("firstname")),
                    filterTextLowerCase
            );

            Predicate middlenamePredicate = criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("middlename")),
                    filterTextLowerCase
            );

            Predicate lastnamePredicate = criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("lastname")),
                    filterTextLowerCase
            );

            // Объединение предикатов с помощью "или"
            return criteriaBuilder.or(usernamePredicate, firstnamePredicate, middlenamePredicate, lastnamePredicate);
        };

    }
}
