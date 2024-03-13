package com.example.demo;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface ContactRepository extends JpaRepository<Contact, Long> {
    @Query("SELECT DISTINCT group_name FROM Contact")
    List<String> findDistinctGroupName();

    Optional<Contact> findByToken(String token);
}

