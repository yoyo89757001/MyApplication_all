package megvii.testfacepass.beans;

import java.util.List;

public class CheckBean {


    /**
     * result : success
     * status : 200
     * StartIndex : 1
     * count : 1
     * TotalCount : 13
     * data : [{"username":"测试002","user_id":31,"operation":"0","update_time":"1541991357832","ic_number":"","extra_data":"AA0D01CA","face_img_url_1":"http://39.104.180.94:1080/uploads/WIN_20180510_16_00_57_Pro_1541991317113.jpg","face_img_url_2":"http://39.104.180.94:1080/uploads/WIN_20180827_09_38_45_Pro_1541991318980.jpg","face_img_url_3":"http://39.104.180.94:1080/uploads/WIN_20181017_13_41_52_Pro_1541991329612.jpg"}]
     */

    private String result;
    private int status;
    private int StartIndex;
    private int count;
    private int TotalCount;
    private List<DataBean> data;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStartIndex() {
        return StartIndex;
    }

    public void setStartIndex(int StartIndex) {
        this.StartIndex = StartIndex;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTotalCount() {
        return TotalCount;
    }

    public void setTotalCount(int TotalCount) {
        this.TotalCount = TotalCount;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * username : 测试002
         * user_id : 31
         * operation : 0
         * update_time : 1541991357832
         * ic_number :
         * extra_data : AA0D01CA
         * face_img_url_1 : http://39.104.180.94:1080/uploads/WIN_20180510_16_00_57_Pro_1541991317113.jpg
         * face_img_url_2 : http://39.104.180.94:1080/uploads/WIN_20180827_09_38_45_Pro_1541991318980.jpg
         * face_img_url_3 : http://39.104.180.94:1080/uploads/WIN_20181017_13_41_52_Pro_1541991329612.jpg
         */

        private String username;
        private Long user_id;
        private String operation;
        private String update_time;
        private String ic_number;
        private String extra_data;
        private String face_img_url_1;
        private String face_img_url_2;
        private String face_img_url_3;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public Long getUser_id() {
            return user_id;
        }

        public void setUser_id(Long user_id) {
            this.user_id = user_id;
        }

        public String getOperation() {
            return operation;
        }

        public void setOperation(String operation) {
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
}
