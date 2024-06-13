package elevatorsManagement.task;

import elevatorsManagement.model.EDirection;
import elevatorsManagement.model.EStatus;
import elevatorsManagement.model.Elevator;
import elevatorsManagement.repository.ElevatorRepository;
import elevatorsManagement.service.ElevatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class TaskService {

    @Qualifier("taskExecutor")
    @Autowired
    private TaskExecutor taskExecutor;

    private final Lock lock = new ReentrantLock();

    private final ElevatorService elevatorService;
    private final ElevatorRepository elevatorRepository;

    public TaskService(ElevatorService elevatorService, ElevatorRepository elevatorRepository) {
        this.elevatorService = elevatorService;
        this.elevatorRepository = elevatorRepository;
    }

    public void elevatorStep(UUID id) {
        taskExecutor.execute(() -> {
            System.out.println("2");
            try {
                lock.lock();
                try {

//                List<Integer> floorsBelow = new ArrayList<>();
//                List<Integer> floorsAbove = new ArrayList<>();
//                elevator.getDestinationsFloor().forEach(floor -> {
//                    if (floor < elevator.getCurrentFloor()) {
//                        floorsBelow.add(floor);
//                    } else if (floor > elevator.getCurrentFloor()) {
//                        floorsAbove.add(floor);
//                    }
//                });
//
//                floorsBelow.sort(Collections.reverseOrder());
//                floorsAbove.sort(Integer::compareTo);
//
//                if(elevator.getCurrentDirection().equals(EDirection.UP)) {
//                    List<Integer> floorsSorted = new ArrayList<>();
//                    floorsSorted.addAll(floorsAbove);
//                    floorsSorted.addAll(floorsBelow);
//                    elevator.setDestinationsFloor(floorsSorted);
//                } else {
//                    List<Integer> floorsSorted = new ArrayList<>();
//                    floorsSorted.addAll(floorsBelow);
//                    floorsSorted.addAll(floorsAbove);
//                    elevator.setDestinationsFloor(floorsSorted);
//                }
//                elevatorRepository.save(elevator);


                    Thread.sleep(5000); // Opóźnienie 5 sekund
                    Elevator elevator = elevatorService.getElevator(id);
                    Integer currentFloor = elevator.getCurrentFloor();
                    if (!elevator.getDestinationsFloor().isEmpty() && elevator.getCurrentDirection().equals(EDirection.UP))
                        elevator.setCurrentFloor(currentFloor + 1);
                    else if (!elevator.getDestinationsFloor().isEmpty() && elevator.getCurrentDirection().equals(EDirection.DOWN))
                        elevator.setCurrentFloor(currentFloor - 1);
                    else if (elevator.getCurrentDirection().equals(EDirection.NONE) && elevator.getUsersInElevator().isEmpty()) {
                        // elevatorService.updateElevatorAfterMoving(id);
                        if (elevator.getCurrentFloor() == 0) {
                            elevator.setStatus(EStatus.IDLE);
                            elevator.setCurrentDirection(EDirection.NONE);
                            elevatorRepository.save(elevator);
                            return;
                        } else if (elevator.getDestinationsFloor().isEmpty()) {
                            elevator.setCurrentDirection(EDirection.DOWN);
                            elevator.setStatus(EStatus.BUSY);
//                        List<Integer> destinationsFloor = elevator.getDestinationsFloor();
//                        if(!destinationsFloor.contains(0))
//                            destinationsFloor.add(0);
                            elevator.setDestinationsFloor(new ArrayList<Integer>(Collections.singleton(0)));
                            elevatorRepository.save(elevator);
                            elevatorStep(elevator.getId());
                        }
                    }

                    elevatorRepository.save(elevator);

                    if (!elevator.getDestinationsFloor().isEmpty() && currentFloor.equals(elevator.getDestinationsFloor().getFirst()))
                        elevatorService.updateElevatorAfterMoving(id);
                    else
                        elevatorStep(id);

                    System.out.println("2.1");
                } finally {
                    lock.unlock();
            }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }

}
