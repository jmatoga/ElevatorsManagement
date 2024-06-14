package elevatorsManagement.service;

import elevatorsManagement.exception.CurrentUserNotAuthenticatedException;
import elevatorsManagement.exception.ElevatorNotFoundException;
import elevatorsManagement.model.EDirection;
import elevatorsManagement.model.EStatus;
import elevatorsManagement.model.Elevator;
import elevatorsManagement.model.User;
import elevatorsManagement.repository.ElevatorRepository;
import elevatorsManagement.repository.UserRepository;
import elevatorsManagement.task.TaskService;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.hibernate.Hibernate;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Log4j2
public class ElevatorServiceImpl implements ElevatorService {
    private final ElevatorRepository elevatorRepository;
    private final UserService userService;
    private final TaskService taskService;
    private final UserRepository userRepository;

    public ElevatorServiceImpl(ElevatorRepository elevatorRepository, UserService userService, @Lazy TaskService taskService, UserRepository userRepository) {
        this.elevatorRepository = elevatorRepository;
        this.userService = userService;
        this.taskService = taskService;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public List<Elevator> getAllElevators() {
        return elevatorRepository.findAll();
    }

    @Override
    @Transactional
    public Elevator updateElevator(UUID id) throws CurrentUserNotAuthenticatedException {
        Elevator elevator = getElevator(id);
        User currentUser = userService.getCurrentUser();

        if (elevator.getUsersInElevator().isEmpty()) {
            if (currentUser.getCurrentFloor() > elevator.getCurrentFloor()) {
                elevator.setCurrentDirection(EDirection.UP);
                setDestinationsFloor(id, elevator, currentUser);
            } else if (currentUser.getCurrentFloor() < elevator.getCurrentFloor()) {
                elevator.setCurrentDirection(EDirection.DOWN);
                setDestinationsFloor(id, elevator, currentUser);
            } else {
                elevator.setStatus(EStatus.BUSY);
                elevatorRepository.save(elevator);
            }

            return elevator;
        } else {
            setDestinationsFloor(id, elevator, currentUser);
            return elevatorRepository.save(elevator);
        }
    }

    private void setDestinationsFloor(UUID id, Elevator elevator, User currentUser) {
        if(!elevator.getDestinationsFloor().contains(currentUser.getCurrentFloor()))
            elevator.getDestinationsFloor().add(currentUser.getCurrentFloor());

        elevator.setStatus(EStatus.BUSY);
        elevatorRepository.save(elevator);
        taskService.elevatorStep(id);
    }

    // wsiadam do windy
    @Override
    @Transactional
    public Elevator updateElevatorFloor(UUID id, Integer floor) throws CurrentUserNotAuthenticatedException {
        Elevator elevator = getElevator(id);
        User currentUser = userService.getCurrentUser();

        elevator.setStatus(EStatus.BUSY);
        currentUser.setInsideElevator(true);

        if (!elevator.getUsersInElevator().contains(currentUser)) {
            elevator.getUsersInElevator().add(currentUser);
            elevator.setUsersInElevator(elevator.getUsersInElevator());
        }
        currentUser.setDestinationFloor(floor);
        userRepository.save(currentUser);

        if (!elevator.getDestinationsFloor().contains(floor)) {
            List<Integer> floors = elevator.getDestinationsFloor();
            floors.add(floor);
            elevator.setDestinationsFloor(floors);
        }

        if (!Objects.equals(floor, elevator.getCurrentFloor())) {
            elevatorRepository.save(elevator);
            taskService.elevatorStep(id);
        }

        return elevatorRepository.save(elevator);
    }

    @Override
    @Transactional
    public void updateElevatorAfterMoving(UUID id) {
        Elevator elevator = getElevator(id);
        List<Integer> destinationsFloor = elevator.getDestinationsFloor();

        System.out.println("Destinations before moving: " + destinationsFloor);

        if (!destinationsFloor.isEmpty()) {

            Integer nextFloor = destinationsFloor.getFirst();
            elevator.setCurrentFloor(nextFloor);
            destinationsFloor.removeFirst();

            if(destinationsFloor.isEmpty())
                elevator.setCurrentDirection(EDirection.NONE);
            else if(nextFloor < destinationsFloor.getFirst())
                elevator.setCurrentDirection(EDirection.DOWN);
            else if (nextFloor > destinationsFloor.getFirst())
                elevator.setCurrentDirection(EDirection.UP);

            System.out.println("Elevator moved to floor: " + nextFloor);

            List<User> usersStillInElevator = new ArrayList<>();

            elevator.getUsersInElevator().forEach(user -> {
                if (user.getDestinationFloor().equals(elevator.getCurrentFloor())) {
                    System.out.println("User " + user.getName() + " reached destination floor: " + elevator.getCurrentFloor());
                    user.setCurrentFloor(elevator.getCurrentFloor());
                    user.setInsideElevator(false);
                    userRepository.save(user);
                } else {
                    usersStillInElevator.add(user);
                }
            });

            elevator.setUsersInElevator(usersStillInElevator);

            if (destinationsFloor.isEmpty()) {
                elevator.setStatus(EStatus.IDLE);
                elevator.setCurrentDirection(EDirection.NONE);
                elevatorRepository.save(elevator);
                taskService.elevatorStep(elevator.getId());
            }

            elevator.setDestinationsFloor(destinationsFloor);
            elevatorRepository.save(elevator);

            System.out.println("Elevator with number: " + elevator.getNumber() + " updated after moving.");
        } else {
            System.out.println("No destinations to move to for elevator with number: " + elevator.getNumber());
        }
    }


    @Override
    @Transactional
    public Elevator getElevator(UUID id) {
        Elevator elevator = elevatorRepository.findById(id)
                                    .orElseThrow(() -> new ElevatorNotFoundException(id));
        Hibernate.initialize(elevator.getDestinationsFloor()); // Initialize the collection
        return elevator;
    }
}
