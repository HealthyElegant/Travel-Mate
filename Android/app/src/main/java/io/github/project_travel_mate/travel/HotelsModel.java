package io.github.project_travel_mate.travel;

class HotelsModel {
    private String mTitle;
    private String mAddress;
    private String mPhone;
    private String mHref;
    private int mDistance;
    private double mLatitude;
    private double mLongitude;

    HotelsModel(String mTitle, String mAddress, String mPhone, String mHref, int mDistance,
                double mLatitude, double mLongitude) {
        this.mTitle = mTitle;
        this.mAddress = mAddress;
        this.mPhone = mPhone;
        this.mHref = mHref;
        this.mDistance = mDistance;
        this.mLatitude = mLatitude;
        this.mLongitude = mLongitude;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getAddress() {
        return mAddress;
    }

    public String getPhone() {
        return mPhone;
    }

    public String getHref() {
        return mHref;
    }

    public int getDistance() {
        return mDistance;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }
}
