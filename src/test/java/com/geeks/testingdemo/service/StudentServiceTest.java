package com.geeks.testingdemo.service;

import com.geeks.testingdemo.bean.Gender;
import com.geeks.testingdemo.bean.Student;
import com.geeks.testingdemo.exception.BadRequestException;
import com.geeks.testingdemo.exception.StudentNotFoundException;
import com.geeks.testingdemo.repository.StudentRepository;
import com.geeks.testingdemo.service.impl.StudentServiceImpl;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import org.mockito.Mock;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock private StudentRepository studentRepository;
    private StudentService underTest;

    @BeforeEach
    void setUp() {
        underTest = new StudentServiceImpl(studentRepository);
    }

    @Test
    void getAllStudents() {
        //when
        underTest.getAllStudents();
        //then
        verify(studentRepository).findAll();
    }

    @Test
    void addStudent() {
        //given
        Student student = new Student("junaid", "junaid@gmail.com", Gender.MALE);
        //when
        underTest.addStudent(student);
        //then
        ArgumentCaptor<Student> studentArgumentCaptor = ArgumentCaptor.forClass(Student.class);
        verify(studentRepository).save(studentArgumentCaptor.capture());
        Student expected = studentArgumentCaptor.getValue();
        assertThat(expected).isEqualTo(student);
    }

    @Test
    void addStudentThrowWhenEmailIsTaken() {
        //given
        Student student = new Student("junaid", "junaid@gmail.com", Gender.MALE);
        given(studentRepository.selectExistsEmail(anyString())).willReturn(true);

        //when
        //then
        assertThatThrownBy(() -> underTest.addStudent(student))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Email " + student.getEmail() + " taken");

        verify(studentRepository, never()).save(any());
    }

    @Test
    void deleteStudent() {
        //given
        long id = 10;
        given(studentRepository.existsById(id)).willReturn(true);
        //when
        underTest.deleteStudent(id);

        //then
        verify(studentRepository).deleteById(id);
    }

    @Test
    void deleteStudentThrowWhenIdNotFound() {
        //given
        long id = 10;
        given(studentRepository.existsById(id)).willReturn(false);
        //when
        //then
        assertThatThrownBy(() -> underTest.deleteStudent(id))
                .isInstanceOf(StudentNotFoundException.class)
                .hasMessageContaining("Student with id " + id + " does not exist");
        verify(studentRepository, never()).deleteById(any());
    }
}