package com.screening.uitls;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

//import com.huashi.bluetooth.HSBlueApi;

import java.lang.reflect.Method;
import java.util.Set;

public class BluetoothUtils {
    //    private SharedPreferences sharedPreferences;
//    private static final String DEFAULT_SP_NAME = "default_sp";
//    private HSBlueApi api;
    private Context context;
    private BluetoothAdapter bluetoothAdapter;

    public BluetoothUtils(Context context) {
        this.context = context.getApplicationContext();
        BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
    }

    //判断蓝牙状态
    public boolean getBluetooth() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return bluetoothAdapter.isEnabled();
    }

    //将连接的蓝牙信息保存到本地
    public void initSaveBlue(String key, String value) {
        SPUtils.put(context, key, value);
//        sharedPreferences=context.getSharedPreferences(DEFAULT_SP_NAME, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor=sharedPreferences.edit();
//        editor.putString(key,value);
//        editor.commit();
    }

    //获得本地保存的蓝牙信息
    public String initGetBlue(String key) {
        return (String) SPUtils.get(context, key, "");
//        sharedPreferences=context.getSharedPreferences(DEFAULT_SP_NAME,Context.MODE_PRIVATE);
//        return sharedPreferences.getString(key,"");
    }

    //判断蓝牙是否已连接
    public String initBlueLink() {
        Class<BluetoothAdapter> bluetoothAdapterClass = BluetoothAdapter.class;//得到BluetoothAdapter的Class对象
        try {//得到蓝牙状态的方法
            Method method = bluetoothAdapterClass.getDeclaredMethod("getConnectionState", (Class[]) null);
            //打开权限
            method.setAccessible(true);
            int state = (int) method.invoke(bluetoothAdapter, (Object[]) null);

            if (state == BluetoothAdapter.STATE_CONNECTED) {

                Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();

                for (BluetoothDevice device : devices) {

                    Method isConnectedMethod = BluetoothDevice.class.getDeclaredMethod("isConnected", (Class[]) null);
                    method.setAccessible(true);
                    boolean isConnected = (boolean) isConnectedMethod.invoke(device, (Object[]) null);

                    if (isConnected) {

                        return device.getAddress();
                    }

                }

            }

        } catch (Exception e) {

            Log.e("异常", e.getMessage().toString());
        }
        return null;
    }
}
