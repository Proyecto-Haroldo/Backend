package itm.proyectoharoldo.backend.Controllers;

import itm.proyectoharoldo.backend.Models.DTO.Schedule.CreateScheduleRequest;
import itm.proyectoharoldo.backend.Models.DTO.Schedule.ScheduleResponseDTO;
import itm.proyectoharoldo.backend.Services.ScheduleService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/schedules")
@AllArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @GetMapping("/my")
    public List<ScheduleResponseDTO> listMine(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return scheduleService.listMySchedules(email, from, to);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ScheduleResponseDTO create(@Valid @RequestBody CreateScheduleRequest body) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return scheduleService.create(email, body);
    }
}
