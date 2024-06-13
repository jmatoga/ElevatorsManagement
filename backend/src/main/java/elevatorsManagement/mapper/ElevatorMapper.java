package elevatorsManagement.mapper;

import elevatorsManagement.dto.ElevatorDTO;
import elevatorsManagement.model.Elevator;
import elevatorsManagement.repository.ElevatorRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public abstract class ElevatorMapper {
    @Autowired
    private ElevatorRepository elevatorRepository;

//    @Named("mapToElevatorsDTO")
//    @Mapping(target = "usersCount", expression = "java(getUsersCount(elevator))")
//    public abstract List<ElevatorDTO> mapToElevatorsDTO(List<Elevator> elevators);

    @Named("mapToElevatorDTO")
    @Mapping(target = "usersCount", expression = "java(getUsersCount(elevator))")
    public abstract ElevatorDTO mapToElevatorDTO(Elevator elevator);


    public List<ElevatorDTO> mapToElevatorsDTO(List<Elevator> elevators) {
        if (elevators == null) {
            return null;
        }

        List<ElevatorDTO> list = new ArrayList<ElevatorDTO>(elevators.size());
        for (Elevator elevator : elevators) {
            list.add(elevatorToElevatorDTO(elevator));
        }

        return list;
    }

    protected ElevatorDTO elevatorToElevatorDTO(Elevator elevator) {
        if ( elevator == null ) {
            return null;
        }

        ElevatorDTO elevatorDTO = new ElevatorDTO();

        elevatorDTO.setId( elevator.getId() );
        if ( elevator.getNumber() != null ) {
            elevatorDTO.setNumber( elevator.getNumber() );
        }
        if ( elevator.getCurrentFloor() != null ) {
            elevatorDTO.setCurrentFloor( elevator.getCurrentFloor() );
        }
        if ( elevator.getCurrentDirection() != null ) {
            elevatorDTO.setCurrentDirection( elevator.getCurrentDirection() );
        }
        if ( elevator.getDestinationsFloor() != null ) {
            elevatorDTO.setDestinationsFloor( elevator.getDestinationsFloor() );
        }
        if ( elevator.getStatus() != null ) {
            elevatorDTO.setStatus( elevator.getStatus().name() );
        }
        elevatorDTO.setUsersCount(getUsersCount(elevator));

        return elevatorDTO;
    }

    int getUsersCount(Elevator elevator) {
        return elevatorRepository.countUsersInElevator(elevator.getId());
    }
}
