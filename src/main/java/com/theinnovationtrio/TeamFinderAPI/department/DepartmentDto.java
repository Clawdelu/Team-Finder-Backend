package com.theinnovationtrio.TeamFinderAPI.department;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class DepartmentDto {

    @NotNull(message = "This field is null.")
    private String departmentName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UUID departmentManager;
}
