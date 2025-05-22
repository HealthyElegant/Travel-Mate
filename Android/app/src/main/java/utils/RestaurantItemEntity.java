package utils;

public class RestaurantItemEntity {
    private String mId;
    private String mImageUrl;
    private String mRestaurantName;
    private String mRestaurantAddress;
    private float mRatings;
    private int mVotes;
    private String mURL;
    private int mAvgCost;
    private int mPriceLevel;
    private boolean mHiddenGem;

    public RestaurantItemEntity(String id, String imageUrl, String name, String address,
                                float ratings, int votes, int avgCost, String restaurantURL,
                                int priceLevel, boolean hiddenGem) {
        this.mId = id;
        this.mImageUrl = imageUrl;
        this.mRestaurantName = name;
        this.mRestaurantAddress = address;
        this.mRatings = ratings;
        this.mVotes = votes;
        this.mAvgCost = avgCost;
        this.mURL = restaurantURL;
        this.mPriceLevel = priceLevel;
        this.mHiddenGem = hiddenGem;
    }

    public String getImage() {
        return mImageUrl;
    }

    public String getName() {
        return mRestaurantName;
    }

    public String getAddress() {
        return mRestaurantAddress;
    }

    public float getRatings() {
        return mRatings;
    }

    public int getVotes() {
        return mVotes;
    }

    public int getAvgCost() {
        return mAvgCost;
    }

    public int getPriceLevel() { return mPriceLevel; }

    public boolean isHiddenGem() { return mHiddenGem; }

    public String getURL() {
        return mURL;
    }

    public String getId() {
        return mId;
    }

}
