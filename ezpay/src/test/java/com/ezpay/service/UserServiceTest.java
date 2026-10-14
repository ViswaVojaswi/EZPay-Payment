package com.ezpay.service;

import com.ezpay.dao.UserDAO;
import com.ezpay.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserDAO userDAO;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);

        user = new User();

        user.setUserId(1L);
        user.setName("Swapana");
        user.setEmail("swapana@gmail.com");
        user.setMobile("9876543210");
        user.setPassword("Test@123");
        user.setAddress("Pune");
    }

    // ==========================================
    // REGISTRATION
    // ==========================================

    @Test
    void testRegisterUser_Success() {

        when(userDAO.existsByEmail(user.getEmail()))
                .thenReturn(false);

        when(userDAO.existsByMobile(user.getMobile()))
                .thenReturn(false);

        when(userDAO.saveUser(any(User.class)))
                .thenReturn(user);

        String result = userService.registerUser(user);

        assertEquals(
                "Registration successful. OTP sent to mobile.",
                result
        );

        verify(userDAO).saveUser(user);
    }

    @Test
    void testRegisterUser_EmailAlreadyRegistered() {

        when(userDAO.existsByEmail(user.getEmail()))
                .thenReturn(true);

        String result = userService.registerUser(user);

        assertEquals(
                "Email already registered",
                result
        );

        verify(userDAO, never()).saveUser(any(User.class));
    }

    @Test
    void testRegisterUser_MobileAlreadyRegistered() {

        when(userDAO.existsByEmail(user.getEmail()))
                .thenReturn(false);

        when(userDAO.existsByMobile(user.getMobile()))
                .thenReturn(true);

        String result = userService.registerUser(user);

        assertEquals(
                "Mobile number already registered",
                result
        );

        verify(userDAO, never()).saveUser(any(User.class));
    }

    // ==========================================
    // OTP VERIFICATION
    // ==========================================

    @Test
    void testVerifyOTP_Success() {

        user.setOtp("123456");
        user.setOtpExpiry(
                java.time.LocalDateTime.now().plusMinutes(5)
        );

        when(userDAO.findByMobile("9876543210"))
                .thenReturn(Optional.of(user));

        String result =
                userService.verifyOTP(
                        "9876543210",
                        "123456"
                );

        assertEquals(
                "Account verified successfully",
                result
        );

        verify(userDAO).saveUser(user);
    }

    @Test
    void testVerifyOTP_InvalidOTP() {

        user.setOtp("123456");
        user.setOtpExpiry(
                java.time.LocalDateTime.now().plusMinutes(5)
        );

        when(userDAO.findByMobile("9876543210"))
                .thenReturn(Optional.of(user));

        String result =
                userService.verifyOTP(
                        "9876543210",
                        "999999"
                );

        assertEquals(
                "Invalid OTP",
                result
        );
    }

    @Test
    void testVerifyOTP_UserNotFound() {

        when(userDAO.findByMobile("9876543210"))
                .thenReturn(Optional.empty());

        String result =
                userService.verifyOTP(
                        "9876543210",
                        "123456"
                );

        assertEquals(
                "User not found",
                result
        );
    }

    // ==========================================
    // LOGIN
    // ==========================================

    @Test
    void testLoginUser_Success() {

        when(userDAO.findByEmailOrMobile(
                "swapana@gmail.com"))
                .thenReturn(Optional.of(user));

        String result =
                userService.loginUser(
                        "swapana@gmail.com",
                        "Test@123"
                );

        assertEquals(
                "Login successful",
                result
        );
    }

    @Test
    void testLoginUser_InvalidCredentials() {

        when(userDAO.findByEmailOrMobile(
                "swapana@gmail.com"))
                .thenReturn(Optional.of(user));

        String result =
                userService.loginUser(
                        "swapana@gmail.com",
                        "WrongPassword"
                );

        assertEquals(
                "Invalid email/mobile or password",
                result
        );
    }

    @Test
    void testLoginUser_UserNotFound() {

        when(userDAO.findByEmailOrMobile(
                "unknown@gmail.com"))
                .thenReturn(Optional.empty());

        String result =
                userService.loginUser(
                        "unknown@gmail.com",
                        "Test@123"
                );

        assertEquals(
                "Invalid email/mobile or password",
                result
        );
    }

    // ==========================================
    // PROFILE UPDATE
    // ==========================================

    @Test
    void testUpdateProfile_Success() {

        when(userDAO.findById(1L))
                .thenReturn(Optional.of(user));

        when(userDAO.saveUser(any(User.class)))
                .thenReturn(user);

        String result =
                userService.updateProfile(
                        1L,
                        "Swapana Updated",
                        "Pune, Maharashtra",
                        "profile.jpg"
                );

        assertEquals(
                "Profile updated successfully",
                result
        );

        assertEquals(
                "Swapana Updated",
                user.getName()
        );

        assertEquals(
                "Pune, Maharashtra",
                user.getAddress()
        );

        assertEquals(
                "profile.jpg",
                user.getProfilePicture()
        );

        verify(userDAO).saveUser(user);
    }

    @Test
    void testUpdateProfile_UserNotFound() {

        when(userDAO.findById(1L))
                .thenReturn(Optional.empty());

        String result =
                userService.updateProfile(
                        1L,
                        "Swapana",
                        "Pune",
                        "profile.jpg"
                );

        assertEquals(
                "User not found",
                result
        );

        verify(userDAO, never())
                .saveUser(any(User.class));
    }

    // ==========================================
    // FORGOT PASSWORD
    // ==========================================

    @Test
    void testForgotPassword_Success() {

        when(userDAO.findByEmailOrMobile(
                "swapana@gmail.com"))
                .thenReturn(Optional.of(user));

        when(userDAO.saveUser(any(User.class)))
                .thenReturn(user);

        String result =
                userService.forgotPassword(
                        "swapana@gmail.com"
                );

        assertEquals(
                "Password reset link sent successfully",
                result
        );

        assertNotNull(user.getResetToken());
        assertNotNull(user.getResetTokenExpiry());

        verify(userDAO).saveUser(user);
    }

    @Test
    void testForgotPassword_UserNotFound() {

        when(userDAO.findByEmailOrMobile(
                "unknown@gmail.com"))
                .thenReturn(Optional.empty());

        String result =
                userService.forgotPassword(
                        "unknown@gmail.com"
                );

        assertEquals(
                "Email or mobile number not registered",
                result
        );

        verify(userDAO, never())
                .saveUser(any(User.class));
    }

    // ==========================================
    // RESET PASSWORD
    // ==========================================

    @Test
    void testResetPassword_Success() {

        user.setResetToken("test-token");

        user.setResetTokenExpiry(
                java.time.LocalDateTime.now().plusMinutes(10)
        );

        when(userDAO.findByResetToken("test-token"))
                .thenReturn(Optional.of(user));

        when(userDAO.saveUser(any(User.class)))
                .thenReturn(user);

        String result =
                userService.resetPassword(
                        "test-token",
                        "NewTest@123"
                );

        assertEquals(
                "Password updated successfully",
                result
        );

        assertEquals(
                "NewTest@123",
                user.getPassword()
        );

        assertNull(user.getResetToken());
        assertNull(user.getResetTokenExpiry());

        verify(userDAO).saveUser(user);
    }

    @Test
    void testResetPassword_InvalidToken() {

        when(userDAO.findByResetToken("wrong-token"))
                .thenReturn(Optional.empty());

        String result =
                userService.resetPassword(
                        "wrong-token",
                        "NewTest@123"
                );

        assertEquals(
                "Invalid reset token",
                result
        );
    }
}