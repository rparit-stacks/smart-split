package com.rps.smartsplit.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthDto {

    public static class RegisterDto {
        @NotBlank(message = "Name cannot be blank")
        private String name;

        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Invalid email format")
        private String email;

        @NotBlank(message = "Phone number cannot be blank")
        private String phNumber;

        @NotBlank(message = "Password cannot be blank")
        @Size(min = 8, message = "Password must be at least 8 characters long")
        private String password;

        public RegisterDto() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhNumber() {
            return phNumber;
        }

        public void setPhNumber(String phNumber) {
            this.phNumber = phNumber;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static class LoginDto {  // Changed from Login to LoginDto for consistency
        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Invalid email format")
        private String email;

        @NotBlank(message = "Password cannot be blank")
        private String password;

        public LoginDto() {
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static class VerifyEmailOtpDto {
        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Invalid email format")
        private String email;

        @NotBlank(message = "OTP cannot be blank")
        private String otp;

        public VerifyEmailOtpDto() {
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getOtp() {
            return otp;
        }

        public void setOtp(String otp) {
            this.otp = otp;
        }
    }

    public static class VerifyMobileOtpDto {
        @NotBlank(message = "Phone number cannot be blank")
        private String phNumber;

        @NotBlank(message = "OTP cannot be blank")
        private String otp;

        public VerifyMobileOtpDto() {
        }

        public String getPhNumber() {
            return phNumber;
        }

        public void setPhNumber(String phNumber) {
            this.phNumber = phNumber;
        }

        public String getOtp() {
            return otp;
        }

        public void setOtp(String otp) {
            this.otp = otp;
        }
    }

    public static class ResendEmailOtpDto {
        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Invalid email format")
        private String email;

        public ResendEmailOtpDto() {
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    public static class ResendMobileOtpDto {
        @NotBlank(message = "Phone number cannot be blank")
        private String phNumber;

        public ResendMobileOtpDto() {
        }

        public String getPhNumber() {
            return phNumber;
        }

        public void setPhNumber(String phNumber) {
            this.phNumber = phNumber;
        }
    }

    public static class VerifyLoginOtpDto {
        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Invalid email format")
        private String email;

        @NotBlank(message = "OTP cannot be blank")
        private String otp;

        public VerifyLoginOtpDto() {
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getOtp() {
            return otp;
        }

        public void setOtp(String otp) {
            this.otp = otp;
        }
    }

    public static class ForgotPasswordDto{
        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Invalid email format")
        private String email;

        public ForgotPasswordDto() {
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    public static class ResetPasswordDto {
        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Invalid email format")
        private String email;

        @NotBlank(message = "OTP cannot be blank")
        private String otp;

        @NotBlank(message = "New password cannot be blank")
        @Size(min = 8, message = "Password must be at least 8 characters long")
        private String newPassword;

        public ResetPasswordDto() {
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getOtp() {
            return otp;
        }

        public void setOtp(String otp) {
            this.otp = otp;
        }

        public String getNewPassword() {
            return newPassword;
        }

        public void setNewPassword(String newPassword) {
            this.newPassword = newPassword;
        }
    }

}