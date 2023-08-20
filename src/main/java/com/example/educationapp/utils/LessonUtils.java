package com.example.educationapp.utils;

import com.example.educationapp.entity.Lesson;
import com.example.educationapp.exception.LessonNotFoundException;
import com.example.educationapp.repo.LessonRepo;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class LessonUtils {
    private Lesson lesson;

    private final LessonRepo lessonRepo;

    public void validateLesson(Long lessonId) {
        Optional<Lesson> lessonOptional = lessonRepo.findById(lessonId);

        if(!lessonOptional.isPresent()) {
            throw new LessonNotFoundException("Lesson is not found");
        }

        lesson = lessonOptional.get();
    }

    public Lesson getValidatedLesson(Long lessonId) {
        validateLesson(lessonId);
        return lesson;
    }
}
