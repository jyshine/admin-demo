package com.demo.admindemo.domain.repository;

import com.demo.admindemo.domain.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Admin findByUser_UserName(String userName);

}
