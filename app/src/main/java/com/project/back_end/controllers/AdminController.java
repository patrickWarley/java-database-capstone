
package com.project.back_end.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.back_end.models.Admin;
import com.project.back_end.services.BaseService;

@RestController
@RequestMapping("${api.path}admin")
public class AdminController {

  private final BaseService service;

  @Autowired
  public AdminController(BaseService service) {
    this.service = service;
  }

  @PostMapping
  public ResponseEntity<Map<String, String>> adminLogin(@RequestParam Admin admin) {
    return service.validateAdmin(admin);
  }
}
