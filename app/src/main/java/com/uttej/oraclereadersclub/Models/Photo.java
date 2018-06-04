package com.uttej.oraclereadersclub.Models;

import java.util.List;

/**
 * Created by Clean on 27-03-2018.
 */

public class Photo {

    private String book_title;
    private String date_created;
    private String image_path;
    private String photo_id;
    private String user_id;
    private String genres;
    private String in_possession;
    private List<Request> requests;

    public Photo(){

    }

    public Photo(String book_title, String date_created, String image_path, String photo_id, String user_id, String genres, String in_possession, List<Request> requests) {
        this.book_title = book_title;
        this.date_created = date_created;
        this.image_path = image_path;
        this.photo_id = photo_id;
        this.user_id = user_id;
        this.genres = genres;
        this.in_possession = in_possession;
        this.requests = requests;
    }

    public String getBook_title() {
        return book_title;
    }

    public void setBook_title(String book_title) {
        this.book_title = book_title;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public String getPhoto_id() {
        return photo_id;
    }

    public void setPhoto_id(String photo_id) {
        this.photo_id = photo_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public String getIn_possession() {
        return in_possession;
    }

    public void setIn_possession(String in_possession) {
        this.in_possession = in_possession;
    }

    public List<Request> getRequests() {
        return requests;
    }

    public void setRequests(List<Request> requests) {
        this.requests = requests;
    }
}
