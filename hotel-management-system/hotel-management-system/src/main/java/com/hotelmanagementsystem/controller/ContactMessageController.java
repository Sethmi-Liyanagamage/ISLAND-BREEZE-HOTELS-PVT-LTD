package com.hotelmanagementsystem.controller;

import com.hotelmanagementsystem.dto.ContactMessageDTO;
import com.hotelmanagementsystem.dto.CreateContactMessageDTO;
import com.hotelmanagementsystem.dto.AdminReplyDTO;
import com.hotelmanagementsystem.service.ContactMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contact-messages")
public class ContactMessageController {

    private final ContactMessageService contactMessageService;

    @Autowired
    public ContactMessageController(ContactMessageService contactMessageService) {
        this.contactMessageService = contactMessageService;
    }

    @PostMapping
    public ResponseEntity<ContactMessageDTO> submitMessage(@RequestBody CreateContactMessageDTO messageDTO) {
        ContactMessageDTO savedMessage = contactMessageService.saveMessage(messageDTO);
        return ResponseEntity.ok(savedMessage);
    }

    @GetMapping
    public ResponseEntity<List<ContactMessageDTO>> getAllMessages() {
        List<ContactMessageDTO> messages = contactMessageService.getAllMessages();
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/unreplied")
    public ResponseEntity<List<ContactMessageDTO>> getUnrepliedMessages() {
        List<ContactMessageDTO> messages = contactMessageService.getUnrepliedMessages();
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/urgent")
    public ResponseEntity<List<ContactMessageDTO>> getUrgentMessages() {
        List<ContactMessageDTO> messages = contactMessageService.getUrgentMessages();
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContactMessageDTO> getMessageById(@PathVariable Long id) {
        ContactMessageDTO message = contactMessageService.getMessageById(id);
        return ResponseEntity.ok(message);
    }

    @PostMapping("/{id}/reply")
    public ResponseEntity<ContactMessageDTO> replyToMessage(
            @PathVariable Long id,
            @RequestBody AdminReplyDTO replyDTO) {
        ContactMessageDTO updatedMessage = contactMessageService.replyToMessage(id, replyDTO.getReply());
        return ResponseEntity.ok(updatedMessage);
    }

    @GetMapping("/count/unreplied")
    public ResponseEntity<Long> getUnrepliedCount() {
        long count = contactMessageService.getUnrepliedCount();
        return ResponseEntity.ok(count);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id) {
        contactMessageService.deleteMessage(id);
        return ResponseEntity.noContent().build();
    }

    // Fallback delete for clients that cannot send DELETE
    @PostMapping("/{id}/delete")
    public ResponseEntity<Void> deleteMessageFallback(@PathVariable Long id) {
        contactMessageService.deleteMessage(id);
        return ResponseEntity.noContent().build();
    }
}
