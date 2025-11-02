package com.example.ead_backend.model.entity;

import com.example.ead_backend.model.enums.ServiceStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "services")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private ServiceCategory category;

    @Column(name = "duration_in_hours")
    private Double durationInHours;

    // 👇 Integrated pricing here
    private Double price;

    @Builder.Default
    private String currency = "LKR";

    @Enumerated(EnumType.STRING)
    private ServiceStatus status;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();
}
