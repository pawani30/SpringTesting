//package main.java.com.codingshuttle.Testing.App.dto;

package com.codingshuttle.Testing.App.dto;

import lombok.*;

import java.util.Objects;

@Builder
@Data
//@Getter
//@Setter
@NoArgsConstructor
@AllArgsConstructor

public class EmployeeDto {
    private Long id;
    private String email;
    private String name;
    private Long salary;

//    public Long id;
//    public String email;
//    public String name;
//    public Long salary;
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public Long getSalary() {
//        return salary;
//    }
//
//    public void setSalary(Long salary) {
//        this.salary = salary;
//    }

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
