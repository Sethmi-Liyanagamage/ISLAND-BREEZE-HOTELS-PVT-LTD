package com.hotelmanagementsystem.controller;

import com.hotelmanagementsystem.dto.CardDto;
import com.hotelmanagementsystem.dto.CreateCardDto;
import com.hotelmanagementsystem.dto.UpdateCardDto;
import com.hotelmanagementsystem.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/cards")
@CrossOrigin(origins = "*")
public class CardController {

    @Autowired
    private CardService cardService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CardDto>> getUserCards(@PathVariable String userId) {
        try {
            List<CardDto> cards = cardService.getUserCards(userId);
            return ResponseEntity.ok(cards);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{cardId}/user/{userId}")
    public ResponseEntity<CardDto> getCard(@PathVariable String cardId, @PathVariable String userId) {
        try {
            return cardService.getCardById(cardId, userId)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/default/user/{userId}")
    public ResponseEntity<CardDto> getDefaultCard(@PathVariable String userId) {
        try {
            return cardService.getDefaultCard(userId)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<?> createCard(@PathVariable String userId, @Valid @RequestBody CreateCardDto createCardDto) {
        try {
            CardDto createdCard = cardService.createCard(userId, createCardDto);
            return ResponseEntity.ok(createdCard);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to create card: " + e.getMessage());
        }
    }

    @PutMapping("/{cardId}/user/{userId}")
    public ResponseEntity<?> updateCard(@PathVariable String cardId, @PathVariable String userId, 
                                       @Valid @RequestBody UpdateCardDto updateCardDto) {
        try {
            CardDto updatedCard = cardService.updateCard(cardId, userId, updateCardDto);
            return ResponseEntity.ok(updatedCard);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to update card: " + e.getMessage());
        }
    }

    @DeleteMapping("/{cardId}/user/{userId}")
    public ResponseEntity<?> deleteCard(@PathVariable String cardId, @PathVariable String userId) {
        try {
            cardService.deleteCard(cardId, userId);
            return ResponseEntity.ok().body("Card deleted successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to delete card: " + e.getMessage());
        }
    }

    @PutMapping("/{cardId}/user/{userId}/default")
    public ResponseEntity<?> setDefaultCard(@PathVariable String cardId, @PathVariable String userId) {
        try {
            CardDto updatedCard = cardService.setDefaultCard(cardId, userId);
            return ResponseEntity.ok(updatedCard);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to set default card: " + e.getMessage());
        }
    }
}
