package com.rps.smartsplit.service;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
public class OTPService {
    
    private static final int OTP_LENGTH = 6;
    private static final int OTP_VALIDITY_MINUTES = 5;
    
    /**
     * Generates a random 6-digit OTP
     */
    public String generateOtp() {
        // Generate 6-digit OTP (100000 to 999999)
        int min = (int) Math.pow(10, OTP_LENGTH - 1);
        int max = (int) Math.pow(10, OTP_LENGTH) - 1;
        int otp = min + new SecureRandom().nextInt(max - min + 1);
        return String.valueOf(otp);
    }
    
    /**
     * Calculates OTP expiry time (current time + validity period)
     */
    public LocalDateTime calculateOtpExpiry() {
        return LocalDateTime.now().plusMinutes(OTP_VALIDITY_MINUTES);
    }
    
    /**
     * Validates if OTP is correct and not expired
     */
    public boolean isValidOtp(String storedOtp, LocalDateTime expiryTime, String providedOtp) {
        if (storedOtp == null || providedOtp == null) {
            return false;
        }
        
        if (expiryTime == null || LocalDateTime.now().isAfter(expiryTime)) {
            return false; // OTP expired
        }
        
        return storedOtp.equals(providedOtp);
    }
    
    /**
     * Checks if OTP has expired
     */
    public boolean isOtpExpired(LocalDateTime expiryTime) {
        if (expiryTime == null) {
            return true;
        }
        return LocalDateTime.now().isAfter(expiryTime);
    }
}

