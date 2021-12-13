package com.example.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;


public class MainActivity extends AppCompatActivity {
    //蓝牙适配器
    private BluetoothAdapter mBluetoothAdapter;
    //用来存放搜到的蓝牙
    private Set<BluetoothDevice> mDevices;
    private ListView mListView;
    private ArrayList mList;
    private ArrayAdapter mAdapter;
    private TextView mConnectedView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//自定义方法初始化UI控件
        initView();
        initData();
    }

    private void initData() {
//实例化蓝牙适配器
        mBluetoothAdapter =BluetoothAdapter.getDefaultAdapter();

    }

    private void initView() {
// mListView = (ListView) findViewById(R.id.lv_bluetooth_name);
// mConnectedView 指的是已被连接的（可用的蓝牙）
        mConnectedView = (TextView) findViewById(R.id.tv_bluetooth_connected);


    }

    public void onClick(View view) {
        if (view != null) {
            switch (view.getId()){
/**
 * 选择打开开启蓝牙。但是当选择它，蓝牙将不会被打开。
 * 事实上它会询问许可，以启用蓝牙。
 */

                case R.id.bt_bluetooth_on:
/**
 * 判断BluetoothAdapter 是否已经在准备状态，没有的话，就打开
 */
                    if (!mBluetoothAdapter.isEnabled()) {
//调用下列蓝牙ACTION_REQUEST_ENABLE的意图
                        Intent turnOn =new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(turnOn,0);
                        Toast.makeText(MainActivity.this,
                                "turn on", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(MainActivity.this,
                                "Already on", Toast.LENGTH_LONG).show();
                    }
                    break;
/**
 * 开启许可，允许其他蓝牙设备120秒内可以搜索到该设备
 * 选择设置可见按钮来打开视图。
 * 下面的屏幕会出现要求许可才能打开发现120秒
 */
                case R.id.bt_bluetooth_visible:
                    if (mBluetoothAdapter.isEnabled()) {
                        Intent getVisible = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                        startActivityForResult(getVisible,0);
                    }else{
                        Toast.makeText(MainActivity.this, "请先开启蓝牙", Toast.LENGTH_SHORT).show();
                    }



                    break;
/**
 * 选择列表中的设备选项。它会列出倒在列表视图中的配对设备。
 * 就我而言，只有一个配对设备。它如下所示。
 */
                case R.id.bt_bluetooth_list:

//可以通过调用 getBondedDevices()方法来获取配对设备列表
                    mDevices = mBluetoothAdapter.getBondedDevices();
//在这初始化ListView是为了方便刷新，显示数据
                    mListView = (ListView) findViewById(R.id.lv_bluetooth_name);
                    mList = new ArrayList();
                    if (mBluetoothAdapter.isEnabled()) {
                        for (BluetoothDevice bd :mDevices){
                            mList.add(bd.getName());
                        }
                        if (mList.size() != 0){
                            mConnectedView.setVisibility(View.VISIBLE);
                        }
                        Toast.makeText(MainActivity.this,
                                "Showing Paired Devices", Toast.LENGTH_SHORT).show();
                        mAdapter = new ArrayAdapter(
                                this,android.R.layout.simple_list_item_1,mList);
                        mListView.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                    }else{
                        Toast.makeText(MainActivity.this, "请先开启蓝牙", Toast.LENGTH_SHORT).show();
                    }


                    break;
/**
 * 选择关闭按钮来关闭蓝牙。
 * 当关掉蓝牙指示成功切换关闭蓝牙会出现以下消息
 */
                case R.id.bt_bluetooth_off:
//判断蓝牙是否关闭
                    if (mBluetoothAdapter.isEnabled()){
//未关闭
                        mBluetoothAdapter.disable();
                        mList.clear();
                        mConnectedView.setVisibility(View.INVISIBLE);
                        mAdapter.notifyDataSetChanged();

                    }

                    Toast.makeText(getApplicationContext(),"蓝牙已关闭" ,
                            Toast.LENGTH_LONG).show();

                    break;
                default:
                    break;
            }
        }

    }


}
