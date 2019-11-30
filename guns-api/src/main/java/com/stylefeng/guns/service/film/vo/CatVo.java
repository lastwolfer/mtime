package com.stylefeng.guns.service.film.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class CatVo implements Serializable {

    private static final long serialVersionUID = 3175155554118458343L;

    /**
     * catId : 1
     * catName : 爱情
     * active : false
     */
    private String catId;
    private String catName;
    private boolean active;

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getCatId() {
        return catId;
    }

    public String getCatName() {
        return catName;
    }

    public boolean isActive() {
        return active;
    }
}
