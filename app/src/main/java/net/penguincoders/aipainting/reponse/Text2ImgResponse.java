package net.penguincoders.aipainting.reponse;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

public class Text2ImgResponse {
    @SerializedName("images")
    private String images;

    @SerializedName("parameters")
    private String parameters;

    @SerializedName("info")
    private String info;

    public String getImg() {
        return images;
    }
}
