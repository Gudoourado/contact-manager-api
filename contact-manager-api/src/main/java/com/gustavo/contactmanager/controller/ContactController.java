package com.gustavo.contactmanager.controller;

import com.gustavo.contactmanager.dto.ContactDTO;
import com.gustavo.contactmanager.service.ContactService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contacts")
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping
    public ResponseEntity<List<ContactDTO>> getAllContacts() {
        List<ContactDTO> contacts = contactService.findAll();
        return ResponseEntity.ok(contacts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContactDTO> getContactById(@PathVariable Long id) {
        ContactDTO contact = contactService.findById(id);
        return ResponseEntity.ok(contact);
    }

    @PostMapping
    public ResponseEntity<ContactDTO> createContact(@Valid @RequestBody ContactDTO contactDTO) {
        ContactDTO created = contactService.create(contactDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContactDTO> updateContact(@PathVariable Long id,
                                                     @Valid @RequestBody ContactDTO contactDTO) {
        ContactDTO updated = contactService.update(id, contactDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable Long id) {
        contactService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<ContactDTO>> searchContacts(@RequestParam String keyword) {
        List<ContactDTO> contacts = contactService.searchByKeyword(keyword);
        return ResponseEntity.ok(contacts);
    }

    @GetMapping("/search/name")
    public ResponseEntity<List<ContactDTO>> searchByName(@RequestParam String name) {
        List<ContactDTO> contacts = contactService.searchByName(name);
        return ResponseEntity.ok(contacts);
    }
}
