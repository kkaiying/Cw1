package uk.ac.ed.acp.cw2.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public class MedDispatchRecDTO {
    @NotNull
    public int id;
    @NotNull
    public LocalDate date;
    @NotNull
    public LocalTime time;
    @NotNull
    @Valid
    public RequirementsDTO requirements;
}
