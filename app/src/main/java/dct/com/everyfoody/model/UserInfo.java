package dct.com.everyfoody.model;

/**
 * Created by jyoung on 2017. 10. 16..
 */

public class UserInfo {

    private String email;
    private String uid;
    private int category;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }
}
