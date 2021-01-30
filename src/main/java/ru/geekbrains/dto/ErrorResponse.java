package ru.geekbrains.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ErrorResponse {

    @JsonProperty("status")
    private Integer status;
    @JsonProperty("message")
    private String message;
    @JsonProperty("timestamp")
    private String timestamp;

}