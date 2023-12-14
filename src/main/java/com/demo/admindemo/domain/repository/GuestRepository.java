package com.demo.admindemo.domain.repository;

import com.demo.admindemo.domain.entity.Guest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuestRepository extends JpaRepository<Guest, Long> {
}
