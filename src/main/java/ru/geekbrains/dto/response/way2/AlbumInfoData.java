package ru.geekbrains.dto.response.way2;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.geekbrains.dto.response.AdConfig;


@JsonInclude(JsonInclude.Include.NON_NULL)
    @lombok.Data
    public class AlbumInfoData{ //extends CommonResponse<AlbumInfoData>

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
        private List<ImageInfoData> imageInfoResponse = null;
        @JsonProperty("ad_config")
        private AdConfig adConfig;

    }
