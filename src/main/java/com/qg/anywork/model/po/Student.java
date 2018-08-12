package com.qg.anywork.model.po;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Create by ming on 18-8-11 上午10:49
 * 学生实体类
 *
 * @author ming
 * I'm the one to ignite the darkened skies.
 */
@Entity
@Table(name = "student")
@Data
@NoArgsConstructor
public class Student {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "student_id")
    private String studentId;

    @Column(name = "student_name")
    private String studentName;
}
