package ru.geekbrains.dto.response;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@lombok.Data
public class AdConfig {

    @JsonProperty("safeFlags")
    private List<String> safeFlags = null;
    @JsonProperty("highRiskFlags")
    private List<Object> highRiskFlags = null;
    @JsonProperty("unsafeFlags")
    private List<String> unsafeFlags = null;
    @JsonProperty("wallUnsafeFlags")
    private List<Object> wallUnsafeFlags = null;
    @JsonProperty("showsAds")
    private Boolean showsAds;

}