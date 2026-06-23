package com.hotelmanagementsystem.repository;

import com.hotelmanagementsystem.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, String> {
    
    List<Card> findByUserId(String userId);
    
    Optional<Card> findByUserIdAndIsDefaultTrue(String userId);
    
    Optional<Card> findByCardIdAndUserId(String cardId, String userId);
    
    boolean existsByUserIdAndCardNumber(String userId, String cardNumber);
    
    @Modifying
    @Transactional
    @Query("UPDATE Card c SET c.isDefault = false WHERE c.userId = :userId")
    void clearDefaultCards(@Param("userId") String userId);
    
    @Query("SELECT COUNT(c) FROM Card c WHERE c.userId = :userId")
    long countByUserId(@Param("userId") String userId);
}
