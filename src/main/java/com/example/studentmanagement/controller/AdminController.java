package com.example.studentmanagement.controller;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/test")
    public String adminTest(){

        return "Only admin can access this api";
    }

}
