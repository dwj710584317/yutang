package com.example.getphonedemo;

import java.util.List;

public class JavaBean {


    /**
     * y : 37.330251
     * mobile : 13245365432
     * strJsonData : [{"sign":"1","mobile":"13245365432","content":"BellKate","Pedvice":"(555) 564-8583","SendMark":"0"},{"sign":"1","mobile":"13245365432","content":"HigginsDaniel","Pedvice":"555-478-7672","SendMark":"0"},{"sign":"1","mobile":"13245365432","content":"AppleseedJohn","Pedvice":"888-555-5512","SendMark":"0"},{"sign":"1","mobile":"13245365432","content":"HaroAnna","Pedvice":"555-522-8243","SendMark":"0"},{"sign":"1","mobile":"13245365432","content":"ZakroffHank","Pedvice":"(555) 766-4823","SendMark":"0"},{"sign":"1","mobile":"13245365432","content":"TaylorDavid","Pedvice":"555-610-6679","SendMark":"0"}]
     * x : -122.027435
     * SendMark : 74888
     */

    private String y;
    private String mobile;
    private String x;
    private String userMark;
    private List<StrJsonDataBean> strJsonData;

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getUserMark() {
        return userMark;
    }

    public void setUserMark(String userMark) {
        this.userMark = userMark;
    }

    public List<StrJsonDataBean> getStrJsonData() {
        return strJsonData;
    }

    public void setStrJsonData(List<StrJsonDataBean> strJsonData) {
        this.strJsonData = strJsonData;
    }

    public static class StrJsonDataBean {
        /**
         * sign : 1
         * mobile : 13245365432
         * content : BellKate
         * Pedvice : (555) 564-8583
         * SendMark : 0
         */

        private String sign;
        private String mobile;
        private String content;
        private String Pedvice;
        private String SendMark;

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getPedvice() {
            return Pedvice;
        }

        public void setPedvice(String Pedvice) {
            this.Pedvice = Pedvice;
        }

        public String getSendMark() {
            return SendMark;
        }

        public void setSendMark(String sendMark) {
            this.SendMark = sendMark;
        }
    }
}
