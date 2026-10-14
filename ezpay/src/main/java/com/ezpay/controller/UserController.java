package com.ezpay.controller;

import com.ezpay.entity.User;
import com.ezpay.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // USER REGISTRATION
    @PostMapping("/register")
    public String registerUser(@RequestBody User user) {
        return userService.registerUser(user);
    }

    // OTP VERIFICATION
    @PostMapping("/verify-otp")
    public String verifyOTP(@RequestBody User user) {
        return userService.verifyOTP(
                user.getMobile(),
                user.getOtp()
        );
    }

    // LOGIN
    @PostMapping("/login")
    public String loginUser(@RequestBody User user) {
        return userService.loginUser(
                user.getEmail(),
                user.getPassword()
        );
    }

    // PROFILE UPDATE
    @PutMapping("/{userId}/profile")
    public String updateProfile(
            @PathVariable Long userId,
            @RequestBody User user) {

        return userService.updateProfile(
                userId,
                user.getName(),
                user.getAddress(),
                user.getProfilePicture()
        );
    }

    // FORGOT PASSWORD
    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestBody User user) {
        return userService.forgotPassword(
                user.getEmail()
        );
    }

    // RESET PASSWORD
    @PostMapping("/reset-password/{token}")
    public String resetPassword(
            @PathVariable String token,
            @RequestBody User user) {

        return userService.resetPassword(
                token,
                user.getPassword()
        );
    }
}