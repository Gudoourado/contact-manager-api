package com.gustavo.contactmanager.repository;

import com.gustavo.contactmanager.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {

    Optional<Contact> findByEmail(String email);

    List<Contact> findByNameContainingIgnoreCase(String name);

    List<Contact> findByCompanyContainingIgnoreCase(String company);

    @Query("SELECT c FROM Contact c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(c.email) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(c.company) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Contact> searchByKeyword(@Param("keyword") String keyword);

    boolean existsByEmail(String email);
}
