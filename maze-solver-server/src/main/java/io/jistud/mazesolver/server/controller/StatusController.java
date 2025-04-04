package io.jistud.mazesolver.server.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller providing basic server status information.
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // Allow requests from any origin for now - restrict in production
public class StatusController {

    /**
     * Returns a simple message indicating the server is running.
     * @return Status message
     */
    @GetMapping("/status")
    public String getStatus() {
        return "maze-solver-server is running";
    }
}
