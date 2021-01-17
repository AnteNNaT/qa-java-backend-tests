package ru.geekbrains.dto.response;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@lombok.Data
public class ImageInfoResponse {

    @JsonProperty("id")
    public String id;
    @JsonProperty("title")
    public Object title;
    @JsonProperty("description")
    public Object description;
    @JsonProperty("datetime")
    public Integer datetime;
    @JsonProperty("type")
    public String type;
    @JsonProperty("animated")
    public Boolean animated;
    @JsonProperty("width")
    public Integer width;
    @JsonProperty("height")
    public Integer height;
    @JsonProperty("size")
    public Integer size;
    @JsonProperty("views")
    public Integer views;
    @JsonProperty("bandwidth")
    public Integer bandwidth;
    @JsonProperty("vote")
    public Object vote;
    @JsonProperty("favorite")
    public Boolean favorite;
    @JsonProperty("nsfw")
    public Object nsfw;
    @JsonProperty("section")
    public Object section;
    @JsonProperty("account_url")
    public Object accountUrl;
    @JsonProperty("account_id")
    public Object accountId;
    @JsonProperty("is_ad")
    public Boolean isAd;
    @JsonProperty("in_most_viral")
    public Boolean inMostViral;
    @JsonProperty("has_sound")
    public Boolean hasSound;
    @JsonProperty("tags")
    public List<Object> tags = null;
    @JsonProperty("ad_type")
    public Integer adType;
    @JsonProperty("ad_url")
    public String adUrl;
    @JsonProperty("edited")
    public String edited;
    @JsonProperty("in_gallery")
    public Boolean inGallery;
    @JsonProperty("deletehash")
    public String deletehash;
    @JsonProperty("name")
    public Object name;
    @JsonProperty("link")
    public String link;

}