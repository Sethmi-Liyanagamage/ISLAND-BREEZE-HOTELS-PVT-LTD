package com.hotelmanagementsystem.service;

import com.hotelmanagementsystem.dto.ContactMessageDTO;
import com.hotelmanagementsystem.dto.CreateContactMessageDTO;
import com.hotelmanagementsystem.model.ContactMessage;
import com.hotelmanagementsystem.repository.ContactMessageRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContactMessageService {

    private final ContactMessageRepository contactMessageRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ContactMessageService(ContactMessageRepository contactMessageRepository, ModelMapper modelMapper) {
        this.contactMessageRepository = contactMessageRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public ContactMessageDTO saveMessage(CreateContactMessageDTO messageDTO) {
        ContactMessage message = modelMapper.map(messageDTO, ContactMessage.class);
        message.setSubmittedAt(LocalDateTime.now());
        message.setReplied(false);
        ContactMessage savedMessage = contactMessageRepository.save(message);
        return convertToDTO(savedMessage);
    }

    @Transactional(readOnly = true)
    public List<ContactMessageDTO> getAllMessages() {
        return contactMessageRepository.findAllByOrderBySubmittedAtDesc().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ContactMessageDTO> getUnrepliedMessages() {
        return contactMessageRepository.findByRepliedFalseOrderBySubmittedAtDesc().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ContactMessageDTO> getUrgentMessages() {
        return contactMessageRepository.findByUrgentTrueAndRepliedFalseOrderBySubmittedAtDesc().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ContactMessageDTO replyToMessage(Long messageId, String reply) {
        return contactMessageRepository.findById(messageId).map(message -> {
            message.setReplied(true);
            message.setAdminReply(reply);
            message.setRepliedAt(LocalDateTime.now());
            ContactMessage updatedMessage = contactMessageRepository.save(message);
            return convertToDTO(updatedMessage);
        }).orElseThrow(() -> new RuntimeException("Message not found with id: " + messageId));
    }

    @Transactional(readOnly = true)
    public ContactMessageDTO getMessageById(Long id) {
        return contactMessageRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Message not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public long getUnrepliedCount() {
        return contactMessageRepository.countByRepliedFalse();
    }

    @Transactional
    public void deleteMessage(Long id) {
        if (!contactMessageRepository.existsById(id)) {
            throw new RuntimeException("Message not found with id: " + id);
        }
        contactMessageRepository.deleteById(id);
    }

    private ContactMessageDTO convertToDTO(ContactMessage message) {
        return modelMapper.map(message, ContactMessageDTO.class);
    }
}
