package com.screening.uitls;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.Toast;

import com.screening.model.Bean;
import com.screening.model.DoctorId;
import com.screening.model.Item;
import com.screening.model.ListMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.util.List;

public class BackupsLitepalUtils {

    private List<ListMessage> listMessages;

    private Thread thread;

    private Context mContext;

    private CopyUtils copyUtils;

    private String sdPath = "";

    private String TAG_e = "backup_e";

    public BackupsLitepalUtils(Context mContext) {
        this.mContext = mContext.getApplicationContext();
    }

    public void stopThread() {
        if (thread != null) {
            thread.interrupt();
        }
    }

    public void backUplitepalAdmin() {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                listMessages = LitePal.where("isCopy = ?", Constant.COPYCODE_1).find(ListMessage.class);
                if (listMessages.size() == 0) {
                    if (backupsResult != null) {
                        backupsResult.getBackupsResult(6, false);
                    }
                    return;
                }
                JSONObject jsonObject = new JSONObject();
                JSONArray jsonArray = new JSONArray();
                for (int i = 0; i < listMessages.size(); i++) {
                    JSONObject object = new JSONObject();
                    String pid = listMessages.get(i).getpId();//编号
                    String name = listMessages.get(i).getName();//姓名
                    String age = listMessages.get(i).getAge();//年龄
                    String IDCard = listMessages.get(i).getIdCard();//身份证
                    String phone = listMessages.get(i).getPhone();//手机
                    String Fixedtelephone = listMessages.get(i).getFixedtelephone();//固定电话
                    String sex = listMessages.get(i).getSex();//性别
                    String dataofbirth = listMessages.get(i).getDataofbirth();//出生日期
                    String Remarks = listMessages.get(i).getRemarks();//备注
                    String Occupation = listMessages.get(i).getOccupation();//职业
                    String smoking = listMessages.get(i).getSmoking();//吸烟史
                    String abortion = listMessages.get(i).getAbortion();//流产次数
                    String sexualpartners = listMessages.get(i).getSexualpartners();//性伙伴数
                    String Gestationaltimes = listMessages.get(i).getGestationaltimes();//孕次
                    String contraception = listMessages.get(i).getContraception();//避孕方式
                    String marry = listMessages.get(i).getMarry();//婚否
                    String picYaunTuPath = listMessages.get(i).getPicYaunTuPath();//原图路径
                    String picCuSuanPath = listMessages.get(i).getPicCuSuanPath();//醋酸白路径
                    String picDianYouPath = listMessages.get(i).getPicDianYouPath();//碘油路径
                    String vedioPath = listMessages.get(i).getVedioPath();//视频路径
                    String uploadScreenid = listMessages.get(i).getUploadScreenid();//是否已进行筛查
                    String HPV = listMessages.get(i).getHpv();//hpv
                    String cytology = listMessages.get(i).getCytology();//细胞学
                    String gene = listMessages.get(i).getGene();//基因
                    String DNA = listMessages.get(i).getDna();//dna
                    String other = listMessages.get(i).getOther();//其他
                    String Detailedaddress = listMessages.get(i).getDetailedaddress();//详细地址
                    String screeningId = listMessages.get(i).getScreeningId();//患者存放图像的目录
                    String Identification = listMessages.get(i).getIdentification();
                    int screenState = listMessages.get(i).getScreenState();
                    long dayTime = listMessages.get(i).getDayTime();
//                    String isCopy = listMessages.get(i).getIsCopy();
                    try {
                        object.put("pId", pid);
                        object.put("name", name);
                        object.put("age", age);
                        object.put("IDCard", IDCard);
                        object.put("phone", phone);
                        object.put("Fixedtelephone", Fixedtelephone);
                        object.put("sex", sex);
                        object.put("dataofbirth", dataofbirth);
                        object.put("Remarks", Remarks);
//                        object.put("parity",parity);
                        object.put("Occupation", Occupation);
                        object.put("smoking", smoking);
                        object.put("abortion", abortion);
                        object.put("sexualpartners", sexualpartners);
                        object.put("Gestationaltimes", Gestationaltimes);
                        object.put("contraception", contraception);
                        object.put("marry", marry);
                        object.put("picYaunTuPath", picYaunTuPath);
                        object.put("picCuSuanPath", picCuSuanPath);
                        object.put("picDianYouPath", picDianYouPath);
                        object.put("vedioPath", vedioPath);
//                        object.put("ScanTime",ScanTime);
                        object.put("uploadScreenid", uploadScreenid);
                        object.put("HPV", HPV);
                        object.put("cytology", cytology);
                        object.put("gene", gene);
                        object.put("DNA", DNA);
                        object.put("other", other);
                        object.put("Detailedaddress", Detailedaddress);
                        object.put("screeningId", screeningId);
                        object.put("Identification", Identification);

                        object.put("isCopy", Constant.COPYCODE_2);
                        object.put("screenState", screenState);
                        object.put("dayTime", dayTime);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    jsonArray.put(object);
                    listMessages.get(i).setIsCopy(Constant.COPYCODE_2);
                    listMessages.get(i).save();
                }
                try {
                    jsonObject.put("screenData", jsonArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    return;
                }

                //拼接SD卡路径
                File file = new File(picPath + listMessages.get(0).getpId() + "-" + listMessages.get(listMessages.size() - 1).getpId() + "筛查备份-" + DateUtils.getDate() + ".txt");
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                BufferedWriter bufferedWriter = null;
                try {
                    bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
                    bufferedWriter.write(jsonObject.toString());
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    if (backupsResult != null) {
                        backupsResult.getBackupsResult(1, true);
                    }
                } catch (Exception e) {
                    if (backupsResult != null) {
                        backupsResult.getBackupsResult(1, false);
                    }
                    Log.e("backupUtils00", e.getMessage().toString());
                } finally {

                }
            }
        });
        thread.start();
    }

    /**
     * 得到所有的存储路径（内部存储+外部存储）
     *
     * @param context
     * @return
     */
    public static String[] getAllSdPaths(Context context) {
        Method mMethodGetPaths = null;
        String[] paths = null;
        //通过调用类的实例mStorageManager的getClass()获取StorageManager类对应的Class对象
        //getMethod("getVolumePaths")返回StorageManager类对应的Class对象的getVolumePaths方法，这里不带参数
        StorageManager mStorageManager = (StorageManager) context
                .getSystemService(context.STORAGE_SERVICE);//storage
        try {
            mMethodGetPaths = mStorageManager.getClass().getMethod("getVolumePaths");
            paths = (String[]) mMethodGetPaths.invoke(mStorageManager);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return paths;
    }

    private List<DoctorId> doctorIds;
    String picPath = "";//

    //将需要复制的图片直接复制到SD卡
    public void copyToSd(String sdAviSize) {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                //从数据库中查询符合条件的患者
                copyUtils = new CopyUtils();
                int result = -2;//复制结果
                float size = 0;//本地文件大小
                String sdAvailableSize = "0";
                listMessages = LitePal.where("isCopy = ?", Constant.COPYCODE_0).find(ListMessage.class);
                if (listMessages.size() > 0) {
                    for (int i = 0; i < listMessages.size(); i++) {
                        //需要复制的原始文件路径
                        String filePath = Bean.bePath + listMessages.get(i).getScreeningId();
                        File file = new File(filePath);
                        if (file.exists()) {
                            //先判断本地文件大小
                            String fileSize = FileUtils.getAutoFilesSize(filePath);
                            //得到某个文件夹下文件的大小
                            size = size + Float.parseFloat(fileSize);
                        }
                    }
                    //查询结束后，开始计算文件总大小
                    if (sdAvailableSize.contains("吉")) {
                        sdAvailableSize = sdAviSize.substring(0, sdAviSize.indexOf("吉"));
                    } else {
                        sdAvailableSize = sdAviSize.substring(0, sdAviSize.indexOf("G"));
                    }
                    if (Double.parseDouble(sdAvailableSize) - size < 0.1) {
                        if (backupsResult != null) {
                            backupsResult.getBackupsResult(5, false);
                        }
                        Log.e("sd", "内存不足");
                    } else {
                        doctorIds = LitePal.findAll(DoctorId.class);
                        //SD卡上每次备份的路径
                        if (Item.getOneItem(mContext).getSdRootPath() == null) {
                            return;
                        }
                        //备份路径：SD卡根路径+该软件的包名路径+地区+图片和视频+当前时间
                        picPath = sdPath + Constant.sdPath + doctorIds.get(0).getDoctorId() + "_" + Constant.fileName + DateUtils.getDate() + "/";
                        File picFile = new File(picPath);
                        if (!picFile.exists()) {
                            picFile.mkdirs();
                        }
                        //开始循环复制
                        for (int i = 0; i < listMessages.size(); i++) {
                            //查询本地是否有这个患者的图片信息
                            String filePath = Bean.bePath + listMessages.get(i).getScreeningId();
                            File file = new File(filePath);
                            if (file.exists()) {
                                //将患者信息缓存下来，如果图片复制失败，就将信息保留下来
                                Constant.errorListMessage = listMessages.get(i);
                                result = copyUtils.copy(filePath, picPath + listMessages.get(i).getScreeningId() + "/");
                                if (result == 1) {
                                    listMessages.get(i).setIsCopy(Constant.COPYCODE_1);
                                    listMessages.get(i).save();
                                } else {
                                    listMessages.get(i).setIsCopy(Constant.COPYCODE_4);
                                    listMessages.get(i).save();
                                }
                            }
                        }
                        //图片复制动作全部结束，不管成功与否
                        if (backupsResult != null) {
                            backupsResult.getBackupsResult(2, true);
                        }
                    }
                } else {
                    if (backupsResult != null) {
                        backupsResult.getBackupsResult(6, false);
                    }
                }
            }
        });
        thread.start();
    }


    //得到SD卡的相关信息，包括是否存在，可用内存大小
    public String getSDMessage() {
        String[] AllRoutePath = getAllSdPaths(mContext);
        if (AllRoutePath.length > 1) {
            sdPath = AllRoutePath[1];//sd卡路径
            Item.getOneItem(mContext).setSdRootPath(sdPath);
            File file = new File(sdPath);
            //在SD卡Android/data路径下创建以软件包名命名的路径
            String absolutePath = mContext.getExternalFilesDir(null).getAbsolutePath();
            File absoluteFile = new File(absolutePath);
            boolean fileCreate = false;
            if (!absoluteFile.exists()) {
                fileCreate = absoluteFile.mkdirs();
            } else {
                fileCreate = true;
            }

            if (!fileCreate) {
                Log.e(TAG_e + "_ab", "根路径创建失败");
                if (backupsResult != null) {
                    backupsResult.getBackupsResult(7, false);
                }
                return null;
            }
            long blockSize = 0;
            long avaiSize = 0;
            //1、判断sd卡是否可用
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                //sd卡可用
                StatFs statFs = new StatFs(file.getPath());
                blockSize = statFs.getBlockSize();
                avaiSize = statFs.getAvailableBlocks();
            } else {
                Toast.makeText(mContext, "SD卡不存在", Toast.LENGTH_SHORT).show();
            }
            return Formatter.formatFileSize(mContext, blockSize * avaiSize);
        }
        return null;
    }

    public interface BackupsResult {
        void getBackupsResult(int type, boolean result);//type为1代表数据库生成json，type为2代表复制,3代表压缩，4代表复制到SD卡,5代表SD卡内存不足,6代表不需要进行备份,7代表SD卡文件夹创建失败

    }

    static BackupsResult backupsResult;

    public static void setBackupsResultLisnester(BackupsResult backupsResultLisnester) {
        backupsResult = backupsResultLisnester;
    }
}
