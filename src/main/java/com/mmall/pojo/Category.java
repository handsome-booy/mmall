package com.mmall.pojo;

import java.util.Date;
import java.util.Objects;

/**
 * category是一个大的类别，不是具体的一个商品
 */
public class Category {
    private Integer id;

    private Integer parentId;

    private String categoryName;

    private Boolean status;

    private Integer sortOrder;

    private Date createTime;

    private Date uodateTime;

    public Category(Integer id, Integer parentId, String categoryName, Boolean status, Integer sortOrder, Date createTime, Date uodateTime) {
        this.id = id;
        this.parentId = parentId;
        this.categoryName = categoryName;
        this.status = status;
        this.sortOrder = sortOrder;
        this.createTime = createTime;
        this.uodateTime = uodateTime;
    }

    public Category() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName == null ? null : categoryName.trim();
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUodateTime() {
        return uodateTime;
    }

    public void setUodateTime(Date uodateTime) {
        this.uodateTime = uodateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(id, category.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}