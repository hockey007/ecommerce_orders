package com.ecommerce.order.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderRequestDto {

    @JsonProperty("user_id")
    @NotBlank(message = "UserId required")
    private String userId;

    // TODO: might want to include address id as well

}
