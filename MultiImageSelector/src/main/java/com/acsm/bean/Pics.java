package com.acsm.bean;

/**
 * Created by xiaoma on 2017/4/24.
 */

public class Pics {
    
    private String pic;
    private boolean hasChoose;

    public Pics(String pic, boolean hasChoose) {
        this.pic = pic;
        this.hasChoose = hasChoose;
    }

    public String getPic() {
        return pic;
    }

    public boolean isHasChoose() {
        return hasChoose;
    }

    public void setHasChoose(boolean hasChoose) {
        this.hasChoose = hasChoose;
    }
}
