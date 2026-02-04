package com.codingshuttle.Testing.App.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor

public class EmployeeDto {
    private Long id;
    private String email;
    private String name;
    private Long salary;
}
