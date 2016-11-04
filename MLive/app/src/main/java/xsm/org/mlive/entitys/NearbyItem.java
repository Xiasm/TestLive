package xsm.org.mlive.entitys;

/**
 * Created by xsm on 16-11-4.
 */

public class NearbyItem {

    private String city;
    private String imgUrl;
    private String grade;

    public NearbyItem(String city, String grade) {
        this.city = city;
        this.grade = grade;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}
