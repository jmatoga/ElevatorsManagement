package elevatorsManagement.repository;

import elevatorsManagement.model.Elevator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ElevatorRepository extends JpaRepository<Elevator, UUID> {

    @Query(value = "SELECT COUNT(*) FROM users_in_elevators WHERE elevator_id = ?1", nativeQuery = true)
    int countUsersInElevator(UUID id);
}
