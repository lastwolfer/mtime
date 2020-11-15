package com.stylefeng.guns.rest.common.persistence.model;

public enum Gender {

    MAN(0,"男"),
    WOMEN(1,"女");

    private Integer num;
    private String sex;

    Gender(int i, String sex) {
        this.num = i;
        this.sex = sex;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
