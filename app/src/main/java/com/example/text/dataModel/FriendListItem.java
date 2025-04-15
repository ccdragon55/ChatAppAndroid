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
    private String name;
    private String firstLetter; // 首字母

    public FriendListItem(String name){
        this.name=name;
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

    public String getName() {
        return name;
    }

    public String getFirstLetter() {
        return firstLetter;
    }

    public static void sortByFiestLetter(List<FriendListItem> list){
        Collections.sort(list, (f1, f2) ->{
            if(Objects.equals(f1.getFirstLetter(), "#") && Objects.equals(f2.getFirstLetter(), "#")){
                return f1.getName().compareTo(f2.getName());
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
