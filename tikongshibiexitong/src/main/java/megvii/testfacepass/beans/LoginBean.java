package megvii.testfacepass.beans;

public class LoginBean {


    /**
     * isLogin : true
     * token : eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOjk1ODAsImV4cCI6MTU0Mjc5NjM4Mjc3Mn0.su5Tv2VojWYWVLyR8L8fD0L9lXz_1EZ1Go_VSCQLwhA
     * msg : Success login!
     * user : {"id":9580,"email":"device@tikong.com","group_id":1,"type":"3","role_id":"17"}
     */

    private boolean isLogin;
    private String token;
    private String msg;
    private UserBean user;

    public boolean isIsLogin() {
        return isLogin;
    }

    public void setIsLogin(boolean isLogin) {
        this.isLogin = isLogin;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public static class UserBean {
        /**
         * id : 9580
         * email : device@tikong.com
         * group_id : 1
         * type : 3
         * role_id : 17
         */

        private int id;
        private String email;
        private int group_id;
        private String type;
        private String role_id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public int getGroup_id() {
            return group_id;
        }

        public void setGroup_id(int group_id) {
            this.group_id = group_id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getRole_id() {
            return role_id;
        }

        public void setRole_id(String role_id) {
            this.role_id = role_id;
        }
    }
}
