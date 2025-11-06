package org.genc.sneakoapp.productmanagementservice.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Product {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long productID;

        private String imageUrl;

        private String productName;

        private String description;

        private BigDecimal price;

        private Long stockQuantity;

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "CategoryID")
        private Category category;

        @CreationTimestamp
        private LocalDateTime createdAt;
        @UpdateTimestamp
        private LocalDateTime updatedAt;
        private String createdBy;
        private String updatedBy;

        @Version
        private Long version;

        @PrePersist
        public void setCreationDefaults() {
                // Only set if they haven't been explicitly set in the service layer
                if (this.createdBy == null) {
                        this.createdBy = "ADMIN";
                }
                if (this.updatedBy == null) {
                        this.updatedBy = "ADMIN";
                }
        }

}
