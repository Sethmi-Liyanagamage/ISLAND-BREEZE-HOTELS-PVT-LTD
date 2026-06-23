package com.hotelmanagementsystem.service;

import com.hotelmanagementsystem.dto.CardDto;
import com.hotelmanagementsystem.dto.CreateCardDto;
import com.hotelmanagementsystem.dto.UpdateCardDto;
import com.hotelmanagementsystem.model.Card;
import com.hotelmanagementsystem.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CardService {

    @Autowired
    private CardRepository cardRepository;

    public List<CardDto> getUserCards(String userId) {
        List<Card> cards = cardRepository.findByUserId(userId);
        return cards.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<CardDto> getCardById(String cardId, String userId) {
        Optional<Card> card = cardRepository.findByCardIdAndUserId(cardId, userId);
        return card.map(this::convertToDto);
    }

    public Optional<CardDto> getDefaultCard(String userId) {
        Optional<Card> card = cardRepository.findByUserIdAndIsDefaultTrue(userId);
        return card.map(this::convertToDto);
    }

    @Transactional
    public CardDto createCard(String userId, CreateCardDto createCardDto) {
        // Validate expiry date
        if (!isValidExpiryDate(createCardDto.getExpiryDate())) {
            throw new IllegalArgumentException("Card has expired or expiry date is invalid");
        }

        // Check if card already exists for this user
        if (cardRepository.existsByUserIdAndCardNumber(userId, createCardDto.getCardNumber())) {
            throw new IllegalArgumentException("Card already exists for this user");
        }

        Card card = new Card();
        card.setUserId(userId);
        card.setCardNumber(createCardDto.getCardNumber());
        card.setCardHolderName(createCardDto.getCardHolderName());
        card.setCvv(createCardDto.getCvv());
        card.setExpiryDate(createCardDto.getExpiryDate());
        card.setCardType(createCardDto.getCardType());
        card.setDefault(createCardDto.isDefault());

        // If this is the first card or set as default, make it default
        long cardCount = cardRepository.countByUserId(userId);
        if (cardCount == 0 || createCardDto.isDefault()) {
            // Clear other default cards first
            cardRepository.clearDefaultCards(userId);
            card.setDefault(true);
        }

        Card savedCard = cardRepository.save(card);
        return convertToDto(savedCard);
    }

    @Transactional
    public CardDto updateCard(String cardId, String userId, UpdateCardDto updateCardDto) {
        Card card = cardRepository.findByCardIdAndUserId(cardId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Card not found"));

        // Update fields if provided
        if (updateCardDto.getCardHolderName() != null) {
            card.setCardHolderName(updateCardDto.getCardHolderName());
        }
        if (updateCardDto.getCvv() != null) {
            card.setCvv(updateCardDto.getCvv());
        }
        if (updateCardDto.getExpiryDate() != null) {
            if (!isValidExpiryDate(updateCardDto.getExpiryDate())) {
                throw new IllegalArgumentException("Card has expired or expiry date is invalid");
            }
            card.setExpiryDate(updateCardDto.getExpiryDate());
        }
        if (updateCardDto.getCardType() != null) {
            card.setCardType(updateCardDto.getCardType());
        }
        if (updateCardDto.getIsDefault() != null && updateCardDto.getIsDefault()) {
            // Clear other default cards first
            cardRepository.clearDefaultCards(userId);
            card.setDefault(true);
        }

        Card savedCard = cardRepository.save(card);
        return convertToDto(savedCard);
    }

    @Transactional
    public void deleteCard(String cardId, String userId) {
        Card card = cardRepository.findByCardIdAndUserId(cardId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Card not found"));

        boolean wasDefault = card.isDefault();
        cardRepository.delete(card);

        // If deleted card was default, set another card as default
        if (wasDefault) {
            List<Card> remainingCards = cardRepository.findByUserId(userId);
            if (!remainingCards.isEmpty()) {
                Card newDefaultCard = remainingCards.get(0);
                newDefaultCard.setDefault(true);
                cardRepository.save(newDefaultCard);
            }
        }
    }

    @Transactional
    public CardDto setDefaultCard(String cardId, String userId) {
        Card card = cardRepository.findByCardIdAndUserId(cardId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Card not found"));

        // Clear other default cards first
        cardRepository.clearDefaultCards(userId);
        
        // Set this card as default
        card.setDefault(true);
        Card savedCard = cardRepository.save(card);
        
        return convertToDto(savedCard);
    }

    private CardDto convertToDto(Card card) {
        CardDto dto = new CardDto();
        dto.setCardId(card.getCardId());
        dto.setUserId(card.getUserId());
        dto.setCardNumber(maskCardNumber(card.getCardNumber()));
        dto.setCardHolderName(card.getCardHolderName());
        dto.setExpiryDate(card.getExpiryDate());
        dto.setCardType(card.getCardType());
        dto.setDefault(card.isDefault());
        dto.setCreatedAt(card.getCreatedAt() != null ? card.getCreatedAt().toString() : null);
        dto.setUpdatedAt(card.getUpdatedAt() != null ? card.getUpdatedAt().toString() : null);
        return dto;
    }

    private String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 4) {
            return "****";
        }
        String lastFour = cardNumber.substring(cardNumber.length() - 4);
        return "****" + lastFour;
    }

    private boolean isValidExpiryDate(String expiryDate) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
            YearMonth expiry = YearMonth.parse(expiryDate, formatter);
            YearMonth current = YearMonth.now();
            return !expiry.isBefore(current);
        } catch (Exception e) {
            return false;
        }
    }
}
