package com.demo.admindemo.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "USER_TOKEN")
public class UserToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String refreshToken;
    private String accessToken;
    private LocalDateTime rtExpiryDate;
    private LocalDateTime atExpiryDate;
    @Column(unique = true)
    private String userName;


    public UserToken() {
    }

    @Builder
    public UserToken(String refreshToken, String accessToken, LocalDateTime rtExpiryDate, LocalDateTime atExpiryDate,
                     String userName) {
        this.refreshToken = refreshToken;
        this.accessToken = accessToken;
        this.rtExpiryDate = rtExpiryDate;
        this.atExpiryDate = atExpiryDate;
        this.userName = userName;
    }
}
