package com.qg.anywork;

import com.qg.anywork.domain.UserRepository;
import com.qg.anywork.model.po.Question;
import com.qg.anywork.util.ExcelUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AnyworkApplicationTests {

    //    @Autowired
//    private StudentRepository studentRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void contextLoads() {
    }
//
//    @Test
//    public void addUser() {
//        List<Student> students = (List<Student>) studentRepository.findAll();
//        List<User> users = new ArrayList<>();
//        int email = 15949988;
//        int i = 1;
//        for (Student student : students) {
//            User user = new User();
//            user.setStudentId(student.getStudentId());
//            user.setUserName(student.getStudentName());
//            user.setPassword(Encryption.getMD5("123456"));
//            user.setMark(0);
//            user.setEmail(String.valueOf(email + i) + "@qq.com");
//            users.add(user);
//            i++;
//        }
//        userRepository.saveAll(users);
//    }
//
//    @Test
//    public void getStudentId() {
//        List<Student> students = (List<Student>) studentRepository.findAll();
//        for (Student student : students) {
//            System.out.println(student.getStudentId());
//        }
//    }
//
//    @Test
//    public void deleteStudentId() {
//        List<Student> students = (List<Student>) studentRepository.findAll();
//        for (Student student : students) {
//            try {
//                userRepository.findByStudentId(student.getStudentId());
//            } catch (IncorrectResultSizeDataAccessException e) {
//                System.out.println("Test");
//                userRepository.deleteAllByStudentId(student.getStudentId());
//            }
//        }
//    }

    @Test
    public void getQuestion() throws Exception {
        InputStream inputStream = new FileInputStream(new File("/home/ming/桌面/anywork/Anywork题库/第三章 最简单的C程序设计——顺序程序设计/3.2数据的表现形式及其运算.xlsx"));
        List<Question> questions = ExcelUtil.getQuestionList(inputStream);
        System.out.println(questions.size());
        System.out.println(questions);
    }
}
