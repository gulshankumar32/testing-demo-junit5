package com.geeks.testingdemo.service.impl;

import com.geeks.testingdemo.bean.Student;
import com.geeks.testingdemo.exception.BadRequestException;
import com.geeks.testingdemo.exception.StudentNotFoundException;
import com.geeks.testingdemo.repository.StudentRepository;
import com.geeks.testingdemo.service.StudentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public void addStudent(Student student) {
        Boolean existsEmail = studentRepository.selectExistsEmail(student.getEmail());
        if(existsEmail) {
            throw  new BadRequestException("Email " + student.getEmail() + " taken");
        }
        studentRepository.save(student);
    }

    @Override
    public void deleteStudent(Long studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new StudentNotFoundException("Student with id " + studentId + " does not exist");
        }
        studentRepository.deleteById(studentId);
    }
}
