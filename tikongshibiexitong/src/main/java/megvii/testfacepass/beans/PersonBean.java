package megvii.testfacepass.beans;



import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class PersonBean {


        /**
         * username : 李
         * user_id : 1
         * operation : 0
         * update_time : 1541467811000
         * ic_number :
         * extra_data : dh22334543454
         * face_img_url_1 : http://39.104.180.94:1080/uploads/WIN_20180827_09_38_45_Pro_1541560305380.jpg
         * face_img_url_2 : http://39.104.180.94:1080/uploads/WIN_20180510_16_00_57_Pro_1541560317614.jpg
         * face_img_url_3 : http://39.104.180.94:1080/uploads/pic_1541468003132.jpg
         */


       @Id(assignable = true)
        private Long user_id;
        private String username;
        private int operation;
        private String update_time;
        private String ic_number;
        private String extra_data;
        private String face_img_url_1;
        private String face_img_url_2;
        private String face_img_url_3;
        private boolean isDonwold;
        private boolean isPhoto;

    public boolean isPhoto() {
        return isPhoto;
    }

    public void setPhoto(boolean photo) {
        isPhoto = photo;
    }

    public boolean isDonwold() {
            return isDonwold;
        }

        public void setDonwold(boolean donwold) {
            isDonwold = donwold;
        }

    public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public long getUser_id() {
            return user_id;
        }

        public void setUser_id(long user_id) {
            this.user_id = user_id;
        }

        public int getOperation() {
            return operation;
        }

        public void setOperation(int operation) {
            this.operation = operation;
        }

        public String getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(String update_time) {
            this.update_time = update_time;
        }

        public String getIc_number() {
            return ic_number;
        }

        public void setIc_number(String ic_number) {
            this.ic_number = ic_number;
        }

        public String getExtra_data() {
            return extra_data;
        }

        public void setExtra_data(String extra_data) {
            this.extra_data = extra_data;
        }

        public String getFace_img_url_1() {
            return face_img_url_1;
        }

        public void setFace_img_url_1(String face_img_url_1) {
            this.face_img_url_1 = face_img_url_1;
        }

        public String getFace_img_url_2() {
            return face_img_url_2;
        }

        public void setFace_img_url_2(String face_img_url_2) {
            this.face_img_url_2 = face_img_url_2;
        }

        public String getFace_img_url_3() {
            return face_img_url_3;
        }

        public void setFace_img_url_3(String face_img_url_3) {
            this.face_img_url_3 = face_img_url_3;
        }

}
