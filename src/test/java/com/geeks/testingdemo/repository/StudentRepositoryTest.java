package com.geeks.testingdemo.repository;

import com.geeks.testingdemo.bean.Gender;
import com.geeks.testingdemo.bean.Student;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class StudentRepositoryTest {

    @Autowired
    private StudentRepository underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void testWhenStudentEmailExists() {
        //given
        String email = "ali@gmail.com";
        Student student = new Student("ali", email, Gender.MALE);
        underTest.save(student);

        //when
        boolean expected = underTest.selectExistsEmail(email);

        //then
        assertThat(expected).isTrue();
    }

    @Test
    void testWhenStudentEmailDoesNotExists() {
        //given
        String email = "ali@gmail.com";
        Student student = new Student("ali", email, Gender.MALE);

        //when
        boolean expected = underTest.selectExistsEmail(email);

        //then
        assertThat(expected).isFalse();
    }
}