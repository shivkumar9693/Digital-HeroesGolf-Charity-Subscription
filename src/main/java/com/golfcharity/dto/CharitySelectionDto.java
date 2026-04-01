package com.golfcharity.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CharitySelectionDto {
    @NotNull(message = "Charity is required")
    private Long charityId;

    @NotNull(message = "Percentage is required")
    @Min(value = 10, message = "Minimum contribution is 10%")
    private Double percentage;
}
