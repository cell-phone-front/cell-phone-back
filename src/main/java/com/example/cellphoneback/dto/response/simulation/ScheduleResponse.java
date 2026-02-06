package com.example.cellphoneback.dto.response.simulation;

import java.time.LocalDate;

public record ScheduleResponse(String team, LocalDate date, String shift) {}