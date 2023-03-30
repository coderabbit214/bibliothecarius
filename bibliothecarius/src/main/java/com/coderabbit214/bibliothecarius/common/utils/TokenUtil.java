package com.coderabbit214.bibliothecarius.common.utils;

public class TokenUtil {

    /**
     * 英文一个单词一个token
     * 中文一个字两个token
     * 换行，空格，符号 一个token
     *
     * @param text
     * @return
     */
    public static Integer getTokens(String text) {
        //计算汉字
        int len = text.length();
        int count = 0;
        for (int i = 0; i < len; i++) {
            char c = text.charAt(i);
            if (c >= '\u4e00' && c <= '\u9fa5') {
                count += 2;
            }
        }
        //去掉所有汉字
        text = text.replaceAll("[\\u4e00-\\u9fa5]", " ");
        //合并空格
        text = text.replaceAll("\\s+", " ");
        //计算单词 根据空格分割 根据换行
        String[] split = text.split("\n");
        for (String s : split) {
            String[] words = s.split(" ");
            count += words.length / 3 * 4;
        }
        text = text.replaceAll("\n", "");
        //去掉所有字母
        text = text.replaceAll("[a-zA-Z]", "");
        //计算特殊符号
        count += text.length();
        return count;
    }
}
