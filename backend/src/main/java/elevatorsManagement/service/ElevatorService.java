package elevatorsManagement.service;

import elevatorsManagement.exception.CurrentUserNotAuthenticatedException;
import elevatorsManagement.model.Elevator;

import java.util.List;
import java.util.UUID;

public interface ElevatorService {
    List<Elevator> getAllElevators();

    Elevator updateElevator(UUID id) throws CurrentUserNotAuthenticatedException;

    Elevator updateElevatorFloor(UUID id, Integer floor) throws CurrentUserNotAuthenticatedException;

    void updateElevatorAfterMoving(UUID id);

    Elevator getElevator(UUID id);
}
