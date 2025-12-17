package com.rps.smartsplit.service;

import com.rps.smartsplit.config.CustomUserDetail;
import com.rps.smartsplit.dto.GroupResponseDTO;
import com.rps.smartsplit.config.CustomUserDetail;
import com.rps.smartsplit.dto.UserRequestDTO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.rps.smartsplit.dto.UserResponseDTO;
import com.rps.smartsplit.model.Role;
import com.rps.smartsplit.model.User;
import com.rps.smartsplit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private GroupService groupService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private OTPService otpService;




    public ResponseEntity<UserResponseDTO> updateUser(UUID id, UserRequestDTO userRequestDTO) {
        // Check if user exists
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));

        // Check if email is being changed and if new email already exists (for different user)
        if (userRequestDTO.getEmail() != null && !userRequestDTO.getEmail().equals(user.getEmail())) {
            userRepository.findByEmail(userRequestDTO.getEmail())
                    .ifPresent(existingUser -> {
                        if (!existingUser.getId().equals(id)) {
                            throw new IllegalArgumentException("Email already exists");
                        }
                    });
        }

        // Check if phone number is being changed and if new phone already exists (for different user)
        if (userRequestDTO.getPhNumber() != null && !userRequestDTO.getPhNumber().equals(user.getPhNumber())) {
            userRepository.findByPhNumber(userRequestDTO.getPhNumber())
                    .ifPresent(existingUser -> {
                        if (!existingUser.getId().equals(id)) {
                            throw new IllegalArgumentException("Phone number already exists");
                        }
                    });
        }

        // Update user fields (only update non-null fields from DTO)
        if (userRequestDTO.getName() != null) {
            user.setName(userRequestDTO.getName());
        }
        if (userRequestDTO.getAddress() != null) {
            user.setAddress(userRequestDTO.getAddress());
        }
        if (userRequestDTO.getAge() > 0) {
            user.setAge(userRequestDTO.getAge());
        }
        if (userRequestDTO.getEmail() != null) {
            user.setEmail(userRequestDTO.getEmail());
        }
        if (userRequestDTO.getPhNumber() != null) {
            user.setPhNumber(userRequestDTO.getPhNumber());
        }
        if (userRequestDTO.getProfileUrl() != null) {
            user.setProfileUrl(userRequestDTO.getProfileUrl());
        }
        if (userRequestDTO.getPassword() != null && !userRequestDTO.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        }
        
        // Update role if provided
        if (userRequestDTO.getRole() != null && !userRequestDTO.getRole().isEmpty()) {
            try {
                user.setRole(Role.valueOf(userRequestDTO.getRole().toUpperCase()));
            } catch (IllegalArgumentException e) {
                // If invalid role, keep existing role (don't update)
            }
        }

        // Save updated user
        User updatedUser = userRepository.save(user);

        // Convert to DTO and return
        return ResponseEntity.ok(UserToUserDto(updatedUser));
    }

    public ResponseEntity<UserResponseDTO> deleteUser(UUID id) {
        // Check if user exists
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));

        // Check if user has groups
        if (!user.getGroups().isEmpty()) {
            throw new IllegalArgumentException("Cannot delete user with groups. Please remove user from all groups first.");
        }

        // Check if user has expense participants (optional check - depends on your business logic)
        if (user.getExpenseParticipantList() != null && !user.getExpenseParticipantList().isEmpty()) {
            throw new IllegalArgumentException("Cannot delete user with expense participants. Please settle all expenses first.");
        }

        // Convert to DTO before deletion (to return deleted user info)
        UserResponseDTO userResponseDTO = UserToUserDto(user);

        // Delete the user
        userRepository.delete(user);

        return ResponseEntity.ok(userResponseDTO);
    }

    public ResponseEntity<List<GroupResponseDTO>> getUserGroups(UUID id) {
        // Check if user exists
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));

        
        List<GroupResponseDTO> groupResponseDTOs = user.getGroups().stream()
                .map(groupService::groupToGroupDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(groupResponseDTOs);
    }

    public ResponseEntity<UserResponseDTO> getUserBalanceSummary(UUID id) {
        return null;
    }

    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserResponseDTO> userResponseDTOS = users.stream().map(this::UserToUserDto).toList();
        return ResponseEntity.ok(userResponseDTOS);

    }

    public ResponseEntity<List<UserResponseDTO>> getUser(UUID id) {
        User user = userRepository.findById(id).get();
        if (user.getId() == null) {
            return ResponseEntity.notFound().build();
        }
        UserResponseDTO userResponseDTO = UserToUserDto(user);
        return ResponseEntity.ok(List.of(userResponseDTO));
    }

    public ResponseEntity<?> createUser(UserRequestDTO userRequestDTO) {
        // Check if email already exists
        if (userRepository.findByEmail((userRequestDTO.getEmail())).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        // Check if phone number already exists
        if (userRepository.findByPhNumber(userRequestDTO.getPhNumber()).isPresent()) {
            throw new IllegalArgumentException("Phone number already exists");
        }

        User user = new User();
        user.setName(userRequestDTO.getName());
        user.setAddress(userRequestDTO.getAddress());
        user.setAge(userRequestDTO.getAge());
        user.setEmail(userRequestDTO.getEmail());
        user.setPhNumber(userRequestDTO.getPhNumber());
        user.setProfileUrl(userRequestDTO.getProfileUrl());
        user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        
        // Set role (default to USER if not provided or invalid)
        if (userRequestDTO.getRole() != null && !userRequestDTO.getRole().isEmpty()) {
            try {
                user.setRole(Role.valueOf(userRequestDTO.getRole().toUpperCase()));
            } catch (IllegalArgumentException e) {
                user.setRole(Role.USER); // Default to USER if invalid role
            }
        } else {
            user.setRole(Role.USER); // Default to USER
        }
        
        // Generate and send email OTP
        String emailOtp = otpService.generateOtp();
        user.setEmailOtp(emailOtp);
        user.setEmailOtpExpiry(otpService.calculateOtpExpiry());
        emailService.sendOtpEmail(user.getEmail(), emailOtp);

        User savedUser = userRepository.save(user);
        return ResponseEntity.ok("Registration successful. Please verify your email with the OTP sent to " + user.getEmail());
    }


    private UserResponseDTO UserToUserDto(User user){
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(user.getId());
        userResponseDTO.setName(user.getName());
        userResponseDTO.setAddress(user.getAddress());
        userResponseDTO.setAge(user.getAge());
        userResponseDTO.setEmail(user.getEmail());
        userResponseDTO.setPhNumber(user.getPhNumber());
        userResponseDTO.setProfileUrl(user.getProfileUrl());
        userResponseDTO.setRole(user.getRole() != null ? user.getRole().name() : Role.USER.name());

        return userResponseDTO;
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));
    }

    /**
     * Gets current authenticated user's details
     */
    public ResponseEntity<UserResponseDTO> getCurrentUser(String email) {
        User user = getUserByEmail(email);
        UserResponseDTO userResponseDTO = UserToUserDto(user);
        return ResponseEntity.ok(userResponseDTO);
    }

    public String verifyEmailOtp(String email, String otp){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));
        
        // Check if email is already verified
        if (Boolean.TRUE.equals(user.getEmailVerified())) {
            return "Email is already verified";
        }
        
        // Validate OTP
        if (!otpService.isValidOtp(user.getEmailOtp(), user.getEmailOtpExpiry(), otp)) {
            if (otpService.isOtpExpired(user.getEmailOtpExpiry())) {
                throw new IllegalArgumentException("OTP has expired. Please request a new one.");
            }
            throw new IllegalArgumentException("Invalid OTP");
        }
        
        // Mark email as verified and clear email OTP
        user.setEmailVerified(true);
        user.setEmailOtp(null);
        user.setEmailOtpExpiry(null);
        
        // Generate and send mobile OTP
        String mobileOtp = otpService.generateOtp();
        user.setMobileOtp(mobileOtp);
        user.setMobileOtpExpiry(otpService.calculateOtpExpiry());
        // TODO: Send SMS OTP here when SMS service is implemented
        System.out.println("Mobile OTP for " + user.getPhNumber() + ": " + mobileOtp);
        
        userRepository.save(user);
        return "Email successfully verified. Mobile OTP has been sent to " + user.getPhNumber();
    }


    public String verifyMobileOtp(String phNumber, String otp){
        User user = userRepository.findByPhNumber(phNumber)
                .orElseThrow(() -> new IllegalArgumentException("User not found with phone number: " + phNumber));

        // Check if email is verified first
        if (user.getEmailVerified() == null || !user.getEmailVerified()) {
            throw new IllegalArgumentException("Please verify your email first");
        }
        
        // Check if mobile is already verified
        if (Boolean.TRUE.equals(user.getMobileVerified())) {
            return "Mobile number is already verified";
        }
        
        // Validate OTP
        if (!otpService.isValidOtp(user.getMobileOtp(), user.getMobileOtpExpiry(), otp)) {
            if (otpService.isOtpExpired(user.getMobileOtpExpiry())) {
                throw new IllegalArgumentException("OTP has expired. Please request a new one.");
            }
            throw new IllegalArgumentException("Invalid OTP");
        }
        
        // Mark mobile as verified and clear mobile OTP
            user.setMobileVerified(true);
        user.setMobileOtp(null);
        user.setMobileOtpExpiry(null);
        
        userRepository.save(user);
        return "Mobile number successfully verified";
    }

    /**
     * Resends email OTP to the user
     */
    public String resendEmailOtp(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));
        
        // Generate new email OTP
        String emailOtp = otpService.generateOtp();
        user.setEmailOtp(emailOtp);
        user.setEmailOtpExpiry(otpService.calculateOtpExpiry());
        emailService.sendOtpEmail(email, emailOtp);
        
        userRepository.save(user);
        return "Email OTP has been resent to " + email;
    }
    
    /**
     * Resends mobile OTP to the user
     */
    public String resendMobileOtp(String phNumber) {
        User user = userRepository.findByPhNumber(phNumber)
                .orElseThrow(() -> new IllegalArgumentException("User not found with phone number: " + phNumber));
        
        // Check if email is verified first
        if (user.getEmailVerified() == null || !user.getEmailVerified()) {
            throw new IllegalArgumentException("Please verify your email first");
        }
        
        // Generate new mobile OTP
        String mobileOtp = otpService.generateOtp();
        user.setMobileOtp(mobileOtp);
        user.setMobileOtpExpiry(otpService.calculateOtpExpiry());
        // TODO: Send SMS OTP here when SMS service is implemented
        System.out.println("Mobile OTP for " + phNumber + ": " + mobileOtp);
        
        userRepository.save(user);
        return "Mobile OTP has been resent to " + phNumber;
    }

    /**
     * Sends login OTP to user's email after password verification
     */
    public String sendLoginOtp(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));
        
        // Generate login OTP
        String loginOtp = otpService.generateOtp();
        user.setLoginOtp(loginOtp);
        user.setLoginOtpExpiry(otpService.calculateOtpExpiry());
        emailService.sendOtpEmail(email, loginOtp);
        
        userRepository.save(user);
        return "Login OTP has been sent to " + email;
    }
    
    /**
     * Verifies login OTP and returns user if valid
     */
    public User verifyLoginOtp(String email, String otp) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));
        
        // Validate OTP
        if (!otpService.isValidOtp(user.getLoginOtp(), user.getLoginOtpExpiry(), otp)) {
            if (otpService.isOtpExpired(user.getLoginOtpExpiry())) {
                throw new IllegalArgumentException("OTP has expired. Please login again.");
            }
            throw new IllegalArgumentException("Invalid OTP");
        }
        
        // Clear login OTP after successful verification
        user.setLoginOtp(null);
        user.setLoginOtpExpiry(null);
        userRepository.save(user);
        
        return user;
    }

    /**
     * Resends login OTP
     */
    public String resendLoginOtp(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));
        
        // Generate new login OTP
        String loginOtp = otpService.generateOtp();
        user.setLoginOtp(loginOtp);
        user.setLoginOtpExpiry(otpService.calculateOtpExpiry());
        emailService.sendOtpEmail(email, loginOtp);
        
        userRepository.save(user);
        return "Login OTP has been resent to " + email;
    }

    public String forgotPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));
        
        // Generate forgot password OTP
        String otp = otpService.generateOtp();
        user.setForgotPasswordOtp(otp);
        user.setForgotPasswordOtpExpiry(otpService.calculateOtpExpiry());
        
        // Send OTP via email
        emailService.sendOtpEmail(email, otp);
        
        // Save user
        userRepository.save(user);
        
        return "Password reset OTP has been sent to " + email;
    }

    /**
     * Resets password after OTP verification
     */
    public String resetPassword(String email, String otp, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));
        
        // Validate OTP again
        if (!otpService.isValidOtp(user.getForgotPasswordOtp(), user.getForgotPasswordOtpExpiry(), otp)) {
            if (otpService.isOtpExpired(user.getForgotPasswordOtpExpiry())) {
                throw new IllegalArgumentException("OTP has expired. Please request a new one.");
            }
            throw new IllegalArgumentException("Invalid OTP");
        }
        
        // Update password
        user.setPassword(passwordEncoder.encode(newPassword));
        
        // Clear forgot password OTP after successful reset
        user.setForgotPasswordOtp(null);
        user.setForgotPasswordOtpExpiry(null);
        
        userRepository.save(user);
        
        return "Password has been reset successfully";
    }

    /**
     * Resends forgot password OTP
     */
    public String resendForgotPasswordOtp(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));
        
        // Generate new forgot password OTP
        String otp = otpService.generateOtp();
        user.setForgotPasswordOtp(otp);
        user.setForgotPasswordOtpExpiry(otpService.calculateOtpExpiry());
        emailService.sendOtpEmail(email, otp);
        
        userRepository.save(user);
        return "Password reset OTP has been resent to " + email;
    }

   
//
//    public ResponseEntity<List<UserResponseDTO>> getUsersByRole(String role) {
//
//    }
}
