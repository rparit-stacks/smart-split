package com.rps.smartsplit.controller;

import com.rps.smartsplit.config.CustomUserDetail;
import com.rps.smartsplit.dto.AuthDto;
import com.rps.smartsplit.dto.UserRequestDTO;
import com.rps.smartsplit.model.User;
import com.rps.smartsplit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired(required = false)
    private SecurityContextRepository securityContextRepository;

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody AuthDto.RegisterDto registerDto){
        System.out.println("Register called");
        // Map auth DTO to full UserRequestDTO
        UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setName(registerDto.getName());
        userRequestDTO.setEmail(registerDto.getEmail());
        userRequestDTO.setPhNumber(registerDto.getPhNumber());
        userRequestDTO.setPassword(registerDto.getPassword());

        return userService.createUser(userRequestDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthDto.LoginDto loginDto) {
        try{
            // First verify email and password
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDto.getEmail(),
                            loginDto.getPassword()
                    )
            );

            if (authentication == null) {
                return ResponseEntity.badRequest().body("Authentication failed");
            }
            
            // If credentials are valid, send OTP
            String result = userService.sendLoginOtp(loginDto.getEmail());
            return ResponseEntity.ok(result);
            
        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest().body("Invalid email or password");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Login failed: " + e.getMessage());
        }
    }

    @PostMapping("/verify-login-otp")
    public ResponseEntity<?> verifyLoginOtp(
            @RequestBody AuthDto.VerifyLoginOtpDto verifyLoginOtpDto,
            HttpServletRequest request,
            HttpServletResponse response) {
        try {
            // Verify OTP
            User user = userService.verifyLoginOtp(verifyLoginOtpDto.getEmail(), verifyLoginOtpDto.getOtp());
            
            // Create UserDetails from the verified user
            CustomUserDetail userDetails = new CustomUserDetail(user);
            
            // Create Authentication object directly (no need to re-authenticate)
            // Credentials were already verified in /login endpoint
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null, // No password needed - already verified
                    userDetails.getAuthorities()
            );
            
            // Create and set SecurityContext
            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
            securityContext.setAuthentication(authentication);
            
            // Set it in SecurityContextHolder for current request
            SecurityContextHolder.setContext(securityContext);
            
            // Save SecurityContext to HTTP session using Spring Security's repository
            // If repository is not injected, use default HttpSessionSecurityContextRepository
            SecurityContextRepository repo = securityContextRepository != null 
                    ? securityContextRepository 
                    : new HttpSessionSecurityContextRepository();
            repo.saveContext(securityContext, request, response);
            
            System.out.println("✅ Authentication saved to session. Session ID: " + request.getSession().getId());
            System.out.println("✅ User authenticated: " + user.getEmail());
            
            return ResponseEntity.ok("Login successful - Welcome " + user.getName());
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to verify login OTP: " + e.getMessage());
        }
    }


    @PostMapping("/logout")
    public ResponseEntity<String> logout(){
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Logout successful");
    }

    @PostMapping("/verify-email-otp")
    public ResponseEntity<?> verifyEmailOtp(@RequestBody AuthDto.VerifyEmailOtpDto verifyEmailOtpDto) {
        try {
        String result = userService.verifyEmailOtp(verifyEmailOtpDto.getEmail(), verifyEmailOtpDto.getOtp());
        return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to verify email OTP: " + e.getMessage());
        }
    }

    @PostMapping("/verify-mobile-otp")
    public ResponseEntity<?> verifyMobileOtp(@RequestBody AuthDto.VerifyMobileOtpDto verifyMobileOtpDto) {
        try {
            String result = userService.verifyMobileOtp(verifyMobileOtpDto.getPhNumber(), verifyMobileOtpDto.getOtp());
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to verify mobile OTP: " + e.getMessage());
        }
    }

    @PostMapping("/resend-email-otp")
    public ResponseEntity<?> resendEmailOtp(@RequestBody AuthDto.ResendEmailOtpDto resendEmailOtpDto) {
        try {
            String result = userService.resendEmailOtp(resendEmailOtpDto.getEmail());
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to resend email OTP: " + e.getMessage());
        }
    }

    @PostMapping("/resend-mobile-otp")
    public ResponseEntity<?> resendMobileOtp(@RequestBody AuthDto.ResendMobileOtpDto resendMobileOtpDto) {
        try {
            String result = userService.resendMobileOtp(resendMobileOtpDto.getPhNumber());
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to resend mobile OTP: " + e.getMessage());
        }
    }

    @PostMapping("/resend-login-otp")
    public ResponseEntity<?> resendLoginOtp(@RequestBody AuthDto.ResendEmailOtpDto resendEmailOtpDto) {
        try {
            String result = userService.resendLoginOtp(resendEmailOtpDto.getEmail());
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to resend login OTP: " + e.getMessage());
        }
    }


    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody AuthDto.ForgotPasswordDto forgotPassword){
        try{
            String result = userService.forgotPassword(forgotPassword.getEmail());
            return ResponseEntity.ok(result);
        }catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to ForgotPassword : " + e.getMessage());
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody AuthDto.ResetPasswordDto resetPasswordDto) {
        try {
            String result = userService.resetPassword(
                    resetPasswordDto.getEmail(),
                    resetPasswordDto.getOtp(),
                    resetPasswordDto.getNewPassword()
            );
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to reset password: " + e.getMessage());
        }
    }

    @PostMapping("/resend-forgot-password-otp")
    public ResponseEntity<?> resendForgotPasswordOtp(@RequestBody AuthDto.ForgotPasswordDto forgotPasswordDto) {
        try {
            String result = userService.resendForgotPasswordOtp(forgotPasswordDto.getEmail());
        return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to resend forgot password OTP: " + e.getMessage());
        }
    }

}


