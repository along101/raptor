package com.ppdai.raptor.codegen.java.util;

import com.google.common.base.CaseFormat;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhangchengxi
 * Date 2018/5/16
 */
public class CaseFormatUtil {
    /**
     * 判断str是哪种命名格式的,不能保证一定判断对,慎用
     *
     * @param str
     * @return
     */
    public static CaseFormat determineFormat(String str) {
        Preconditions.checkNotNull(str);
        String[] split = str.split("_");
        List<String> splitedStrings = Arrays.stream(split).map(String::trim).filter(StringUtils::isNotBlank).collect(Collectors.toList());

        if (splitedStrings.size() == 1) {
            //camel
            if (CharUtils.isAsciiAlphaUpper(splitedStrings.get(0).charAt(0))) {
                return CaseFormat.UPPER_CAMEL;
            } else {
                return CaseFormat.LOWER_CAMEL;
            }
        } else if (splitedStrings.size() > 1) {
            //underscore
            if (CharUtils.isAsciiAlphaUpper(splitedStrings.get(0).charAt(0))) {
                return CaseFormat.UPPER_UNDERSCORE;
            } else {
                return CaseFormat.LOWER_UNDERSCORE;
            }
        }else{
            //判断不出那个
            return CaseFormat.LOWER_CAMEL;
        }
    }

    public static void main(String[] args) {
        String str = "_abc_ac";
        String[] split = str.split("_");
        int i = 0;
        for (String s : split) {
            System.out.println(i + "" + s);
            i++;
        }
    }
}
