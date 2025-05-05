package com.example.text.dataModel;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.text.TextUtils;
import android.util.Log;

import com.github.promeg.pinyinhelper.Pinyin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class FriendListItem {
    private String contactId;
    private String contactName;
    private String avatarUrl;
    private String firstLetter; // 首字母

    public FriendListItem(String name){
        this.contactName =name;
        if (TextUtils.isEmpty(name)) {
            firstLetter="#";
            return;
        }
        char firstChar = name.charAt(0);
//        if (Character.isLetter(firstChar)) {
        if (('A'<=firstChar&&firstChar<='Z')||('a'<=firstChar&&firstChar<='z')) {
            Log.i(TAG, "isLetter: ");
            firstLetter=String.valueOf(Character.toUpperCase(firstChar));
        } else {
            String pinyin = Pinyin.toPinyin(firstChar);
            Log.i(TAG, "notLetter: "+pinyin);
            firstLetter=pinyin.isEmpty()||pinyin.charAt(0)<'A'||'Z'<pinyin.charAt(0) ? "#" : pinyin.substring(0, 1);
        }
        Log.i(TAG, "name: "+name);
        Log.i(TAG, "firstLetter: "+firstLetter);
    }

    public FriendListItem(String name,String avatar){
        this.contactName =name;
        if (TextUtils.isEmpty(name)) {
            firstLetter="#";
            return;
        }
        char firstChar = name.charAt(0);
//        if (Character.isLetter(firstChar)) {
        if (('A'<=firstChar&&firstChar<='Z')||('a'<=firstChar&&firstChar<='z')) {
            Log.i(TAG, "isLetter: ");
            firstLetter=String.valueOf(Character.toUpperCase(firstChar));
        } else {
            String pinyin = Pinyin.toPinyin(firstChar);
            Log.i(TAG, "notLetter: "+pinyin);
            firstLetter=pinyin.isEmpty()||pinyin.charAt(0)<'A'||'Z'<pinyin.charAt(0) ? "#" : pinyin.substring(0, 1);
        }
        Log.i(TAG, "name: "+name);
        Log.i(TAG, "firstLetter: "+firstLetter);
        this.avatarUrl=avatar;
    }

    public FriendListItem(String contactId, String contactName, String avatarUrl) {
        this.contactId = contactId;
        this.contactName = contactName;
        this.avatarUrl = avatarUrl;
        if (TextUtils.isEmpty(contactName)) {
            firstLetter="#";
            return;
        }
        char firstChar = contactName.charAt(0);
        if (('A'<=firstChar&&firstChar<='Z')||('a'<=firstChar&&firstChar<='z')) {
            firstLetter=String.valueOf(Character.toUpperCase(firstChar));
        } else {
            String pinyin = Pinyin.toPinyin(firstChar);
            firstLetter=pinyin.isEmpty()||pinyin.charAt(0)<'A'||'Z'<pinyin.charAt(0) ? "#" : pinyin.substring(0, 1);
        }
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getFirstLetter() {
        return firstLetter;
    }

    public void setFirstLetter(String firstLetter) {
        this.firstLetter = firstLetter;
    }

    public static void sortByFiestLetter(List<FriendListItem> list){
        Collections.sort(list, (f1, f2) ->{
            if(Objects.equals(f1.getFirstLetter(), "#") && Objects.equals(f2.getFirstLetter(), "#")){
                return f1.getContactName().compareTo(f2.getContactName());
            }else if(Objects.equals(f1.getFirstLetter(), "#") && !Objects.equals(f2.getFirstLetter(), "#")){
                return 1;
            }else if(!Objects.equals(f1.getFirstLetter(), "#") && Objects.equals(f2.getFirstLetter(), "#")){
                return -1;
            }else{
                return f1.getFirstLetter().compareTo(f2.getFirstLetter());
            }
        });
    }

    // 生成测试数据
    private List<FriendListItem> generateData() {
        List<FriendListItem> list = new ArrayList<>();
        list.add(new FriendListItem("Alice"));
        list.add(new FriendListItem("Bob"));
        list.add(new FriendListItem("Charlie"));
        list.add(new FriendListItem("Alice"));
        list.add(new FriendListItem("Bob"));
        list.add(new FriendListItem("Charlie"));
        list.add(new FriendListItem("Alice"));
        list.add(new FriendListItem("Bob"));
        list.add(new FriendListItem("Charlie"));
        list.add(new FriendListItem("Alice"));
        list.add(new FriendListItem("Bob"));
        list.add(new FriendListItem("Charlie"));
        list.add(new FriendListItem("Alice"));
        list.add(new FriendListItem("Bob"));
        list.add(new FriendListItem("Charlie"));
        list.add(new FriendListItem("Alice"));
        list.add(new FriendListItem("Bob"));
        list.add(new FriendListItem("Charlie"));
        list.add(new FriendListItem("Alice"));
        list.add(new FriendListItem("Bob"));
        list.add(new FriendListItem("Charlie"));
        list.add(new FriendListItem("王嘉诚"));
        list.add(new FriendListItem("于洋"));
        list.add(new FriendListItem("谭杰"));


        // 更多数据...

        // 按首字母排序
//        Collections.sort(list, (f1, f2) ->
//                f1.getFirstLetter().compareTo(f2.getFirstLetter()));
        sortByFiestLetter(list);
        return list;
    }
}

//public class FriendListItem {
//    private String userId;
//    private String nickName;
//    private String avatarUrl;
//    private int sex;
//    private String email;
//    private String personalSignature;
//    private String firstLetter; // 首字母
//
//    public FriendListItem(String name){
//        this.nickName=name;
//        if (TextUtils.isEmpty(name)) {
//            firstLetter="#";
//            return;
//        }
//        char firstChar = name.charAt(0);
////        if (Character.isLetter(firstChar)) {
//        if (('A'<=firstChar&&firstChar<='Z')||('a'<=firstChar&&firstChar<='z')) {
//            Log.i(TAG, "isLetter: ");
//            firstLetter=String.valueOf(Character.toUpperCase(firstChar));
//        } else {
//            String pinyin = Pinyin.toPinyin(firstChar);
//            Log.i(TAG, "notLetter: "+pinyin);
//            firstLetter=pinyin.isEmpty()||pinyin.charAt(0)<'A'||'Z'<pinyin.charAt(0) ? "#" : pinyin.substring(0, 1);
//        }
//        Log.i(TAG, "name: "+name);
//        Log.i(TAG, "firstLetter: "+firstLetter);
//    }
//
//    public FriendListItem(String name,String avatar){
//        this.nickName=name;
//        if (TextUtils.isEmpty(name)) {
//            firstLetter="#";
//            return;
//        }
//        char firstChar = name.charAt(0);
////        if (Character.isLetter(firstChar)) {
//        if (('A'<=firstChar&&firstChar<='Z')||('a'<=firstChar&&firstChar<='z')) {
//            Log.i(TAG, "isLetter: ");
//            firstLetter=String.valueOf(Character.toUpperCase(firstChar));
//        } else {
//            String pinyin = Pinyin.toPinyin(firstChar);
//            Log.i(TAG, "notLetter: "+pinyin);
//            firstLetter=pinyin.isEmpty()||pinyin.charAt(0)<'A'||'Z'<pinyin.charAt(0) ? "#" : pinyin.substring(0, 1);
//        }
//        Log.i(TAG, "name: "+name);
//        Log.i(TAG, "firstLetter: "+firstLetter);
//        this.avatarUrl=avatar;
//    }
//
//    public FriendListItem(String userId, String nickName, String avatarUrl, int sex, String email, String personalSignature) {
//        this.userId = userId;
//        this.nickName = nickName;
//        this.avatarUrl = avatarUrl;
//        this.sex = sex;
//        this.email = email;
//        this.personalSignature = personalSignature;
//        if (TextUtils.isEmpty(nickName)) {
//            firstLetter="#";
//            return;
//        }
//        char firstChar = nickName.charAt(0);
//        if (('A'<=firstChar&&firstChar<='Z')||('a'<=firstChar&&firstChar<='z')) {
//            firstLetter=String.valueOf(Character.toUpperCase(firstChar));
//        } else {
//            String pinyin = Pinyin.toPinyin(firstChar);
//            firstLetter=pinyin.isEmpty()||pinyin.charAt(0)<'A'||'Z'<pinyin.charAt(0) ? "#" : pinyin.substring(0, 1);
//        }
//    }
//
//    public String getUserId() {
//        return userId;
//    }
//
//    public void setUserId(String userId) {
//        this.userId = userId;
//    }
//
//    public String getNickName() {
//        return nickName;
//    }
//
//    public void setNickName(String nickName) {
//        this.nickName = nickName;
//    }
//
//    public String getAvatarUrl() {
//        return avatarUrl;
//    }
//
//    public void setAvatarUrl(String avatarUrl) {
//        this.avatarUrl = avatarUrl;
//    }
//
//    public int getSex() {
//        return sex;
//    }
//
//    public void setSex(int sex) {
//        this.sex = sex;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getPersonalSignature() {
//        return personalSignature;
//    }
//
//    public void setPersonalSignature(String personalSignature) {
//        this.personalSignature = personalSignature;
//    }
//
//    public String getFirstLetter() {
//        return firstLetter;
//    }
//
//    public void setFirstLetter(String firstLetter) {
//        this.firstLetter = firstLetter;
//    }
//
//    public static void sortByFiestLetter(List<FriendListItem> list){
//        Collections.sort(list, (f1, f2) ->{
//            if(Objects.equals(f1.getFirstLetter(), "#") && Objects.equals(f2.getFirstLetter(), "#")){
//                return f1.getNickName().compareTo(f2.getNickName());
//            }else if(Objects.equals(f1.getFirstLetter(), "#") && !Objects.equals(f2.getFirstLetter(), "#")){
//                return 1;
//            }else if(!Objects.equals(f1.getFirstLetter(), "#") && Objects.equals(f2.getFirstLetter(), "#")){
//                return -1;
//            }else{
//                return f1.getFirstLetter().compareTo(f2.getFirstLetter());
//            }
//        });
//    }
//
//    // 生成测试数据
//    private List<FriendListItem> generateData() {
//        List<FriendListItem> list = new ArrayList<>();
//        list.add(new FriendListItem("Alice"));
//        list.add(new FriendListItem("Bob"));
//        list.add(new FriendListItem("Charlie"));
//        list.add(new FriendListItem("Alice"));
//        list.add(new FriendListItem("Bob"));
//        list.add(new FriendListItem("Charlie"));
//        list.add(new FriendListItem("Alice"));
//        list.add(new FriendListItem("Bob"));
//        list.add(new FriendListItem("Charlie"));
//        list.add(new FriendListItem("Alice"));
//        list.add(new FriendListItem("Bob"));
//        list.add(new FriendListItem("Charlie"));
//        list.add(new FriendListItem("Alice"));
//        list.add(new FriendListItem("Bob"));
//        list.add(new FriendListItem("Charlie"));
//        list.add(new FriendListItem("Alice"));
//        list.add(new FriendListItem("Bob"));
//        list.add(new FriendListItem("Charlie"));
//        list.add(new FriendListItem("Alice"));
//        list.add(new FriendListItem("Bob"));
//        list.add(new FriendListItem("Charlie"));
//        list.add(new FriendListItem("王嘉诚"));
//        list.add(new FriendListItem("于洋"));
//        list.add(new FriendListItem("谭杰"));
//
//
//        // 更多数据...
//
//        // 按首字母排序
////        Collections.sort(list, (f1, f2) ->
////                f1.getFirstLetter().compareTo(f2.getFirstLetter()));
//        sortByFiestLetter(list);
//        return list;
//    }
//}
