package com.goopal.rate.utils;

import java.math.BigDecimal;

public class CommonUtil {

  static final String[] basejjb={"BTC","USD","CNY"};

  // 不可为空
  public static void cannotNull(BigDecimal result){
    if (result == null) {
      throw new RuntimeException("result is null,may be do not know this coin");
    }
  }

  // 值是否在数组内
  public static boolean isInArray(String[] arr,String targetValue){
    for(String s:arr){
      if(s.equalsIgnoreCase(targetValue))
        return true;
    }
    return false;
  }

  // 是否为非小号基础计价币
  public static boolean isFxhBase(String name) {
    return isInArray(basejjb,name);
  }
}
