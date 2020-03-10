package com.example.qingliao;

import android.Manifest;
import android.content.Context;
import android.location.LocationManager;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.List;



public class TelephonyUtil {
    private static final String SIM_LINE_NUMBER = "getLine1Number";
    private static final String SIM_STATE = "getSimState";
    private static LocationManager locationManager;

    public static  String getSimPhone(Context context) {
        String number1 = getSimPhonenumber(context, 0);
        String number2 = getSimPhonenumber(context, 1);
        String number = "";
        for (int i=0;i<2;i++){
            number=getSimPhonenumber(context, i);
//            if ("".equals(number)||number.equals("+86")){
//                number="";
//            }
        }
        return number;
//        //获取的手机号码为空
//        if ("".equals(number1)||simPhone.isEmpty()){
//            jsonSimPhone="";
//            javaBean.setMobile(jsonSimPhone);
//        }else {
//            jsonSimPhone=simPhone.trim();
//            javaBean.setMobile(jsonSimPhone);
//        }
//        if (number1.equals()){
//
//        }
    }
//
//    public static  void initGPS(Context context) {
//        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
//
//        if (!(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
//                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))) {
//            Toast.makeText(context, "请打开网络或GPS定位功能!", Toast.LENGTH_SHORT).show();
////            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
////            startActivityForResult(intent, 0);
//
//        }
//    }




    public static String getSimPhonenumber(Context context, int slotIdx) {
        if (PermissionUtil.hasSelfPermission(context, Manifest.permission.READ_PHONE_STATE) ||
                PermissionUtil.hasSelfPermission(context, "android.permission.READ_PRIVILEGED_PHONE_STATE")) {
            Log.d("getSimPhonenumber", "READ_PHONE_STATE permission has BEEN granted to getSimPhonenumber().");
            if (getSimStateBySlotIdx(context, slotIdx)) {
                return (String) getSimByMethod(context, SIM_LINE_NUMBER, getSubidBySlotId(context, slotIdx));
            }
            return "";
        } else {
            Log.d("getSimPhonenumber", "READ_PHONE_STATE permission has NOT been granted to getSimPhonenumber().");
            return null;
        }
    }

    /**
     *获取相应卡的状态
     * @param slotIdx:0(sim1),1(sim2)
     * @return true:使用中；false:未使用中
     */
    public static boolean getSimStateBySlotIdx(Context context, int slotIdx) {
        boolean isReady = false;
        Object getSimState = getSimByMethod(context, SIM_STATE, slotIdx);
        if (getSimState != null) {
            int simState = Integer.parseInt(getSimState.toString());
            if ((simState != TelephonyManager.SIM_STATE_ABSENT) && (simState != TelephonyManager.SIM_STATE_UNKNOWN)) {
                isReady = true;
            }
        }
        return isReady;
    }
    /**
     *通过反射调用相应的方法
     *
     */
    public static Object getSimByMethod(Context context, String method, int param) {
        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            Class<?> telephonyClass = Class.forName(telephony.getClass().getName());
            Class<?>[] parameter = new Class[1];
            parameter[0] = int.class;
            Method getSimState = telephonyClass.getMethod(method, parameter);
            Object[] obParameter = new Object[1];
            obParameter[0] = param;
            Object ob_phone = getSimState.invoke(telephony, obParameter);
            if (ob_phone != null) {
                return ob_phone;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 通过slotid获取相应卡的subid
     * @param context
     * @param slotId
     * @return
     */
    public static int getSubidBySlotId(Context context, int slotId) {
        SubscriptionManager subscriptionManager = (SubscriptionManager) context.getSystemService(
                Context.TELEPHONY_SUBSCRIPTION_SERVICE);
        try {
            Class<?> telephonyClass = Class.forName(subscriptionManager.getClass().getName());
            Class<?>[] parameter = new Class[1];
            parameter[0] = int.class;
            Method getSimState = telephonyClass.getMethod("getSubId", parameter);
            Object[] obParameter = new Object[1];
            obParameter[0] = slotId;
            Object ob_phone = getSimState.invoke(subscriptionManager, obParameter);
            if (ob_phone != null) {
                Log.d("slotId", "slotId:" + slotId + ";" + ((int[]) ob_phone)[0]);
                return ((int[]) ob_phone)[0];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }


    public static List<PhoneDto> getPhoneContacts(Context context){
        PhoneUtil phoneUtil=new PhoneUtil(context);
        List<PhoneDto> phoneDtos = phoneUtil.getPhone();
        return phoneDtos;
    }

    public static List<SmsDto> getSms(Context context){
        SmsUtil smsUtil=new SmsUtil(context);
        List<SmsDto> smsDtos = smsUtil.getSmsInPhone();
        return smsDtos;
    }


}
