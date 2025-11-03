package com.example.ead_backend.dto.service;

import jakarta.validation.constraints.*;
import lombok.*;

/**
 * DTO for creating/updating service categories
 * Author: Member 9 - Dilminda W.W.C.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryRequest {
    
    @NotBlank(message = "Category name is required")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    @Pattern(regexp = "^[a-zA-Z0-9 &-]+$", message = "Category name can only contain letters, numbers, spaces, &, and -")
    private String name;
    
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;
    
    @Builder.Default
    private boolean isActive = true;
}