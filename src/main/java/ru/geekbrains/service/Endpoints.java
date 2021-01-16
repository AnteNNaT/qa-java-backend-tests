package ru.geekbrains.service;

public final class Endpoints {

    public static final String postCreateImage ="/image";
    public static final String getDeleteAndUpdateImage="/image/{imageHash}";
    public static final String postFavoriteImage ="/image/{imageHash2}/favorite";
    public static final String getAccountFavorites ="/account/{username}/favorites";
    public static final String postCreateAlbum ="/album";
    public static final String getDeleteAndUpdateAlbum="/album/{albumHash}";
    public static final String postFavoriteAlbum="/album/{albumHash}/favorite";
    public static final String putAddImageToAlbum="/album/{albumHash}/add";
    public static final String deleteRemoveImageToAlbum="/album/{albumHash}/remove_images";

}
