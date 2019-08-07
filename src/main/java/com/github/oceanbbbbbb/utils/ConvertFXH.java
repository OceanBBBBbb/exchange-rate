package com.github.oceanbbbbbb.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.oceanbbbbbb.bean.CurrencyFXH;
import com.github.oceanbbbbbb.bean.Feixiaohao;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.TreeMap;

/**
 * 汇率工具类-feixiaohao /只提供非小号排名前100的币
 */
public class ConvertFXH {

  static String feixiaohao = "https://dncapi.feixiaohao.com/api/coin/web-coinrank?page=1&type=0&pagesize=100&webp=1";
  private static DataCache dataCache = DataCache.getDataCache();

  /**
   * 通过制定币种获取指定币价格
   */
  public static BigDecimal getPriceByFXH(String coin, CurrencyFXH currency) {
    if (currency == CurrencyFXH.CNY) {
      return getPriceByCny(coin);
    }
    if (currency == CurrencyFXH.USD) {
      return getPriceByUsd(coin);
    }
    if (currency == CurrencyFXH.BTC) {
      return getPriceByBtc(coin);
    }
    return BigDecimal.ZERO;
  }

  public static BigDecimal getPriceByBtc(String coin) {
    return doUrl(feixiaohao, coin, CurrencyFXH.BTC);
  }

  public static BigDecimal getPriceByUsd(String coin) {
    return doUrl(feixiaohao, coin, CurrencyFXH.USD);
  }

  public static BigDecimal getPriceByCny(String coin) {
    return doUrl(feixiaohao, coin, CurrencyFXH.CNY);
  }

  private static BigDecimal doUrl(String url, String symbol, CurrencyFXH currency) {
    Map<String, String> headers = new TreeMap<>();
    headers.put("Content-Type", "application/json");
    headers.put("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36");
    BigDecimal result = null;
    try {
      if(dataCache.isEffeFXH()){
        result = dealData(dataCache.getFeixiaohao().getJsonArray(),symbol,currency);
      }else{
        String content = OkHttpClientUtil.doGet(url, null, headers, null);
        JSONObject jsonObject = JSONObject.parseObject(content);
        JSONArray array = (JSONArray) jsonObject.get("data"); // 这个数据放内存，有5秒寿命
        dataCache.setFeixiaohao(Feixiaohao.builder().ctime(System.currentTimeMillis()).jsonArray(array).build());
        result = dealData(array,symbol,currency);
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("connect err:",e);
    }
    CommonUtil.cannotNull(result);
    return result;
  }

  private static BigDecimal dealData(JSONArray array, String symbol, CurrencyFXH currency) {
    BigDecimal btc_price = null;
    for (int i = 0; i < array.size(); i++) {
      JSONObject o = (JSONObject) array.get(i);
      String name = (String) o.get("name");
      if (name.equalsIgnoreCase(CurrencyFXH.BTC.name())) {
        btc_price = (BigDecimal) o.get("current_price_usd");    // btc default is 1st
      }
      if (name.equalsIgnoreCase(symbol)) {
        if (currency == CurrencyFXH.CNY) {
          return (BigDecimal) o.get("current_price");
        }
        if (currency == CurrencyFXH.USD) {
          return (BigDecimal) o.get("current_price_usd");
        }
        if (currency == CurrencyFXH.BTC) {
          return ((BigDecimal) o.get("current_price_usd")).divide(btc_price, 8, RoundingMode.HALF_UP);
        }
      }
    }
    return null;
  }
}
