package com.stylefeng.guns.service.seckill.vo;

import java.io.Serializable;

public class PublishPromoStockVo implements Serializable {

    private static final long serialVersionUID = 8544146082826057467L;
    /**
     * msg : 发布成功!
     * data :
     * totalPage :
     * imgPre :
     * nowPage :
     * status : 0
     */
    private String msg;
    private String data;
    private String totalPage;
    private String imgPre;
    private String nowPage;
    private int status;

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setTotalPage(String totalPage) {
        this.totalPage = totalPage;
    }

    public void setImgPre(String imgPre) {
        this.imgPre = imgPre;
    }

    public void setNowPage(String nowPage) {
        this.nowPage = nowPage;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public String getData() {
        return data;
    }

    public String getTotalPage() {
        return totalPage;
    }

    public String getImgPre() {
        return imgPre;
    }

    public String getNowPage() {
        return nowPage;
    }

    public int getStatus() {
        return status;
    }
}
