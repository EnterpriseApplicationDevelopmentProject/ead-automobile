package com.example.ead_backend.contoller.employee;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {
    // public ResponseEntity<Void> addEmployee(@RequestBody EmployeeDTO employeeDTO){
    //     employeeService.addEmployee(employeeDTO);
    //     return ResponseEntity.status(HttpStatus.CREATED).build();
    // }
}
