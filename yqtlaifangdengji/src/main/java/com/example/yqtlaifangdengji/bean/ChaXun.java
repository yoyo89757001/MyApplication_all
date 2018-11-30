package com.example.yqtlaifangdengji.bean;

import java.util.List;

public class ChaXun {


    /**
     * count : 1
     * list : [{"departmentName":"研发部门","name":"欧阳","id":"1068448411966599168","displayPhoto":"http://192.168.2.189:8980/userfiles/smallAvatar/employee/1068448411966599168.jpg"}]
     */

    private int count;
    private List<ListBean> list;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * departmentName : 研发部门
         * name : 欧阳
         * id : 1068448411966599168
         * displayPhoto : http://192.168.2.189:8980/userfiles/smallAvatar/employee/1068448411966599168.jpg
         */

        private String departmentName;
        private String name;
        private String id;
        private String displayPhoto;

        public String getDepartmentName() {
            return departmentName;
        }

        public void setDepartmentName(String departmentName) {
            this.departmentName = departmentName;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDisplayPhoto() {
            return displayPhoto;
        }

        public void setDisplayPhoto(String displayPhoto) {
            this.displayPhoto = displayPhoto;
        }
    }
}
