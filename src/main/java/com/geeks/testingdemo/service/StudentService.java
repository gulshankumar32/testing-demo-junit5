package com.geeks.testingdemo.service;

import com.geeks.testingdemo.bean.Student;

import java.util.List;

public interface StudentService {
    List<Student> getAllStudents();
    void addStudent(Student student);
    void deleteStudent(Long studentId);
}
