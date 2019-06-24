package com.southtech.thSDK;

public class lib
{
  static
  {
    try
    {
      System.loadLibrary("IOTCAPIs");
      System.loadLibrary("AVAPIs");
      System.loadLibrary("thSDK");
    }
    catch (UnsatisfiedLinkError e)
    {
      System.out.println("library," + e.getMessage());
    }

  }

  public static native String jGetWlanIP();//查询路由器，比较耗时
  public static native int jtmp_Test();
  public static native String jGetLocalIP();

  public static native int jsmt_Init();
  public static native int jsmt_Stop();
  public static native int jsmt_Start(String SSID, String Password, String Tlv, String Target, int AuthMode);

  public static native int jthSearch_Init();
  public static native int jthSearch_SetCallBack(String ClassName, String FuncName, String ParamName);
  public static native int jthSearch_SetWiFiCfg(String SSID, String Password);
  public static native int jthSearch_SearchDevice();
  public static native int jthSearch_Free();

  //-----------------------------------------------------------------------------
  public static native int jaudio_CreatePlay();
  public static native int jaudio_Shutdown();
  public static native int jaudio_CreateTalk(int Chl);
  public static native int jaudio_StartTalk(int Chl);
  public static native int jaudio_StopTalk(int Chl);

  public static native int jopengl_Resize(int Chl, int w, int h);
  public static native int jopengl_Render(int Chl);

  public static native int jthNet_SetAudioIsMute(int Chl, int Value);// 1=mute 0=no mute
  public static native int jlocal_SnapShot(int Chl, String nFileName);//保存照片的方法
  public static native int jlocal_StartRec(int Chl, String nFilename);//保存录像的方法
  public static native int jlocal_StopRec(int Chl);//停止录像的方法

  public static native String jthNet_GetURL(int Chl, String nUID, String nUIDPsd, String nURL);
  public static native int jthNet_GetImage(int Chl, String nUID, String nUIDPsd, String nFileName);

  //public static native int jthNet_SetAlarmCallBack(String ClassName, String FuncName, String ParamName);
  public static native int jthNet_PlayLive(int Chl, String nDevIP, String nUserName, String nPassword, int nPort, int nStreamType);
  public static native int jthNet_PlayLiveP2P(int Chl, String nUID, String nUIDPsd, int nStreamType);
  public static native int jthNet_PlayRemote(int Chl, String nDevIP, String nUserName, String nPassword, String nFileName, int nPort);
  public static native int jthNet_PlayRemoteP2P(int Chl, String nUID, String NUIDPsd, String nFileName);
  public static native int jthNet_StopPlay(int Chl);
  public static native int jtmp_DisconnectP2P(int Chl);

  public static native int jthNet_SetResolution(int Chl, int nStreamType);

  public static native String jthNet_SearchDev_old();

  public static native int jthNet_MediaKeepAlive(int Chl); //发关键帧

}
