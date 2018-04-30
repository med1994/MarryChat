package mohamed.com.marrychat;

/**
 * Created by mohamed on 11/07/2017.
 */

public class Users {
    public String name;
    public String image;
    public String status;
    public String thum_img;

    public Users() {

    }

    public Users(String name, String image, String status, String thum_img) {
        this.name = name;
        this.image = image;
        this.status = status;
        this.thum_img=thum_img;
    }

    public String getThum_img() {
        return thum_img;
    }

    public void setThum_img(String thum_img) {
        this.thum_img = thum_img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
