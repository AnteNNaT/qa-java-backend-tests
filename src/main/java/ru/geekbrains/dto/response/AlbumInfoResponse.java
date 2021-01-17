package ru.geekbrains.dto.response;

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
public class AlbumInfoResponse {

    @JsonProperty("data")
    public AlbumInfoData albumInfoData;
    @JsonProperty("success")
    public Boolean success;
    @JsonProperty("status")
    public Integer status;

}
