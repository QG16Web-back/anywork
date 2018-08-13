package com.qg.anywork;

import com.qg.anywork.domain.StudentRepository;
import com.qg.anywork.domain.UserRepository;
import com.qg.anywork.model.po.Student;
import com.qg.anywork.model.po.User;
import com.qg.anywork.util.Encryption;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.NonUniqueResultException;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AnyworkApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void addUser() {
        List<Student> students = (List<Student>) studentRepository.findAll();
        List<User> users = new ArrayList<>();
        int email = 15949988;
        int i = 1;
        for (Student student : students) {
            User user = new User();
            user.setStudentId(student.getStudentId());
            user.setUserName(student.getStudentName());
            user.setPassword(Encryption.getMD5("123456"));
            user.setMark(0);
            user.setEmail(String.valueOf(email + i) + "@qq.com");
            users.add(user);
            i++;
        }
        userRepository.saveAll(users);
    }

    @Test
    public void getStudentId() {
        List<Student> students = (List<Student>) studentRepository.findAll();
        for (Student student : students) {
            System.out.println(student.getStudentId());
        }
    }

    @Test
    public void deleteStudentId() {
        List<Student> students = (List<Student>) studentRepository.findAll();
        for (Student student : students) {
            try {
                userRepository.findByStudentId(student.getStudentId());
            } catch (IncorrectResultSizeDataAccessException e) {
                System.out.println("Test");
                userRepository.deleteAllByStudentId(student.getStudentId());
            }
        }
    }
}
