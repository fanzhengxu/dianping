package com.fzx.dianping.model;

import java.math.BigDecimal;
import java.util.Date;

public class ShopModel {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_shop.id
     *
     * @mbg.generated Sat Feb 15 19:14:58 CST 2020
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_shop.created_at
     *
     * @mbg.generated Sat Feb 15 19:14:58 CST 2020
     */
    private Date createdAt;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_shop.updated_at
     *
     * @mbg.generated Sat Feb 15 19:14:58 CST 2020
     */
    private Date updatedAt;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_shop.name
     *
     * @mbg.generated Sat Feb 15 19:14:58 CST 2020
     */
    private String name;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_shop.remark_score
     *
     * @mbg.generated Sat Feb 15 19:14:58 CST 2020
     */
    private BigDecimal remarkScore;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_shop.price_per_man
     *
     * @mbg.generated Sat Feb 15 19:14:58 CST 2020
     */
    private Integer pricePerMan;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_shop.latitude
     *
     * @mbg.generated Sat Feb 15 19:14:58 CST 2020
     */
    private BigDecimal latitude;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_shop.longitude
     *
     * @mbg.generated Sat Feb 15 19:14:58 CST 2020
     */
    private BigDecimal longitude;

    private Integer distance;

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_shop.category_id
     *
     * @mbg.generated Sat Feb 15 19:14:58 CST 2020
     */
    private Integer categoryId;

    private CategoryModel categoryModel;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_shop.tags
     *
     * @mbg.generated Sat Feb 15 19:14:58 CST 2020
     */
    private String tags;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_shop.start_time
     *
     * @mbg.generated Sat Feb 15 19:14:58 CST 2020
     */
    private String startTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_shop.end_time
     *
     * @mbg.generated Sat Feb 15 19:14:58 CST 2020
     */
    private String endTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_shop.address
     *
     * @mbg.generated Sat Feb 15 19:14:58 CST 2020
     */
    private String address;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_shop.seller_id
     *
     * @mbg.generated Sat Feb 15 19:14:58 CST 2020
     */
    private Integer sellerId;

