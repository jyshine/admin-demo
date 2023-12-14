package com.demo.admindemo.domain.entity;

import com.demo.admindemo.domain.enums.GuestStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "GUEST")
public class Guest extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String centerName;
    private String businessNumber;
    private String name;
    private String phoneNumber;
    private String email;
    private String notes;
    private String preferredUserId;
    private String deletedAt;
    @Enumerated(EnumType.STRING)
    private GuestStatus status;
    private LocalDateTime confirmedAt;

    @Setter
    private String password;

    @Builder
    public Guest(String centerName, String businessNumber, String name, String phoneNumber, String email, String notes,
                 String preferredUserId,
                 GuestStatus status,
                 LocalDateTime confirmedAt) {
        this.centerName = centerName;
        this.businessNumber = businessNumber;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.notes = notes;
        this.preferredUserId = preferredUserId;
        this.status = status;
        this.confirmedAt = confirmedAt;
    }

    public Guest() {
    }

    public void changeGuestStatus(GuestStatus status) {
        this.status = status;
        this.confirmedAt = LocalDateTime.now();
    }
}
