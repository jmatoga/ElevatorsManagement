package elevatorsManagement.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Entity
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "elevators")
@Log4j2
public class Elevator {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private Integer number;

    private Integer currentFloor;

    @ElementCollection(targetClass=Integer.class)
    @CollectionTable(name="destinations_floor")
    @Column(name="floor")
    private List<Integer> destinationsFloor = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private EDirection currentDirection;

    @Enumerated(EnumType.STRING)
    private EStatus status;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_in_elevators",
            joinColumns = @JoinColumn(name = "elevator_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> usersInElevator;

}
