package com.util;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.ScaleXSpan;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2017/9/11.
 */

public class AlignedTextUtils  {

    private static int n = 0;//原Str 拥有的字符个数
    private static SpannableString mSpannableString;
    /**
     * 缩放倍数：multiple=(6-n)/(n-1)
     */
    private static double multiple = 0;//方法倍数

    /**
     * 对显示的字符串进行格式化,比如输入：出生年月 输出结果：出正生正年正月
     */
    private static String formatStr(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        n = str.length();
        if (n >= 6) {
            return str;
        }
        StringBuilder sb = new StringBuilder(str);
        for (int i=n-1;i>0;i--) {
            sb.insert(i, "正");
        }
        return sb.toString();
    }

    /**
     * 对显示字符串进行格式化； 比如输入：安正卓正机正器正人  输出结果：安 卓 机 器 人
     */

    public static SpannableString formatText(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        str = formatStr(str);
        if (str.length() <= 6) {
            return null;
        }
        mSpannableString = new SpannableString(str);
        switch (n) {
            case 2:
                multiple = 4;
                break;
            case 3:
                multiple = 1.5;
                break;
            case 4:
                multiple = 0.66666666;
                break;
            case 5:
                multiple = 0.25;
                break;
            default:
                break;
        }
        for (int i=1;i<str.length();i=i+2) {
            mSpannableString.setSpan(new RelativeSizeSpan((float) multiple), i, i + 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            mSpannableString.setSpan(new ForegroundColorSpan(Color.TRANSPARENT), i, i + 1,  Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        return mSpannableString;

    }


    /**
     * 这个方法比上一个方法好
     * 将给定的字符串给定的长度两端对齐
     *
     *
     * @param str 待对齐字符串
     * @param size 汉字个数，eg:size=5，则将str在5个汉字的长度里两端对齐
     * @Return
     */
    public static SpannableStringBuilder justifyString(String str, int size) {

        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        if (TextUtils.isEmpty(str)) {
            return spannableStringBuilder;
        }
        if (str.equals("Age") || str.equals("HCG")||str.contains("No")) {
            return  spannableStringBuilder.append(str);
        }
        char[] chars = str.toCharArray();
        if (chars.length >= size || chars.length == 1) {
            return spannableStringBuilder.append(str);
        }
        int l = chars.length;


        float scale = (float) (size - l) / (l - 1);
        for (int i = 0; i < l; i++) {
            spannableStringBuilder.append(chars[i]);
            if (i != l - 1) {
                SpannableString s = new SpannableString("　");//全角空格
                s.setSpan(new ScaleXSpan(scale), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableStringBuilder.append(s);
            }
        }
        return spannableStringBuilder;
    }

    /**
     * 文字颜色
     */
    public static SpannableString  addForeColorSpan(String str) {
        SpannableString spanString = new SpannableString(str);
        ForegroundColorSpan span = new ForegroundColorSpan(Color.BLUE);
        spanString.setSpan(span, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        tv3.setText(spanString);
        return spanString;
    }

    /**
     * 字体大小
     */
    public static SpannableString addFontSpan(String str,int fontSize) {
        SpannableString spanString = new SpannableString("36号字体");
        AbsoluteSizeSpan span = new AbsoluteSizeSpan(36);
        spanString.setSpan(span, 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanString;
    }


    public static SpannableStringBuilder addConbine1(String str,List<Integer>startList,List<Integer>endList) {
        List<CharacterStyle> listCS = new ArrayList<>();
        SpannableStringBuilder spannable = new SpannableStringBuilder(str);
//        CharacterStyle span1 = new BackgroundColorSpan(Color.BLUE);
        //        spannable.setSpan(span1, 0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        CharacterStyle span1 = new ForegroundColorSpan(Color.BLUE);

        CharacterStyle span1 = new ForegroundColorSpan(Color.BLUE);
        for (int i=0;i<startList.size();i++) {
            listCS.add(new ForegroundColorSpan(Color.BLUE));
        }

        for (int i=0;i<startList.size();i++) {
            spannable.setSpan(listCS.get(i), startList.get(i), endList.get(i), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
//        spannable.setSpan(span1, 10, 15, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        spannable.setSpan(listCS.get(), a, b, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }

    public static SpannableStringBuilder addConbine2(String str,List<Integer>startList,List<Integer>endList) {
        List<CharacterStyle> listCS = new ArrayList<>();
        SpannableStringBuilder spannable = new SpannableStringBuilder(str);
//        CharacterStyle span1 = new BackgroundColorSpan(Color.BLUE);
        //        spannable.setSpan(span1, 0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        CharacterStyle span1 = new ForegroundColorSpan(Color.BLUE);

//        CharacterStyle span1 = new ForegroundColorSpan(Color.RED);
        for (int i=0;i<startList.size();i++) {
            listCS.add(new ForegroundColorSpan(Color.BLUE));
        }

        for (int i=0;i<startList.size();i++) {
            spannable.setSpan(listCS.get(i), startList.get(i), endList.get(i), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
//        spannable.setSpan(span1, 10, 15, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        spannable.setSpan(listCS.get(), a, b, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }


    /**
     * 混合
     * @param str
     * @param
     * @param startList
     * @param endList
     * @return
     */
    public static SpannableStringBuilder addConbine3(String str,List<Integer>startList,List<Integer>endList) {
        List<CharacterStyle> listCS = new ArrayList<>();
        SpannableStringBuilder spannable = new SpannableStringBuilder(str);

        for (int i=0;i<startList.size();i++) {
            listCS.add(new ForegroundColorSpan(Color.BLUE));
        }

        for (int i=0;i<startList.size();i++) {
            spannable.setSpan(listCS.get(i), startList.get(i), endList.get(i), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
//        spannable.setSpan(span1, 10, 15, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        spannable.setSpan(listCS.get(), a, b, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }





}
