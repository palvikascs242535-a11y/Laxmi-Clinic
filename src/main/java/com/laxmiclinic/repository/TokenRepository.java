package com.laxmiclinic.repository;

import com.laxmiclinic.model.Token;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends MongoRepository<Token, String> {
    
    List<Token> findByTokenDateOrderByTokenNumberAsc(String tokenDate);
    
    List<Token> findByTokenDateAndStatusOrderByTokenNumberAsc(String tokenDate, Token.TokenStatus status);
    
    Optional<Token> findByTokenDateAndTokenNumber(String tokenDate, int tokenNumber);
    
    Optional<Token> findFirstByTokenDateOrderByTokenNumberDesc(String tokenDate);
    
    Optional<Token> findFirstByTokenDateAndStatusOrderByTokenNumberAsc(String tokenDate, Token.TokenStatus status);
    
    Optional<Token> findFirstByTokenDateAndStatusOrderByCalledAtDesc(String tokenDate, Token.TokenStatus status);

    List<Token> findByTokenDateAndPatientPhoneOrderByTokenNumberDesc(String tokenDate, String patientPhone);

    long countByTokenDate(String tokenDate);
    
    long countByTokenDateAndStatus(String tokenDate, Token.TokenStatus status);

    long countByTokenDateAndTokenNumberLessThanAndStatus(String tokenDate, int tokenNumber, Token.TokenStatus status);
}
