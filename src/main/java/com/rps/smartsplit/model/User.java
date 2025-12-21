package com.rps.smartsplit.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;


    private String name;

    private String address;

    private int age;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    @Column(unique = true)
    private String email;


    @Column(unique = true)
    private String phNumber;

    private String profileUrl;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;
    
    // Email OTP fields
    private String emailOtp;
    private java.time.LocalDateTime emailOtpExpiry;
    
    // Mobile OTP fields
    private String mobileOtp;
    private java.time.LocalDateTime mobileOtpExpiry;
    
    // Login OTP fields
    private String loginOtp;
    private java.time.LocalDateTime loginOtpExpiry;
    
    // Forgot Password OTP fields
    private String forgotPasswordOtp;
    private java.time.LocalDateTime forgotPasswordOtpExpiry;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.USER; // Default role is USER


    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<ExpenseParticipant> expenseParticipantList;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(
            name = "user_group",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id")
    )
    @JsonIgnore
    private Set<Group> groups = new HashSet<>();


    private Boolean isEmailVerified = false;
    private  Boolean isMobileVerified = false;


    public Boolean getEmailVerified() {
        return isEmailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        isEmailVerified = emailVerified;
    }

    public Boolean getMobileVerified() {
        return isMobileVerified;
    }

    public void setMobileVerified(Boolean mobileVerified) {
        isMobileVerified = mobileVerified;
    }

    public User(){}

    public User(UUID id, String name, String address, int age, String email, String phNumber, String profileUrl, String password, String emailOtp, String mobileOtp) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.age = age;
        this.email = email;
        this.phNumber = phNumber;
        this.profileUrl = profileUrl;
        this.password = password;
        this.emailOtp = emailOtp;
        this.mobileOtp = mobileOtp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
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

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Email OTP getters and setters
    public String getEmailOtp() {
        return emailOtp;
    }

    public void setEmailOtp(String emailOtp) {
        this.emailOtp = emailOtp;
    }

    public java.time.LocalDateTime getEmailOtpExpiry() {
        return emailOtpExpiry;
    }

    public void setEmailOtpExpiry(java.time.LocalDateTime emailOtpExpiry) {
        this.emailOtpExpiry = emailOtpExpiry;
    }

    // Mobile OTP getters and setters
    public String getMobileOtp() {
        return mobileOtp;
    }

    public void setMobileOtp(String mobileOtp) {
        this.mobileOtp = mobileOtp;
    }

    public java.time.LocalDateTime getMobileOtpExpiry() {
        return mobileOtpExpiry;
    }

    public void setMobileOtpExpiry(java.time.LocalDateTime mobileOtpExpiry) {
        this.mobileOtpExpiry = mobileOtpExpiry;
    }

    // Login OTP getters and setters
    public String getLoginOtp() {
        return loginOtp;
    }

    public void setLoginOtp(String loginOtp) {
        this.loginOtp = loginOtp;
    }

    public java.time.LocalDateTime getLoginOtpExpiry() {
        return loginOtpExpiry;
    }

    public void setLoginOtpExpiry(java.time.LocalDateTime loginOtpExpiry) {
        this.loginOtpExpiry = loginOtpExpiry;
    }

    // Forgot Password OTP getters and setters
    public String getForgotPasswordOtp() {
        return forgotPasswordOtp;
    }

    public void setForgotPasswordOtp(String forgotPasswordOtp) {
        this.forgotPasswordOtp = forgotPasswordOtp;
    }

    public java.time.LocalDateTime getForgotPasswordOtpExpiry() {
        return forgotPasswordOtpExpiry;
    }

    public void setForgotPasswordOtpExpiry(java.time.LocalDateTime forgotPasswordOtpExpiry) {
        this.forgotPasswordOtpExpiry = forgotPasswordOtpExpiry;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public Set<Group> getGroups() {
        return groups;
    }

    public void setGroups(Set<Group> groups) {
        this.groups = groups;
    }

    public List<ExpenseParticipant> getExpenseParticipantList() {
        return expenseParticipantList;
    }

    public void setExpenseParticipantList(List<ExpenseParticipant> expenseParticipantList) {
        this.expenseParticipantList = expenseParticipantList;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
