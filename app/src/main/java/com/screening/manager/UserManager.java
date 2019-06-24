package com.screening.manager;

import com.screening.model.DoctorId;
import com.screening.model.ListMessage;
import com.screening.model.User;

import org.litepal.LitePal;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * Created by dell on 2018/4/24.
 * 主要是管理用户
 */

public class UserManager {


    public UserManager() {

    }

    /**
     * 保存数据 保存admin、普通用户的 账户和密码, s
     *
     * @param name
     * @param password
     * @param state    表示用户的类型，state = admin  state =normal 表示登陆的用户  state=staff 表示筛查人员的信息
     */
    public void saveUserData(String name, String password, String state) {
        User user = new User.Builder()
                .name(name)
                .pass(password)
                .state(state)
                .build();

        List<User> adminlist = loginSearch(name, password);
        if (adminlist.size() > 0) {
            return;
        } else {
            user.save();
        }
    }


    /**
     * 通过用户名和密码，查询数据库
     */
    public List<User> loginSearch(String name, String passWord) {
        List<User> list = LitePal.where("name = ?  and  passWord = ?", name, passWord).find(User.class);
        return list;
    }

    /**
     * 通过state 的属性来查询
     */
    public List<User> staffSearch(String state) {
        List<User> list = LitePal.where("state = ?  ", state).find(User.class);
        return list;
    }

    /*
    通过idcard 来查找用户
     */
    public ListMessage toIDSearch(String id) {
        List<ListMessage> list = LitePal.where("idCard = ?  ", id).find(ListMessage.class);
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 查询所有的用户
     */
    public List<User> allUserInfo() {

        List<User> list = LitePal.findAll(User.class);
        return list;
    }

    /**
     * 通过 name 删除相关的用户
     */
    public List<User> deleteUserInfo(String name) {
        int i = LitePal.deleteAll(User.class, "name = ? ", name);
        return allUserInfo();
    }


    /**
     * 初始化doctorid
     */
    public void InitializationDoctorId() {
//        List<DoctorId>doctorIdList = LitePal.findAll(DoctorId.class);
//        if(doctorIdList.size() == 0){
//            DoctorId doctorId = new DoctorId();
//            doctorId.setDoctorId("123");
//            doctorId.save();
//        }

    }

    /**
     * 修改doctor
     */
    public void modifyDoctorId(String doctorId) {
        List<DoctorId> doctorIdList = LitePal.findAll(DoctorId.class);
        if (doctorIdList.size() > 0) {
            doctorIdList.get(0).setDoctorId(doctorId);
            boolean save = doctorIdList.get(0).save();
        } else {
            DoctorId doc = new DoctorId();
            doc.setDoctorId(doctorId);
            doc.save();
        }
    }

    /**
     * 查询doctorid
     */
    public String searchDoctorId() {
        List<DoctorId> doctorIdList = LitePal.findAll(DoctorId.class);
        if (doctorIdList.size() > 0) {
            return doctorIdList.get(0).getDoctorId();
        } else {
//            return "123";
            return "";
        }
    }

    //查询所有未完成的受检者
    public List<ListMessage> searchUserUnfinished() {
        return LitePal.where("screenState < 5 ").find(ListMessage.class);
    }

    //查询所有的已完成的受检者信息
    public List<ListMessage> searchUserFinished() {
        return LitePal.where("screenState >=5 ").find(ListMessage.class);
    }


    /**
     * 删除单个用户
     */
    public int deleteSingleUser(String idCard) {
        return LitePal.deleteAll(ListMessage.class, "idCard = ? ", idCard);
    }

    /**
     * 删除所有未完成的受检者
     */
    public int deleteUserUnfinished() {
        return LitePal.deleteAll(ListMessage.class, "screenState < 5 ");
    }

    /**
     * 删除所有的已完成的受检者信息
     */
    public int deleteUserFinished() {
        return LitePal.deleteAll(ListMessage.class, "screenState >=5");
    }

}
