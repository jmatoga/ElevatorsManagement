package elevatorsManagement.dto;

import elevatorsManagement.model.EDirection;
import lombok.*;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ElevatorDTO {
    private UUID id;
    private int number;
    private int currentFloor;
    private List<Integer> destinationsFloor;
    private EDirection currentDirection;
    private String status;
    private int usersCount;
    private List<UUID> usersInsideElevator;
}
