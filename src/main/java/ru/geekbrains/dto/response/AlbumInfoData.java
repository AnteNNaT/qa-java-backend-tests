package ru.geekbrains.dto.response;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;


    @JsonInclude(JsonInclude.Include.NON_NULL)
    @lombok.Data
    public class AlbumInfoData {

        @JsonProperty("id")
        public String id;
        @JsonProperty("title")
        public String title;
        @JsonProperty("description")
        public String description;
        @JsonProperty("datetime")
        public Integer datetime;
        @JsonProperty("cover")
        public String cover;
        @JsonProperty("cover_edited")
        public Object coverEdited;
        @JsonProperty("cover_width")
        public Integer coverWidth;
        @JsonProperty("cover_height")
        public Integer coverHeight;
        @JsonProperty("account_url")
        public String accountUrl;
        @JsonProperty("account_id")
        public Integer accountId;
        @JsonProperty("privacy")
        public String privacy;
        @JsonProperty("layout")
        public String layout;
        @JsonProperty("views")
        public Integer views;
        @JsonProperty("link")
        public String link;
        @JsonProperty("favorite")
        public Boolean favorite;
        @JsonProperty("nsfw")
        public Boolean nsfw;
        @JsonProperty("section")
        public Object section;
        @JsonProperty("images_count")
        public Integer imagesCount;
        @JsonProperty("in_gallery")
        public Boolean inGallery;
        @JsonProperty("is_ad")
        public Boolean isAd;
        @JsonProperty("include_album_ads")
        public Boolean includeAlbumAds;
        @JsonProperty("is_album")
        public Boolean isAlbum;
        @JsonProperty("deletehash")
        public String deletehash;
        @JsonProperty("images")
        public List<ImageInfoResponse> imageInfoResponses = null;
        @JsonProperty("ad_config")
        public AdConfig adConfig;

    }
