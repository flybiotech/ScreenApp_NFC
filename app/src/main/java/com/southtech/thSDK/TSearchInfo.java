package com.southtech.thSDK;

import java.io.Serializable;

public class TSearchInfo implements Serializable
{
  // TDevInfoPkt
  public long SN; // 序列号
  public String DevModal;// 设备型号 ='401H'
  public String SoftVersion;// 软件版本
  public String FileVersion;// 文件版本
  public String DevName;// 设备名称
  public int ExistWiFi;
  public int ExistSD;
  public int ExistFlash;
  public int HardType;// 硬件类型
  public long VideoTypeMask;
  public long StandardMask;
  public long SubStandardMask;
  public int PlatformType;
  public int ethLinkStatus;// 有线网络是否连接
  public int wifiStatus;
  public int upnpStatus;
  public int WlanStatus;
  public int p2pStatus;

  public int VideoChlCount;
  public int TimeZone;
  public int OEMType;
  public int DoubleStream;
  // TNetCfgPkt
  public int DataPort;
  public int rtspPort;
  public int HttpPort;
  public String DevIP;
  public String DevMAC;
  public String SubMask;
  public String Gateway;
  public String DNS1;
  public String DDNSDomain;
  // TWiFiCfgPkt
  public int WifiActive;
  public int IsAPMode;
  public String SSID_AP;
  public String Password_AP;
  public String SSID_STA;
  public String Password_STA;
  public int EncryptType;
  public int NetworkType;
  public int Auth;
  public int Enc;
  // Tp2pCfgPkt
  public int P2PActive;
  public String P2PUID;
  public String P2PPsd;
  public int p2pType;
  // TVideoCfgPkt
  public int Standard; // 制式 PAL=1, NTSC=0 default=0xff
  public int VideoType; // MPEG4=0x00, MJPEG=0x01 H264=0x02
  public int IPInterval; // IP帧间隔 1-120 default 30
  public int IsMirror; // 水平翻转 false or true
  public int IsFlip; // 垂直翻转 false or true
  public int IsWDR;
  public int IRCutSensitive;
  public int Width0; // 宽 720 360 180 704 352 176 640 320 160
  public int Height0; // 高 480 240 120 576 288 144
  public int FrameRate0; // 帧率 1-30 MAX:PAL 25 NTSC 30
  public int BitRateType0; // 0定码流 1定画质
  public int BitRateQuant0; // 画质 0..4
  public int BitRate0; // 码流 64K 128K 256K 512K 1024K 1536K 2048K 2560K 3072K
  public int Width1; // 宽 720 360 180 704 352 176 640 320 160
  public int Height1; // 高 480 240 120 576 288 144
  public int FrameRate1;// 帧率 1-30 MAX:PAL 25 NTSC 30
  public int BitRateType1;// 0定码流 1定画质
  public int BitRateQuant1;// 画质 0..4
  public int BitRate1;// 码流 64K 128K 256K 512K 1024K 1536K 2048K 2560K 3072K

  public int Brightness; // 亮度 0-255
  public int Contrast; // 对比度 0-255
  public int Hue; // 色度 0-255
  public int Saturation; // 饱和度 0-255
  public int Sharpness;
  public int BrightnessNight; // 亮度 0-255
  public int ContrastNight; // 对比度 0-255
  public int HueNight; // 色度 0-255
  public int SaturationNight; // 饱和度 0-255
  public int SharpnessNight;

  public int AudioActive;
  public int wFormatTag; // PCM=0x01, G711 = 0x02
  public int nChannels; // 单声道=0 立体声=1
  public int nSamplesPerSec; // 采样率
  public int wBitsPerSample; // number of bits per sample of mono data

  public int InputType; // 0 MIC输入, 1 LINE输入
  public int VolumeLineIn;
  public int VolumeLineOut;
  public int SoundTriggerActive;
  public int SoundTriggerSensitive;
  public int IsAudioGain;
  // TUserCfgPkt
  public String UserName;
  public String Password;


  public TSearchInfo()
  {
  }

}
