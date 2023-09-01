package com.example.educationapp.aspect;

import com.example.educationapp.exception.BadDataException;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RepoExceptionAspect {

    @AfterThrowing(pointcut = "execution(* com.example.educationapp.repo.*.*(..))", throwing = "ex")
    public void handleRepositoryException(PropertyReferenceException ex) {
        throw new BadDataException(String.format("Property with name %s is not found.",ex.getPropertyName()));
    }
}
