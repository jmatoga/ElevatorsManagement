package elevatorsManagement.controller;

import elevatorsManagement.dto.ElevatorDTO;
import elevatorsManagement.exception.CurrentUserNotAuthenticatedException;
import elevatorsManagement.mapper.ElevatorMapper;
import elevatorsManagement.service.ElevatorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/elevator")
@Log4j2
public class ElevatorController {

    private final ElevatorService elevatorService;
    private final ElevatorMapper elevatorMapper;

    @GetMapping("/all")
    public List<ElevatorDTO> getAllElevators() {
        return elevatorMapper.mapToElevatorsDTO(elevatorService.getAllElevators());
    }

    @PostMapping("/update/{id}")
    public ElevatorDTO updateElevator(@PathVariable UUID id) throws CurrentUserNotAuthenticatedException {
        return elevatorMapper.mapToElevatorDTO(elevatorService.updateElevator(id));
    }

    @PostMapping("/floor/{id}")
    public ElevatorDTO updateFloor(@PathVariable UUID id, @RequestBody int floor) throws CurrentUserNotAuthenticatedException {
        return elevatorMapper.mapToElevatorDTO(elevatorService.updateElevatorFloor(id, floor));
    }
}
