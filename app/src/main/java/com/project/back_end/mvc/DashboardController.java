package com.project.back_end.mvc;

import java.util.Map;

@Controller
public class DashboardController {

    @Autowired
    private Service service;

    @GetMapping("/adminDashboard/{token}")
    public String adminDashboard(@PathVariable String token){
        Map<String, Object> validationResult = service.validateToken(token, "admin");

        if(validationResult == null || validationResult.isEmpty()){
            return "admin/adminDashboard";
        }

        return "redirect:/";

    }

    @GetMapping("/doctorDashboard/{token}")
    public String doctorDashboard(@PathVariable String token){
        Map<String, Object> validationResult = service.validateToken(token, "admin");

        if(validationResult == null || validationResult.isEmpty()){
            return "doctor/doctorDashboard";
        }

        return "redirect:/";

    }
}
