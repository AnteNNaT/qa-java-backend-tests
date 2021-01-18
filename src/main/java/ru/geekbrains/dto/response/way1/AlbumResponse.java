package ru.geekbrains.dto.response.way1;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.NoArgsConstructor;
import ru.geekbrains.dto.response.AdConfig;

import java.util.List;
@lombok.Data
@NoArgsConstructor
@JsonPropertyOrder({
        "data",
        "success",
        "status"
})
public class AlbumResponse {

        @JsonProperty("data")
        private Data data;
        @JsonProperty("success")
        private Boolean success;
        @JsonProperty("status")
        private Integer status;

    @lombok.Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Data {

        @JsonProperty("id")
        private String id;
        @JsonProperty("title")
        private String title;
        @JsonProperty("description")
        private String description;
        @JsonProperty("datetime")
        private Integer datetime;
        @JsonProperty("cover")
        private String cover;
        @JsonProperty("cover_edited")
        private Object coverEdited;
        @JsonProperty("cover_width")
        private Integer coverWidth;
        @JsonProperty("cover_height")
        private Integer coverHeight;
        @JsonProperty("account_url")
        private String accountUrl;
        @JsonProperty("account_id")
        private Integer accountId;
        @JsonProperty("privacy")
        private String privacy;
        @JsonProperty("layout")
        private String layout;
        @JsonProperty("views")
        private Integer views;
        @JsonProperty("link")
        private String link;
        @JsonProperty("favorite")
        private Boolean favorite;
        @JsonProperty("nsfw")
        private Boolean nsfw;
        @JsonProperty("section")
        private Object section;
        @JsonProperty("images_count")
        private Integer imagesCount;
        @JsonProperty("in_gallery")
        private Boolean inGallery;
        @JsonProperty("is_ad")
        private Boolean isAd;
        @JsonProperty("include_album_ads")
        private Boolean includeAlbumAds;
        @JsonProperty("is_album")
        private Boolean isAlbum;
        @JsonProperty("deletehash")
        private String deletehash;
        @JsonProperty("images")
        private List<Image> images = null;
        @JsonProperty("ad_config")
        private AdConfig adConfig;

    }
    @lombok.Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Image {

        @JsonProperty("id")
        private String id;
        @JsonProperty("title")
        private Object title;
        @JsonProperty("description")
        private Object description;
        @JsonProperty("datetime")
        private Integer datetime;
        @JsonProperty("type")
        private String type;
        @JsonProperty("animated")
        private Boolean animated;
        @JsonProperty("width")
        private Integer width;
        @JsonProperty("height")
        private Integer height;
        @JsonProperty("size")
        private Integer size;
        @JsonProperty("views")
        private Integer views;
        @JsonProperty("bandwidth")
        private Integer bandwidth;
        @JsonProperty("vote")
        private Object vote;
        @JsonProperty("favorite")
        private Boolean favorite;
        @JsonProperty("nsfw")
        private Object nsfw;
        @JsonProperty("section")
        private Object section;
        @JsonProperty("account_url")
        private Object accountUrl;
        @JsonProperty("account_id")
        private Object accountId;
        @JsonProperty("is_ad")
        private Boolean isAd;
        @JsonProperty("in_most_viral")
        private Boolean inMostViral;
        @JsonProperty("has_sound")
        private Boolean hasSound;
        @JsonProperty("tags")
        private List<Object> tags = null;
        @JsonProperty("ad_type")
        private Integer adType;
        @JsonProperty("ad_url")
        private String adUrl;
        @JsonProperty("edited")
        private String edited;
        @JsonProperty("in_gallery")
        private Boolean inGallery;
        @JsonProperty("deletehash")
        private String deletehash;
        @JsonProperty("name")
        private Object name;
        @JsonProperty("link")
        private String link;

    }
}
