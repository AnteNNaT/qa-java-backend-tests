package ru.geekbrains.dto.response.way2;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@lombok.Data
@JsonPropertyOrder({
        "data",
        "success",
        "status"
})
public class CommonResponse<T> {

    @JsonProperty("data")
    private T data;
    @JsonProperty("success")
    private Boolean success;
    @JsonProperty("status")
    private Integer status;

}
