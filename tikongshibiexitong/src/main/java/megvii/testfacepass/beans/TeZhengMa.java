package megvii.testfacepass.beans;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class TeZhengMa {

    @Id
    private Long id;
    private String tezhengma;
    private Long uuid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTezhengma() {
        return tezhengma;
    }

    public void setTezhengma(String tezhengma) {
        this.tezhengma = tezhengma;
    }

    public Long getUuid() {
        return uuid;
    }

    public void setUuid(Long uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return "TeZhengMa{" +
                "id=" + id +
                ", tezhengma='" + tezhengma + '\'' +
                ", uuid=" + uuid +
                '}';
    }
}
