package reggae_beats.com.reggaebeats;

public class FriendsItem {
    private String friendImage;
    private String friendName;

    public FriendsItem(String friendImage, String friendName) {
        this.friendImage = friendImage;
        this.friendName = friendName;
    }

    public String getFriendImage() {
        return friendImage;
    }

    public void setFriendImage(String friendImage) {
        this.friendImage = friendImage;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }
}
