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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
        System.out.println("5, " + elevator.getUsersInElevator());
        if (elevator.getUsersInElevator().isEmpty()) {
            if (currentUser.getCurrentFloor() > elevator.getCurrentFloor()) {
                elevator.setCurrentDirection(EDirection.UP);
                elevator.getDestinationsFloor().add(currentUser.getCurrentFloor());
                System.out.println("5, " + elevator.getDestinationsFloor());
                elevator.setStatus(EStatus.BUSY);
                elevatorRepository.save(elevator);
                taskService.elevatorStep(id);

            } else if (currentUser.getCurrentFloor() < elevator.getCurrentFloor()) {
                elevator.setCurrentDirection(EDirection.DOWN);
                elevator.getDestinationsFloor().add(currentUser.getCurrentFloor());
                System.out.println("5, " + elevator.getDestinationsFloor());
                elevator.setStatus(EStatus.BUSY);
                elevatorRepository.save(elevator);
                taskService.elevatorStep(id);
            } else {
                System.out.println("5.1");

                elevator.setStatus(EStatus.BUSY);
                elevatorRepository.save(elevator);
            }

            return elevator;
        }
        return null;
    }

    // wsiadam do windy
    @Override
    @Transactional
    public Elevator updateElevatorFloor(UUID id, Integer floor) throws CurrentUserNotAuthenticatedException {
        Elevator elevator = getElevator(id);
        User currentUser = userService.getCurrentUser();

        elevator.setStatus(EStatus.BUSY);

        //if(elevator.getUsersInElevator().isEmpty()) {
        if (!elevator.getUsersInElevator().contains(currentUser)) {
            elevator.getUsersInElevator().add(currentUser);
            elevator.setUsersInElevator(elevator.getUsersInElevator());
        }
        currentUser.setDestinationFloor(floor);
        userRepository.save(currentUser);

        if (!elevator.getDestinationsFloor().contains(floor)) {
            List<Integer> floors = elevator.getDestinationsFloor();
            System.out.println("4,  " + floors + " " + floor);
            floors.add(floor);
            elevator.setDestinationsFloor(floors);
        }

        System.out.println("4.1,  " + elevator.getDestinationsFloor());
        if (floor > elevator.getCurrentFloor()) {
            elevator.setCurrentDirection(EDirection.UP);
            elevatorRepository.save(elevator);
            taskService.elevatorStep(id);
        } else if (floor < elevator.getCurrentFloor()) {
            elevator.setCurrentDirection(EDirection.DOWN);
            elevatorRepository.save(elevator);
            taskService.elevatorStep(id);
        }

        System.out.println("1");
        return elevatorRepository.save(elevator);
        //}
        // return null;
    }

    //    @Override
//    @Transactional
//    public void updateElevatorAfterMoving(UUID id) {
//        Elevator elevator = getElevator(id);
//        System.out.println("3,  " + elevator.getDestinationsFloor());
//        elevator.setCurrentFloor(elevator.getDestinationsFloor().getFirst());
//        elevator.getDestinationsFloor().removeFirst();
//
//        elevator.getUsersInElevator().forEach(user -> {
//            System.out.println("usser"+ user.getName());
//            if(user.getDestinationFloor().equals(elevator.getCurrentFloor())) {
//                System.out.println("e"+elevator.getCurrentFloor());
//                user.setCurrentFloor(elevator.getCurrentFloor());
//                userRepository.save(user);
//                System.out.println("u"+user.getCurrentFloor());
//            }
//        });
//
//        if(elevator.getDestinationsFloor().isEmpty() && elevator.getCurrentFloor() == 0) {
//            elevator.setStatus(EStatus.IDLE);
//            elevator.setCurrentDirection(EDirection.NONE);
//        } else if(elevator.getDestinationsFloor().isEmpty() && elevator.getCurrentFloor() > 0) {
//            taskService.elevatorStep(elevator.getId());
//            elevator.setCurrentDirection(EDirection.DOWN);
//        }
//
//        elevatorRepository.save(elevator);
//        System.out.println("Elevator with number: "+elevator.getNumber()+" updated after moving UP");
//
//    }
    @Override
    @Transactional
    public void updateElevatorAfterMoving(UUID id) {
        Elevator elevator = getElevator(id);
        List<Integer> destinationsFloor = elevator.getDestinationsFloor();

        System.out.println("3, Destinations before moving: " + destinationsFloor);

        if (!destinationsFloor.isEmpty()) {
//        List<Integer> floorsBelow = new ArrayList<>();
//        List<Integer> floorsAbove = new ArrayList<>();
//        elevator.getDestinationsFloor().forEach(floor -> {
//            if (floor < elevator.getCurrentFloor()) {
//                floorsBelow.add(floor);
//            } else if (floor > elevator.getCurrentFloor()) {
//                floorsAbove.add(floor);
//            }
//        });
//
//        floorsBelow.sort(Collections.reverseOrder());
//        floorsAbove.sort(Integer::compareTo);
//
//        if(elevator.getCurrentDirection().equals(EDirection.UP)) {
//            List<Integer> floorsSorted = new ArrayList<>();
//            floorsSorted.addAll(floorsAbove);
//            floorsSorted.addAll(floorsBelow);
//            elevator.setDestinationsFloor(floorsSorted);
//        } else {
//            List<Integer> floorsSorted = new ArrayList<>();
//            floorsSorted.addAll(floorsBelow);
//            floorsSorted.addAll(floorsAbove);
//            elevator.setDestinationsFloor(floorsSorted);
//        }


//        if(elevator.getCurrentDirection().equals(EDirection.UP)) {
//            destinationsFloor.sort(Integer::compareTo);
//        } else {
//            destinationsFloor.sort((o1, o2) -> o2.compareTo(o1));
//        }


            Integer nextFloor = destinationsFloor.getFirst();
            elevator.setCurrentFloor(nextFloor);
            destinationsFloor.removeFirst();

            System.out.println("Elevator moved to floor: " + nextFloor);

            List<User> usersStillInElevator = new ArrayList<>();

            elevator.getUsersInElevator().forEach(user -> {
                if (user.getDestinationFloor().equals(elevator.getCurrentFloor())) {
                    System.out.println("User " + user.getName() + " reached destination floor: " + elevator.getCurrentFloor());
                    user.setCurrentFloor(elevator.getCurrentFloor());
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
