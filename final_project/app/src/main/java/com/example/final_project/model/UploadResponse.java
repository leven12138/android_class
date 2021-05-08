package com.example.final_project.model;

import com.google.gson.annotations.SerializedName;

public class UploadResponse {
    @SerializedName("result")
    public Share share;
    @SerializedName("success")
    public boolean success;
}
