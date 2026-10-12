package com.laxmiclinic.controller.publicapi;

import com.laxmiclinic.dto.request.HomeVisitBookingRequest;
import com.laxmiclinic.dto.response.ApiResponse;
import com.laxmiclinic.model.HomeVisit;
import com.laxmiclinic.service.HomeVisitService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/home-visits")
public class PublicHomeVisitController {

    private final HomeVisitService homeVisitService;

    public PublicHomeVisitController(HomeVisitService homeVisitService) {
        this.homeVisitService = homeVisitService;
    }

    @PostMapping("/book")
    public ResponseEntity<ApiResponse<HomeVisit>> bookHomeVisit(@Valid @RequestBody HomeVisitBookingRequest request) {
        HomeVisit booked = homeVisitService.bookHomeVisit(request);
        return new ResponseEntity<>(ApiResponse.ok("Home visit request submitted successfully! Our clinic team will confirm your slot shortly.", booked), HttpStatus.CREATED);
    }

    @GetMapping("/slots")
    public ResponseEntity<ApiResponse<List<String>>> getAvailableSlots() {
        List<String> slots = homeVisitService.getAvailableTimeSlots();
        return ResponseEntity.ok(ApiResponse.ok("Available home visit time slots retrieved", slots));
    }

    @GetMapping("/track/{mobile}")
    public ResponseEntity<ApiResponse<List<HomeVisit>>> trackRequests(@PathVariable String mobile) {
        List<HomeVisit> visits = homeVisitService.getHomeVisitsByMobile(mobile);
        return ResponseEntity.ok(ApiResponse.ok("Home visit requests retrieved for " + mobile, visits));
    }
}
