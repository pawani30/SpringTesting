package com.codingshuttle.Testing.App.entities;

import jakarta.persistence.*;
import lombok.*;

//@Getter
//@Setter
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity

public class Employee {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

@Column(unique = true)
private String email;

private String name;

private Long salary;


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
}
