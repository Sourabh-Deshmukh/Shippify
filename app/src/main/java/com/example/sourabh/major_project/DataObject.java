package com.example.sourabh.major_project;

/**
 * Created by Shwetank on 6/10/2016.
 */


public class DataObject {
    private String mText1;
    private String mText2;
    private String mImg;

    DataObject (String text1, String text2, String text3){
        mText1 = text1;
        mText2 = text2;
        mImg = text3;
    }


    public String getmImg() {
        return mImg;
    }

    public void setmImg(String mImg) {
        this.mImg = mImg;
    }

    public String getmText1() {
        return mText1;
    }

    public void setmText1(String mText1) {
        this.mText1 = mText1;
    }

    public String getmText2() {
        return mText2;
    }

    public void setmText2(String mText2) {
        this.mText2 = mText2;
    }
}