package com.fourshape.numbermazes.rv_adapter;

public class ContentModal {

    private final int contentType;

    private int appLogoId;
    private String appTitle, appPackage;

    public ContentModal (int contentType) {
        this.contentType = contentType;
    }

    public ContentModal (int contentType, int appLogoId, String appTitle, String appPackage) {
        this.contentType = contentType;
        this.appLogoId = appLogoId;
        this.appTitle = appTitle;
        this.appPackage = appPackage;
    }

    public int getContentType() {
        return contentType;
    }

    public int getAppLogoId() {
        return appLogoId;
    }

    public String getAppPackage() {
        return appPackage;
    }

    public String getAppTitle() {
        return appTitle;
    }
}
