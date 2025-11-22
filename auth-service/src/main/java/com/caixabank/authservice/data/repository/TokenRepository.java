package com.caixabank.authservice.data.repository;

import com.caixabank.authservice.data.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByToken(String token);

    @Query("""
        SELECT t FROM Token t
        WHERE t.user.id = :userId
          AND (t.expired = false AND t.revoked = false)
    """)
    List<Token> findAllValidTokensByUserId(@Param("userId") Long id);
}
