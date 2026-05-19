package com.soufiane.Library.controller;

import com.soufiane.Library.DTO.StatsResponse;
import com.soufiane.Library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private BookService bookService;

    @GetMapping("/stats")
    public ResponseEntity<StatsResponse> getStats(){
        return new ResponseEntity<>(bookService.getStats(), HttpStatus.OK);
    }
}
