package kr.co.edge.bosebose;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by snangwon on 2016-07-16.
 */
public class Item implements Serializable{

    @SerializedName("id")
    public int id;
    @SerializedName("created_at")
    private String regTime;
    @SerializedName("hit")
    private int hit;
    @SerializedName("like_count")
    private int likeCount;
    @SerializedName("category")
    private String category;
    @SerializedName("name")
    private String name;
    @SerializedName("storeID")
    public int storeID;
    @SerializedName("content")
    private String content;
    @SerializedName("price")
    private int price;
    @SerializedName("image1")
    private String image1;
    @SerializedName("image2")
    private String image2;
    @SerializedName("image3")
    private String image3;
    @SerializedName("image4")
    private String image4;

    @SerializedName("likeCheck")
    public int likeCheck;


    public String getRegTime() {
        return regTime;
    }

    public void setRegTime(String regTime) {
        this.regTime = regTime;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getStoreID() {
        return storeID;
    }

    public void setStoreID(int storeID) {
        this.storeID = storeID;
    }

    @SerializedName("tag")

    private String tag;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRegDate() {
        return regTime;
    }

    public void setRegDate(String regDate) {
        this.regTime = regDate;
    }

    public int getHit() {
        return hit;
    }

    public void setHit(int hit) {
        this.hit = hit;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public String getImage4() {
        return image4;
    }

    public void setImage4(String image4) {
        this.image4 = image4;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
