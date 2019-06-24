package com.screening.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.crud.LitePalSupport;

/**
 * Created by zhangbin on 2018/4/23.
 * 筛查信息数据表，用来保存录入的筛查信息
 */

public class ListMessage extends LitePalSupport implements Parcelable {
    private String pId;//序号

    private String name;//姓名
    private String age;//年龄
    private String idCard;//身份证号
    private String phone;//电话
    private String fixedtelephone;//固定电话
    private String hpv;//条形码
    private String cytology;//细胞学条形码
    private String gene;//基因检测
    private String dna;
    private String other;
    private String sex;//性别
    private String dataofbirth;//出生日期
    private String remarks;//备注
    private String detailedaddress;//详细地址
    private String parity;//产次
    private String occupation;//职业
    private String smoking;//吸烟史
    private String abortion;//流产次数
    private String sexualpartners;//性伙伴数
    private String gestationaltimes;//孕次
    private String contraception;//避孕
    private String marry;//婚否
    private String identification;//判断是否筛查，0标识为筛查，1标识以筛查
    //添加保存通过视珍宝拍摄内容的地址
    private String picYaunTuPath; //原图地址
    private String picCuSuanPath; //醋酸白地址
    private String picDianYouPath;//碘油地址
    private String vedioPath; //视频地址
    private String scanTime;//扫码患者信息的时间
    private String screeningId;//患者每次筛查的id,也是存放图像的目录
    private String uploadScreenid;//是否已创建筛查
    private String taskNumber;//任务编号
    private String errorMsg;//获取 创建筛查失败时的原因
    private int jobId; //创建筛查时，返回的jobId
    private int row; //进行筛查时，第 row ge 创建筛查的
    private String isCopy;//图片是否已复制
    private long dayTime; //登记时间

    private int screenState; //获取jobid时的状态
    private int upLoadIndex;//获取jobid 时的位置
    private int uploadState;//判断是否已经上传到后台,自己的

    public int getUploadState() {
        return uploadState;
    }

