package elevatorsManagement.service;

import elevatorsManagement.model.RefreshToken;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenService {
    RefreshToken createRefreshToken(UUID userId);
    Optional<RefreshToken> findByToken(String token);
    RefreshToken verifyExpiration(RefreshToken token);
    void deleteByUserId(UUID userId);
    void deleteByToken(String token);
    String findByUser(String email);
}
