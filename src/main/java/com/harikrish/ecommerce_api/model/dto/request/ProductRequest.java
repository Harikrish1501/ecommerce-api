package com.harikrish.ecommerce_api.model.dto.request;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class  ProductRequest {

    @NotBlank(message = "Title is required")
    @Size(message = "Title must be between 3 and 100 characters", min = 3, max = 100)
     private String title;

    @NotBlank(message = "Model is required")
    @Size(message = "Model must be between 2 and 50 characters", min = 2, max = 50)
     private String model;

    @NotBlank(message = "Brand is required")
    @Size(message = "Brand must be between 2 and 50 characters", min = 2, max = 50)
     private  String brand;

    @NotBlank(message = "description is required")
    @Size(message = "description must be between 10 and 1000 characters", min = 10, max = 1000)
     private String description;

    @NotNull(message = "price is required ")
    @Min(message = "price must be a greater than 10", value = 10)
     private Double price;

    @NotNull(message = "stockQuantity is required")
    @Min(message = "stockQuantity must be a non-negative integer", value = 1)
     private Integer stockQuantity;

    @NotBlank(message = "category is required")
    @Size(message = "category must be between 3 and 50 characters", min = 3, max = 50)
     private String category;

    @NotBlank(message = "imageUrl is required")
     private  String imageUrl;


}
