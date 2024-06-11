package elevatorsManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import elevatorsManagement.model.RefreshToken;
import elevatorsManagement.model.User;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByUser(User user);

    @Query(value = "SELECT token from refreshtoken where user_id = ?1", nativeQuery = true)
    String findByUser(UUID userId);
}