    private SellerModel sellerModel;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_shop.icon_url
     *
     * @mbg.generated Sat Feb 15 19:14:58 CST 2020
     */
    private String iconUrl;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_shop.id
     *
     * @return the value of t_shop.id
     *
     * @mbg.generated Sat Feb 15 19:14:58 CST 2020
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_shop.id
     *
     * @param id the value for t_shop.id
     *
     * @mbg.generated Sat Feb 15 19:14:58 CST 2020
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_shop.created_at
     *
     * @return the value of t_shop.created_at
     *
     * @mbg.generated Sat Feb 15 19:14:58 CST 2020
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_shop.created_at
     *
     * @param createdAt the value for t_shop.created_at
     *
     * @mbg.generated Sat Feb 15 19:14:58 CST 2020
     */
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_shop.updated_at
     *
     * @return the value of t_shop.updated_at
     *
     * @mbg.generated Sat Feb 15 19:14:58 CST 2020
     */
    public Date getUpdatedAt() {
        return updatedAt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_shop.updated_at
     *
     * @param updatedAt the value for t_shop.updated_at
     *
     * @mbg.generated Sat Feb 15 19:14:58 CST 2020
     */
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_shop.name
     *
     * @return the value of t_shop.name
     *
     * @mbg.generated Sat Feb 15 19:14:58 CST 2020
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_shop.name
     *
     * @param name the value for t_shop.name
     *
     * @mbg.generated Sat Feb 15 19:14:58 CST 2020
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_shop.remark_score
     *
     * @return the value of t_shop.remark_score
     *
     * @mbg.generated Sat Feb 15 19:14:58 CST 2020
     */
    public BigDecimal getRemarkScore() {
        return remarkScore;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_shop.remark_score
     *
     * @param remarkScore the value for t_shop.remark_score
     *
     * @mbg.generated Sat Feb 15 19:14:58 CST 2020
     */
    public void setRemarkScore(BigDecimal remarkScore) {
        this.remarkScore = remarkScore;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_shop.price_per_man
     *
     * @return the value of t_shop.price_per_man
     *
     * @mbg.generated Sat Feb 15 19:14:58 CST 2020
     */
    public Integer getPricePerMan() {
        return pricePerMan;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_shop.price_per_man
     *
     * @param pricePerMan the value for t_shop.price_per_man
     *
     * @mbg.generated Sat Feb 15 19:14:58 CST 2020
     */
    public void setPricePerMan(Integer pricePerMan) {
        this.pricePerMan = pricePerMan;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_shop.latitude
     *
     * @return the value of t_shop.latitude
     *
     * @mbg.generated Sat Feb 15 19:14:58 CST 2020
     */
    public BigDecimal getLatitude() {
        return latitude;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_shop.latitude
     *
     * @param latitude the value for t_shop.latitude
     *
     * @mbg.generated Sat Feb 15 19:14:58 CST 2020
     */
    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_shop.longitude
     *
     * @return the value of t_shop.longitude
     *
     * @mbg.generated Sat Feb 15 19:14:58 CST 2020
     */
    public BigDecimal getLongitude() {
        return longitude;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_shop.longitude
     *
     * @param longitude the value for t_shop.longitude
     *
     * @mbg.generated Sat Feb 15 19:14:58 CST 2020
     */
    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_shop.category_id
     *
     * @return the value of t_shop.category_id
     *
     * @mbg.generated Sat Feb 15 19:14:58 CST 2020
     */
    public Integer getCategoryId() {
        return categoryId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_shop.category_id
     *
     * @param categoryId the value for t_shop.category_id
     *
     * @mbg.generated Sat Feb 15 19:14:58 CST 2020
     */
    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_shop.tags
     *
     * @return the value of t_shop.tags
     *
     * @mbg.generated Sat Feb 15 19:14:58 CST 2020
     */
    public String getTags() {
        return tags;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_shop.tags
     *
     * @param tags the value for t_shop.tags
     *
     * @mbg.generated Sat Feb 15 19:14:58 CST 2020
     */
    public void setTags(String tags) {
        this.tags = tags == null ? null : tags.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_shop.start_time
     *
     * @return the value of t_shop.start_time
     *
     * @mbg.generated Sat Feb 15 19:14:58 CST 2020
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_shop.start_time
     *
     * @param startTime the value for t_shop.start_time
     *
     * @mbg.generated Sat Feb 15 19:14:58 CST 2020
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime == null ? null : startTime.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_shop.end_time
     *
     * @return the value of t_shop.end_time
     *
     * @mbg.generated Sat Feb 15 19:14:58 CST 2020
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_shop.end_time
     *
     * @param endTime the value for t_shop.end_time
     *
     * @mbg.generated Sat Feb 15 19:14:58 CST 2020
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime == null ? null : endTime.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_shop.address
     *
     * @return the value of t_shop.address
     *
     * @mbg.generated Sat Feb 15 19:14:58 CST 2020
     */
    public String getAddress() {
        return address;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_shop.address
     *
     * @param address the value for t_shop.address
     *
     * @mbg.generated Sat Feb 15 19:14:58 CST 2020
     */
    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_shop.seller_id
     *
     * @return the value of t_shop.seller_id
     *
     * @mbg.generated Sat Feb 15 19:14:58 CST 2020
     */
    public Integer getSellerId() {
        return sellerId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_shop.seller_id
     *
     * @param sellerId the value for t_shop.seller_id
     *
     * @mbg.generated Sat Feb 15 19:14:58 CST 2020
     */
    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_shop.icon_url
     *
     * @return the value of t_shop.icon_url
     *
     * @mbg.generated Sat Feb 15 19:14:58 CST 2020
     */
    public String getIconUrl() {
        return iconUrl;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_shop.icon_url
     *
     * @param iconUrl the value for t_shop.icon_url
     *
     * @mbg.generated Sat Feb 15 19:14:58 CST 2020
     */
    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl == null ? null : iconUrl.trim();
    }

    public CategoryModel getCategoryModel() {
        return categoryModel;
    }

    public void setCategoryModel(CategoryModel categoryModel) {
        this.categoryModel = categoryModel;
    }

    public SellerModel getSellerModel() {
        return sellerModel;
    }

    public void setSellerModel(SellerModel sellerModel) {
        this.sellerModel = sellerModel;
    }
}