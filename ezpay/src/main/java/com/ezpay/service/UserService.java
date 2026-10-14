package com.ezpay.service;

import com.ezpay.dao.UserDAO;
import com.ezpay.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
public class UserService {

    private static final Logger logger =
            LoggerFactory.getLogger(UserService.class);

    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    // ==========================================
    // USER STORY 1.1
    // REGISTRATION
    // ==========================================

    public String registerUser(User user) {

        logger.info("Registration request received for email: {}",
                user.getEmail());

        if (userDAO.existsByEmail(user.getEmail())) {

            logger.warn("Registration failed - email already registered: {}",
                    user.getEmail());

            return "Email already registered";
        }

        if (userDAO.existsByMobile(user.getMobile())) {

            logger.warn("Registration failed - mobile already registered");

            return "Mobile number already registered";
        }

        String otp = String.valueOf(
                100000 + new Random().nextInt(900000)
        );

        user.setOtp(otp);

        user.setOtpExpiry(
                LocalDateTime.now().plusMinutes(5)
        );

        userDAO.saveUser(user);

        logger.info("User registered successfully for email: {}",
                user.getEmail());

        logger.info("OTP generated successfully for mobile: {}",
                user.getMobile());

        return "Registration successful. OTP sent to mobile.";
    }

    // ==========================================
    // USER STORY 1.1
    // OTP VERIFICATION
    // ==========================================

    public String verifyOTP(String mobile, String otp) {

        logger.info("OTP verification request received");

        Optional<User> optionalUser =
                userDAO.findByMobile(mobile);

        if (optionalUser.isEmpty()) {

            logger.warn("OTP verification failed - user not found");

            return "User not found";
        }

        User user = optionalUser.get();

        if (user.getOtp() == null ||
                !user.getOtp().equals(otp)) {

            logger.warn("OTP verification failed - invalid OTP");

            return "Invalid OTP";
        }

        if (user.getOtpExpiry() == null ||
                user.getOtpExpiry()
                        .isBefore(LocalDateTime.now())) {

            logger.warn("OTP verification failed - OTP expired");

            return "OTP expired";
        }

        user.setOtp(null);
        user.setOtpExpiry(null);

        userDAO.saveUser(user);

        logger.info("OTP verified successfully for user ID: {}",
                user.getUserId());

        return "Account verified successfully";
    }

    // ==========================================
    // USER STORY 1.1
    // LOGIN
    // ==========================================

    public String loginUser(
            String emailOrMobile,
            String password) {

        logger.info("Login request received");

        Optional<User> optionalUser =
                userDAO.findByEmailOrMobile(emailOrMobile);

        if (optionalUser.isEmpty()) {

            logger.warn("Login failed - user not found");

            return "Invalid email/mobile or password";
        }

        User user = optionalUser.get();

        if (!user.getPassword().equals(password)) {

            logger.warn("Login failed - invalid password for user ID: {}",
                    user.getUserId());

            return "Invalid email/mobile or password";
        }

        logger.info("Login successful for user ID: {}",
                user.getUserId());

        return "Login successful";
    }

    // ==========================================
    // USER STORY 1.2
    // PROFILE MANAGEMENT
    // ==========================================

    public String updateProfile(
            Long userId,
            String name,
            String address,
            String profilePicture) {

        logger.info("Profile update request received for user ID: {}",
                userId);

        Optional<User> optionalUser =
                userDAO.findById(userId);

        if (optionalUser.isEmpty()) {

            logger.warn("Profile update failed - user not found: {}",
                    userId);

            return "User not found";
        }

        User user = optionalUser.get();

        user.setName(name);
        user.setAddress(address);
        user.setProfilePicture(profilePicture);

        userDAO.saveUser(user);

        logger.info("Profile updated successfully for user ID: {}",
                userId);

        return "Profile updated successfully";
    }

    // ==========================================
    // USER STORY 1.3
    // FORGOT PASSWORD
    // ==========================================

    public String forgotPassword(String emailOrMobile) {

        logger.info("Forgot password request received");

        Optional<User> optionalUser =
                userDAO.findByEmailOrMobile(emailOrMobile);

        if (optionalUser.isEmpty()) {

            logger.warn("Forgot password failed - user not found");

            return "Email or mobile number not registered";
        }

        User user = optionalUser.get();

        String token = UUID.randomUUID().toString();

        user.setResetToken(token);

        user.setResetTokenExpiry(
                LocalDateTime.now().plusMinutes(15)
        );

        userDAO.saveUser(user);

        logger.info("Password reset token generated successfully for user ID: {}",
                user.getUserId());

        return "Password reset link sent successfully";
    }

    // ==========================================
    // USER STORY 1.3
    // RESET PASSWORD
    // ==========================================

    public String resetPassword(
            String token,
            String newPassword) {

        logger.info("Password reset request received");

        Optional<User> optionalUser =
                userDAO.findByResetToken(token);

        if (optionalUser.isEmpty()) {

            logger.warn("Password reset failed - invalid reset token");

            return "Invalid reset token";
        }

        User user = optionalUser.get();

        if (user.getResetTokenExpiry() == null ||
                user.getResetTokenExpiry()
                        .isBefore(LocalDateTime.now())) {

            logger.warn("Password reset failed - reset token expired");

            return "Reset token expired";
        }

        user.setPassword(newPassword);

        user.setResetToken(null);
        user.setResetTokenExpiry(null);

        userDAO.saveUser(user);

        logger.info("Password updated successfully for user ID: {}",
                user.getUserId());

        return "Password updated successfully";
    }
}