package com.gustavo.contactmanager.service;

import com.gustavo.contactmanager.dto.ContactDTO;
import com.gustavo.contactmanager.exception.DuplicateResourceException;
import com.gustavo.contactmanager.exception.ResourceNotFoundException;
import com.gustavo.contactmanager.model.Contact;
import com.gustavo.contactmanager.repository.ContactRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContactService {

    private final ContactRepository contactRepository;

    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public List<ContactDTO> findAll() {
        return contactRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public ContactDTO findById(Long id) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contato não encontrado com id: " + id));
        return toDTO(contact);
    }

    public ContactDTO create(ContactDTO dto) {
        if (contactRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("Já existe um contato com o email: " + dto.getEmail());
        }
        Contact contact = toEntity(dto);
        Contact saved = contactRepository.save(contact);
        return toDTO(saved);
    }

    public ContactDTO update(Long id, ContactDTO dto) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contato não encontrado com id: " + id));

        contactRepository.findByEmail(dto.getEmail())
                .ifPresent(existing -> {
                    if (!existing.getId().equals(id)) {
                        throw new DuplicateResourceException("Já existe um contato com o email: " + dto.getEmail());
                    }
                });

        contact.setName(dto.getName());
        contact.setEmail(dto.getEmail());
        contact.setPhone(dto.getPhone());
        contact.setAddress(dto.getAddress());
        contact.setCompany(dto.getCompany());
        contact.setNotes(dto.getNotes());

        Contact updated = contactRepository.save(contact);
        return toDTO(updated);
    }

    public void delete(Long id) {
        if (!contactRepository.existsById(id)) {
            throw new ResourceNotFoundException("Contato não encontrado com id: " + id);
        }
        contactRepository.deleteById(id);
    }

    public List<ContactDTO> searchByName(String name) {
        return contactRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<ContactDTO> searchByKeyword(String keyword) {
        return contactRepository.searchByKeyword(keyword)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // Conversão Entity -> DTO
    private ContactDTO toDTO(Contact contact) {
        return new ContactDTO(
                contact.getId(),
                contact.getName(),
                contact.getEmail(),
                contact.getPhone(),
                contact.getAddress(),
                contact.getCompany(),
                contact.getNotes()
        );
    }

    // Conversão DTO -> Entity
    private Contact toEntity(ContactDTO dto) {
        Contact contact = new Contact();
        contact.setName(dto.getName());
        contact.setEmail(dto.getEmail());
        contact.setPhone(dto.getPhone());
        contact.setAddress(dto.getAddress());
        contact.setCompany(dto.getCompany());
        contact.setNotes(dto.getNotes());
        return contact;
    }
}
