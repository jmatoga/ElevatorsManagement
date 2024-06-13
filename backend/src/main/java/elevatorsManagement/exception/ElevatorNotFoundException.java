package elevatorsManagement.exception;

import java.util.UUID;

public class ElevatorNotFoundException extends RuntimeException {
    public ElevatorNotFoundException(UUID id) {
        super("Elevator with id: " + id + " does not exists!");
    }

}
