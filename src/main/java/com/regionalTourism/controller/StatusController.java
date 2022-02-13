package com.regionalTourism.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StatusController {
    @GetMapping("/status")
    public ResponseEntity<String> getStatus() {
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
}
