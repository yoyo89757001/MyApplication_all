package megvii.testfacepass.beans;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class CuoWuBean {

    @Id
    private Long id;
    private String filePath;
    private boolean isUplod;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public boolean isUplod() {
        return isUplod;
    }

    public void setUplod(boolean uplod) {
        isUplod = uplod;
    }
}

