package objects;

public class User {

    private String mUsername;
    private String mFirstName;
    private String mLastName;
    private int mId;
    private String mImage;
    private String mDateJoined;
    private String mStatus;
    private SubscriptionTier mSubscriptionTier = SubscriptionTier.BASIC;

    public User(String mUsername, String mFirstName, String mLastName,
                int mId, String mImage, String mDateJoined, String mStatus,
                SubscriptionTier tier) {
        this.mUsername = mUsername;
        this.mFirstName = mFirstName;
        this.mLastName = mLastName;
        this.mId = mId;
        this.mImage = mImage;
        this.mDateJoined = mDateJoined;
        this.mStatus = mStatus;
        this.mSubscriptionTier = tier;
    }

    public User(String mUsername, String mFirstName, String mLastName,
                int mId, String mImage, String mDateJoined, String mStatus) {
        this(mUsername, mFirstName, mLastName, mId, mImage, mDateJoined, mStatus,
                SubscriptionTier.BASIC);
    }

    public User(String firstName, String image) {
        this.mFirstName = firstName;
        this.mImage = image;
    }

    public String getUsername() {
        return mUsername;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public int getId() {
        return mId;
    }

    public String getImage() {
        return mImage;
    }

    public String getDateJoined() {
        return mDateJoined;
    }

    public String getStatus() {
        return mStatus;
    }

    public SubscriptionTier getSubscriptionTier() {
        return mSubscriptionTier;
    }

    public void setSubscriptionTier(SubscriptionTier tier) {
        this.mSubscriptionTier = tier;
    }
}
