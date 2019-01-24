package com.bw.movie.activity;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.bw.movie.R;
import com.bw.movie.mvp.presenter.PresenterImpl;
import com.bw.movie.mvp.view.IView;
import com.bw.movie.util.CircularLoading;
import com.bw.movie.util.NetUtil;
import com.bw.movie.util.ToastUtil;
import android.app.Dialog;
import java.util.Map;

/**
  * @作者 GXY
  * @创建日期 2019/1/24 8:59
  * @描述 Activity基类
  *
  */
public abstract class BaseActivity extends AppCompatActivity implements IView {
    private PresenterImpl presenter;
    private Dialog mCircularLoading;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * 页面增加一个判断，因为4.4版本之前没有沉浸式可言
         * */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        //加载布局
        setContentView(getLayoutResId());
        //初始化view
        initView(savedInstanceState);
        //初始化数据
        initData();
        presenter = new PresenterImpl(this);
        stateNetWork();
    }
    /**
     * post请求
     * */
    protected void doNetWorkPostRequest(String url, Map<String,String> map,Class clazz){
        if(presenter!=null){
            //TODO:弹出等待的loding圈
            mCircularLoading = CircularLoading.showLoadDialog(this, "加载中...", true);
            presenter.requestPost(url,map,clazz);
        }
    }
    /**
     * get请求
     * */
    protected void doNetWorkGetRequest(String url,Class clazz){
        if(presenter!=null){
            //TODO:弹出等待的loding圈
            mCircularLoading = CircularLoading.showLoadDialog(this, "加载中...", true);
            presenter.requestGet(url,clazz);
        }
    }
    /**
     * v层请求成功的方法
     * */
    @Override
    public void requestSuccess(Object o) {
        //TODO：收起
        CircularLoading.closeDialog(mCircularLoading);
        netSuccess(o);
    }
    /**
     * v层请求失败的方法
     * */
    @Override
    public void requestFail(String error) {
        //TODO:收起
        CircularLoading.closeDialog(mCircularLoading);
        if(error.equals("hsanetwork")){
            ToastUtil.showToast(getResources().getString(R.string.no_net_work));
            NetUtil.showNotNetWork(this);
        }
        netFail(error);
    }

    /**
     * 初始化数据
     * */
    protected abstract void initData();

    /**
     * 初始化view
     * */
    protected abstract void initView(Bundle savedInstanceState);
    /**
     * 加载布局
     * */
    protected abstract int getLayoutResId();
    /**
     * 成功
     * */
    protected abstract void netSuccess(Object object);
    /**
     * 失败
     * */
    protected abstract void netFail(String s);
    /***
     * 解绑防止
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(presenter!=null){
            presenter.onDetach();
        }
    }



    //动态注册权限
    private void stateNetWork() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            String[] mStatenetwork = new String[]{
                    //写的权限
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    //读的权限
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    //入网权限
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    //WIFI权限
                    Manifest.permission.ACCESS_WIFI_STATE,
                    //读手机权限
                    Manifest.permission.READ_PHONE_STATE,
                    //网络权限
                    Manifest.permission.INTERNET,
                    //相机
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_APN_SETTINGS,
                    Manifest.permission.ACCESS_NETWORK_STATE,
            };
            ActivityCompat.requestPermissions(this,mStatenetwork,100);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean hasPermission  = false;
        if(requestCode == 100){
            for (int i = 0;i<grantResults.length;i++){
                if(grantResults[i] == -1){
                    hasPermission = true;
                }
            }
        }
    }
}
