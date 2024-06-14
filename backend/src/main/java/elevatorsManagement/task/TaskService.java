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
            try {
                lock.lock();
                try {
                    Elevator elevator1 = elevatorService.getElevator(id);

                    if(elevator1.getDestinationsFloor().size() >= 2) {

                        // Ustawienie kolejnych pięter w odpowiedniej kolejności w zależności od tego które są bliżej
                        List<Integer> floorsBelow = new ArrayList<>();
                        List<Integer> floorsAbove = new ArrayList<>();
                        elevator1.getDestinationsFloor().forEach(floor -> {
                            if (floor < elevator1.getCurrentFloor()) {
                                floorsBelow.add(floor);
                            } else if (floor > elevator1.getCurrentFloor()) {
                                floorsAbove.add(floor);
                            }
                        });

                        floorsBelow.sort(Collections.reverseOrder());
                        floorsAbove.sort(Integer::compareTo);

                        List<Integer> floorsSorted = new ArrayList<>();

                        if (elevator1.getCurrentDirection().equals(EDirection.UP)) {
                            floorsSorted.addAll(floorsAbove);
                            floorsSorted.addAll(floorsBelow);
                        } else if (elevator1.getCurrentDirection().equals(EDirection.DOWN)) {
                            floorsSorted.addAll(floorsBelow);
                            floorsSorted.addAll(floorsAbove);
                        }

                        // Sortowanie na podstawie szacowanego czasu oczekiwania
                        floorsSorted.sort((floor1, floor2) -> {
                            int waitTime1 = calculateWaitTime(floor1, elevator1.getCurrentFloor());
                            int waitTime2 = calculateWaitTime(floor2, elevator1.getCurrentFloor());
                            return Integer.compare(waitTime1, waitTime2);
                        });

                        elevator1.setDestinationsFloor(floorsSorted);
                        elevatorRepository.save(elevator1);
                    }

                    Thread.sleep(5000); // Opóźnienie 5 sekund
                    Elevator elevator = elevatorService.getElevator(id);
                    Integer currentFloor = elevator.getCurrentFloor();

                    if(elevator.getDestinationsFloor().isEmpty())
                        elevator.setCurrentDirection(EDirection.NONE);
                    else if (elevator.getDestinationsFloor().getFirst() > currentFloor)
                        elevator.setCurrentDirection(EDirection.UP);
                    else if(elevator.getDestinationsFloor().getFirst() < currentFloor)
                        elevator.setCurrentDirection(EDirection.DOWN);

                    if (!elevator.getDestinationsFloor().isEmpty() && elevator.getCurrentDirection().equals(EDirection.UP))
                        elevator.setCurrentFloor(currentFloor + 1);
                    else if (!elevator.getDestinationsFloor().isEmpty() && elevator.getCurrentDirection().equals(EDirection.DOWN))
                        elevator.setCurrentFloor(currentFloor - 1);
                    else if (elevator.getCurrentDirection().equals(EDirection.NONE) && elevator.getUsersInElevator().isEmpty()) {
                        if (elevator.getCurrentFloor() == 0) {
                            elevator.setStatus(EStatus.IDLE);
                            elevator.setCurrentDirection(EDirection.NONE);
                            elevatorRepository.save(elevator);
                            return;
                        } else if (elevator.getDestinationsFloor().isEmpty()) {
                            elevator.setCurrentDirection(EDirection.DOWN);
                            elevator.setStatus(EStatus.BUSY);
                            elevator.setDestinationsFloor(new ArrayList<Integer>(Collections.singleton(0)));
                            elevatorRepository.save(elevator);
                            elevatorStep(elevator.getId());
                        }
                    }

                    elevatorRepository.save(elevator);

                    if (!elevator.getDestinationsFloor().isEmpty() && elevator.getCurrentFloor().equals(elevator.getDestinationsFloor().getFirst()))
                        elevatorService.updateElevatorAfterMoving(id);
                    else
                        elevatorStep(id);

                } finally {
                    lock.unlock();
            }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }

    private int calculateWaitTime(int floor, int currentFloor) {
        return Math.abs(floor - currentFloor);
    }

}
