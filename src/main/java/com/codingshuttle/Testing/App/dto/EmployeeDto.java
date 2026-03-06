package com.codingshuttle.Testing.App.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor

public class EmployeeDto {
    private Long id;
    private String email;
    private String name;
    private Long salary;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof EmployeeDto that)) return false;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getEmail(), that.getEmail()) && Objects.equals(getName(), that.getName()) && Objects.equals(getSalary(), that.getSalary());
    }


    @Override
    public int hashCode() {
        return Objects.hash(getId(), getEmail(), getName(), getSalary());
    }

//Alt+Insert(getters and setters) and here we can say that you can verify if something is equal if all the four fields are equal.All these four attributed have to be equal for this employeeDto to be equal to another object.We have to generate equals and hashcode and not getters and setters


}
