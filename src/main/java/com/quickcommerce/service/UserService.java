package com.quickcommerce.service;

import com.quickcommerce.dto.LoginRequest;
import com.quickcommerce.dto.LoginResponse;
import com.quickcommerce.dto.RegistrationRequest;
import com.quickcommerce.dto.UserDTO;
import com.quickcommerce.entity.User;
import com.quickcommerce.enums.UserRole;
import com.quickcommerce.patterns.factory.UserFactory;
import com.quickcommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    /**
     * Register a new user using the UserFactory Pattern
     * Demonstrates Dependency Inversion Principle (DIP)
     */
    public User registerUser(RegistrationRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists!");
        }

        // Using Factory pattern for user creation
        UserRole userRole = UserRole.valueOf(request.getRole() != null ? request.getRole() : "CUSTOMER");
        User user = UserFactory.createUser(
                userRole,
                request.getEmail(),
                request.getPassword(),
                request.getFullName() != null ? request.getFullName() : (request.getFirstName() + " " + request.getLastName()),
                request.getPhoneNumber()
        );

        user.setAddress(request.getAddress() != null ? request.getAddress() : "Not provided");
        user.setCity(request.getCity() != null ? request.getCity() : "Not specified");
        user.setZipCode(request.getZipCode() != null ? request.getZipCode() : "000000");

        if (userRole == UserRole.DELIVERY_PARTNER) {
            user.setVehicleNumber(request.getVehicleNumber());
            user.setLicenseNumber(request.getLicenseNumber());
        }

        return userRepository.save(user);
    }

    /**
     * Authenticate user and return JWT token
     */
    public LoginResponse loginUser(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found!"));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("Invalid password!");
        }

        if (!user.getIsActive()) {
            throw new RuntimeException("User account is inactive!");
        }

        String token = jwtService.generateToken(user);
        LoginResponse response = new LoginResponse();
        response.token = token;
        UserDTO userDTO = convertToDTO(user);
        response.userId = user.getUserId();
        response.email = user.getEmail();
        response.user = (userDTO != null) ? userDTO.getFullName() : "Unknown";
        response.userRole = userDTO != null ? userDTO.getRole() : null;
        response.message = "Login successful!";
        return response;
    }

    /**
     * Get user by ID
     */
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
    }

    /**
     * Get all users (Admin only)
     */
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get all delivery partners
     */
    public List<UserDTO> getAllDeliveryPartners() {
        return userRepository.findByRole(UserRole.DELIVERY_PARTNER).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Update user profile
     */
    public User updateUser(Long userId, User updatedUser) {
        User user = getUserById(userId);
        user.setFullName(updatedUser.getFullName());
        user.setPhoneNumber(updatedUser.getPhoneNumber());
        user.setAddress(updatedUser.getAddress());
        user.setCity(updatedUser.getCity());
        user.setZipCode(updatedUser.getZipCode());
        user.setProfileImageUrl(updatedUser.getProfileImageUrl());
        return userRepository.save(user);
    }

    /**
     * Verify delivery partner
     */
    public void verifyDeliveryPartner(Long deliveryPartnerId) {
        User partner = getUserById(deliveryPartnerId);
        if (partner.getRole() != UserRole.DELIVERY_PARTNER) {
            throw new RuntimeException("User is not a delivery partner!");
        }
        partner.setIsVerified(true);
        partner.setIsActive(true);
        userRepository.save(partner);
    }

    /**
     * Convert User entity to DTO (Liskov Substitution Principle)
     */
    public UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.userId = user.getUserId();
        dto.email = user.getEmail();
        dto.fullName = user.getFullName();
        dto.role = user.getRole().toString();
        dto.walletBalance = user.getWalletBalance();
        return dto;
    }
}
