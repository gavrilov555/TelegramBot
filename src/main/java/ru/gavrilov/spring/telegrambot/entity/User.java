package ru.gavrilov.spring.telegrambot.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy =GenerationType.AUTO)

    private Long id;
    private Long chatId;
    private Integer stateId;

    private String phone;
    private String userName;

    private String email;
    private Boolean admin;
    private Boolean notified = false;

    public User() {
    }

    public User(Long chatId, Integer state) {
        this.chatId = chatId;
        this.stateId = state;
    }

    public User(Long chatId, Integer stateId, Boolean admin) {
        this.chatId = chatId;
        this.stateId = stateId;
        this.admin = admin;
    }


}
