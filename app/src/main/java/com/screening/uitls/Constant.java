package com.screening.uitls;

import com.screening.model.ListMessage;

public class Constant {
    public static final String DOC_VERIFY = "https://wechat.flybiotech.cn/lfy/api/lfys-hospital-doctors/";
    public static String MD5_USERNAME = "lfyapi"; //
    public static String MD5_PASSWORD = "DRtJCRZuPvR6Uao"; //
    public static final String CREATE_URL = "https://wechat.flybiotech.cn/lfy/api/lfys-hospital-screenings/batch-create";
    public static final String Job_id = "https://wechat.flybiotech.cn/lfy/api/batch-jobs/";


    public static int isAddOrModify = -1;//判断添加还是修改,-1 不操作，直接返回，0 修改，1 添加

    public static String unfinishedScreenId = "FLY_jobId";

    public static String zipPath = "zipPath";//记录压缩文件信息，下次备份时，需要先进行查询，如果存在先备份此压缩文件

    public static ListMessage errorListMessage = null;//用来记录异常信息的患者集合

    public static String sdPath = "/Android/data/com.flybiotech.screening/BACKUPS/";

    public static String fileName = "图片和视频";


    //记录文件复制的状态,0未复制，1已复制，-1复制失败

    public static String COPYCODE_0 = "0"; //初始化
    public static String COPYCODE_1 = "1";//复制动作结束，还未写入json
    public static String COPYCODE_4 = "-1";//复制结束，但是有部分文件复制失败
    public static String COPYCODE_2 = "2";//复制成功，写入json成功


    public static String hpv_key = "hpv";
    public static String cytology_key = "cytology";
    public static String gene_key = "gene";
    public static String dna_key = "dna";
    public static String other_key = "other";
    public static String barcode_key = "barcode";

    public static String hpv_id = "", cytology_id = "", gene_id = "", dna_id = "", other_id = "";//各种码的标识
    public static String hpv_size = "", cytology_size = "", gene_size = "", dna_size = "", other_size = "";

    public static String localFile_key = "localFile";//本地文件大小标示
    public static String ftpFile_key = "keyFile";//ftp服务器文件大小标示

}