    public void setUploadState(int uploadState) {
        this.uploadState = uploadState;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getFixedtelephone() {
        return fixedtelephone;
    }

    public void setFixedtelephone(String fixedtelephone) {
        this.fixedtelephone = fixedtelephone;
    }

    public String getHpv() {
        return hpv;
    }

    public void setHpv(String hpv) {
        this.hpv = hpv;
    }

    public String getDna() {
        return dna;
    }

    public void setDna(String dna) {
        this.dna = dna;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getDetailedaddress() {
        return detailedaddress;
    }

    public void setDetailedaddress(String detailedaddress) {
        this.detailedaddress = detailedaddress;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getGestationaltimes() {
        return gestationaltimes;
    }

    public void setGestationaltimes(String gestationaltimes) {
        this.gestationaltimes = gestationaltimes;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public String getScanTime() {
        return scanTime;
    }

    public void setScanTime(String scanTime) {
        this.scanTime = scanTime;
    }

    public String getTaskNumber() {
        return taskNumber;
    }

    public void setTaskNumber(String taskNumber) {
        this.taskNumber = taskNumber;
    }

    public int getScreenState() {
        return screenState;
    }

    public void setScreenState(int screenState) {
        this.screenState = screenState;
    }

    public int getUpLoadIndex() {
        return upLoadIndex;
    }

    public void setUpLoadIndex(int upLoadIndex) {
        this.upLoadIndex = upLoadIndex;
    }

    public long getDayTime() {
        return dayTime;
    }

    public void setDayTime(long dayTime) {
        this.dayTime = dayTime;
    }

    public String getIsCopy() {
        return isCopy;
    }

    public void setIsCopy(String isCopy) {
        this.isCopy = isCopy;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }


    public String getUploadScreenid() {
        return uploadScreenid;
    }

    public void setUploadScreenid(String uploadScreenid) {
        this.uploadScreenid = uploadScreenid;
    }

    public ListMessage() {

    }


    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public String getScreeningId() {
        return screeningId;
    }

    public void setScreeningId(String screeningId) {
        this.screeningId = screeningId;
    }


    protected ListMessage(Parcel in) {
        pId = in.readString();
        name = in.readString();
        age = in.readString();
        idCard = in.readString();
        phone = in.readString();
        fixedtelephone = in.readString();
        hpv = in.readString();
        cytology = in.readString();
        gene = in.readString();
        dna = in.readString();
        other = in.readString();
        sex = in.readString();
        dataofbirth = in.readString();
        remarks = in.readString();
        detailedaddress = in.readString();
        parity = in.readString();
        occupation = in.readString();
        smoking = in.readString();
        abortion = in.readString();
        sexualpartners = in.readString();
        gestationaltimes = in.readString();
        contraception = in.readString();
        marry = in.readString();
        identification = in.readString();
        picYaunTuPath = in.readString();
        picCuSuanPath = in.readString();
        picDianYouPath = in.readString();
        vedioPath = in.readString();
        scanTime = in.readString();
        screeningId = in.readString();
        uploadScreenid = in.readString();
        taskNumber = in.readString();
        errorMsg = in.readString();
        jobId = in.readInt();
        row = in.readInt();
        isCopy = in.readString();
        dayTime = in.readInt();
        screenState = in.readInt();
        upLoadIndex = in.readInt();


    }

    public static final Creator<ListMessage> CREATOR = new Creator<ListMessage>() {
        @Override
        public ListMessage createFromParcel(Parcel in) {
            return new ListMessage(in);
        }

        @Override
        public ListMessage[] newArray(int size) {
            return new ListMessage[size];
        }
    };

    public String getGene() {
        return gene;
    }

    public void setGene(String gene) {
        this.gene = gene;
    }

    public String getPicYaunTuPath() {
        return picYaunTuPath;
    }

    public void setPicYaunTuPath(String picYaunTuPath) {
        this.picYaunTuPath = picYaunTuPath;
    }

    public String getPicCuSuanPath() {
        return picCuSuanPath;
    }

    public void setPicCuSuanPath(String picCuSuanPath) {
        this.picCuSuanPath = picCuSuanPath;
    }

    public String getPicDianYouPath() {
        return picDianYouPath;
    }

    public void setPicDianYouPath(String picDianYouPath) {
        this.picDianYouPath = picDianYouPath;
    }

    public String getVedioPath() {
        return vedioPath;
    }

    public void setVedioPath(String vedioPath) {
        this.vedioPath = vedioPath;
    }


    public String getParity() {
        return parity;
    }

    public void setParity(String parity) {
        this.parity = parity;
    }


    public String getSmoking() {
        return smoking;
    }

    public void setSmoking(String smoking) {
        this.smoking = smoking;
    }

    public String getAbortion() {
        return abortion;
    }

    public void setAbortion(String abortion) {
        this.abortion = abortion;
    }

    public String getSexualpartners() {
        return sexualpartners;
    }

    public void setSexualpartners(String sexualpartners) {
        this.sexualpartners = sexualpartners;
    }


    public String getContraception() {
        return contraception;
    }

    public void setContraception(String contraception) {
        this.contraception = contraception;
    }

    public String getMarry() {
        return marry;
    }

    public void setMarry(String marry) {
        this.marry = marry;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public String getCytology() {
        return cytology;
    }

    public void setCytology(String cytology) {
        this.cytology = cytology;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getDataofbirth() {
        return dataofbirth;
    }

    public void setDataofbirth(String dataofbirth) {
        this.dataofbirth = dataofbirth;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(pId);
        dest.writeString(name);
        dest.writeString(age);
        dest.writeString(idCard);
        dest.writeString(phone);
        dest.writeString(fixedtelephone);
        dest.writeString(hpv);
        dest.writeString(cytology);
        dest.writeString(gene);
        dest.writeString(dna);
        dest.writeString(other);
        dest.writeString(sex);
        dest.writeString(dataofbirth);
        dest.writeString(remarks);
        dest.writeString(detailedaddress);
        dest.writeString(parity);
        dest.writeString(occupation);
        dest.writeString(smoking);
        dest.writeString(abortion);
        dest.writeString(sexualpartners);
        dest.writeString(gestationaltimes);
        dest.writeString(contraception);
        dest.writeString(marry);
        dest.writeString(identification);
        dest.writeString(picYaunTuPath);
        dest.writeString(picCuSuanPath);
        dest.writeString(picDianYouPath);
        dest.writeString(vedioPath);
        dest.writeString(scanTime);
        dest.writeString(screeningId);
        dest.writeString(uploadScreenid);
        dest.writeString(taskNumber);
        dest.writeString(errorMsg);
        dest.writeInt(jobId);
        dest.writeInt(row);
        dest.writeString(isCopy);
        dest.writeLong(dayTime);
        dest.writeInt(screenState);
        dest.writeInt(upLoadIndex);

    }
}
