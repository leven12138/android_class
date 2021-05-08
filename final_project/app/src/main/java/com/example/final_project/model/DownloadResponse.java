package com.example.final_project.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DownloadResponse {
    @SerializedName("feeds")
    public List<Share> feeds;
    @SerializedName("success")
    public boolean success;
}
