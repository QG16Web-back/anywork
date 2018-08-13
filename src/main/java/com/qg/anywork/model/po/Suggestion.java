package com.qg.anywork.model.po;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Create by ming on 18-8-10 下午9:42
 * 建议实体类
 *
 * @author ming
 * I'm the one to ignite the darkened skies.
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "suggestion")
public class Suggestion {

    @Id
    @Column(name = "suggestion_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer suggestionId;

    @Column(name = "description")
    private String description;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, optional = false)
    @JoinColumn(name = "user_id")
    private User user;
}