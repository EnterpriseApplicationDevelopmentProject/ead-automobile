package com.example.ead_backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ead_backend.dto.EmployeeCreateDTO;
import com.example.ead_backend.exceptions.EmployeeNotFoundException;
import com.example.ead_backend.service.EmployeeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeCreateDTO> getEmployeeById(@PathVariable Long id) {
        if(id == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try{
            EmployeeCreateDTO employeeDTO = employeeService.getEmployeeById(id);
            return new ResponseEntity<>(employeeDTO, HttpStatus.OK);
        }catch (EmployeeNotFoundException e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeCreateDTO> updateEmployee(
            @PathVariable Long id, 
            @RequestBody EmployeeCreateDTO dto) {

        if (id == null || dto == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try{
            EmployeeCreateDTO updated = employeeService.updateEmployee(id, dto);
            return new ResponseEntity<>(updated,HttpStatus.OK);
        }catch (EmployeeNotFoundException e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
