package com.chuwa.redbook.controller;

import com.chuwa.redbook.batch.ReportHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author b1go
 * @date 3/3/23 12:06 AM
 */
@RestController
@RequestMapping("/api/v1/reports")
public class ReportController {

    @Autowired
    ReportHandler reportHandler;

    @GetMapping()
    public ResponseEntity<String> getReport() {
        reportHandler.getLast24HoursReport();
        return ResponseEntity.ok("check log");
    }
}
