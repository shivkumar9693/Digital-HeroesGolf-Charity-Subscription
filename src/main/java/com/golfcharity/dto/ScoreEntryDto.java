package com.golfcharity.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class ScoreEntryDto {
    @NotNull(message = "Score is required")
    @Min(value = 1, message = "Score must be between 1 and 45")
    @Max(value = 45, message = "Score must be between 1 and 45")
    private Integer score;

    @NotNull(message = "Date is required")
    private LocalDate scoreDate;
}
