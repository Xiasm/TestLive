package xsm.org.mlive.messages;

/**
 * Created by xsm on 16-11-6.
 */

public class RoomStateMessage {
    private boolean isQQShare;
    private boolean isQZoneShare;
    private boolean isWeiXinShare;
    private boolean isWeiXinQzone;
    private boolean isSinaWeiBoShare;

    private String roomTitle;

    public boolean isQQShare() {
        return isQQShare;
    }

    public void setQQShare(boolean QQShare) {
        isQQShare = QQShare;
    }

    public boolean isQZoneShare() {
        return isQZoneShare;
    }

    public void setQZoneShare(boolean QZoneShare) {
        isQZoneShare = QZoneShare;
    }

    public boolean isWeiXinShare() {
        return isWeiXinShare;
    }

    public void setWeiXinShare(boolean weiXinShare) {
        isWeiXinShare = weiXinShare;
    }

    public boolean isWeiXinQzone() {
        return isWeiXinQzone;
    }

    public void setWeiXinQzone(boolean weiXinQzone) {
        isWeiXinQzone = weiXinQzone;
    }

    public boolean isSinaWeiBoShare() {
        return isSinaWeiBoShare;
    }

    public void setSinaWeiBoShare(boolean sinaWeiBoShare) {
        isSinaWeiBoShare = sinaWeiBoShare;
    }

    public String getRoomTitle() {
        return roomTitle;
    }

    public void setRoomTitle(String roomTitle) {
        this.roomTitle = roomTitle;
    }

    @Override
    public String toString() {
        return "RoomStateMessage{" +
                "isQQShare=" + isQQShare +
                ", isQZoneShare=" + isQZoneShare +
                ", isWeiXinShare=" + isWeiXinShare +
                ", isWeiXinQzone=" + isWeiXinQzone +
                ", isSinaWeiBoShare=" + isSinaWeiBoShare +
                ", roomTitle='" + roomTitle + '\'' +
                '}';
    }
}
