package ru.geekbrains.dto.response;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@lombok.Data
public class AdConfig {

    @JsonProperty("safeFlags")
    public List<String> safeFlags = null;
    @JsonProperty("highRiskFlags")
    public List<Object> highRiskFlags = null;
    @JsonProperty("unsafeFlags")
    public List<String> unsafeFlags = null;
    @JsonProperty("wallUnsafeFlags")
    public List<Object> wallUnsafeFlags = null;
    @JsonProperty("showsAds")
    public Boolean showsAds;

}