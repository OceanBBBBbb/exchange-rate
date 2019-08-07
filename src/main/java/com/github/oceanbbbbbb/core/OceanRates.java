package com.github.oceanbbbbbb.core;

import com.github.oceanbbbbbb.bean.CurrencyFXH;
import com.github.oceanbbbbbb.utils.CommonUtil;
import com.github.oceanbbbbbb.utils.ReptileCMC;
import com.github.oceanbbbbbb.bean.Currency;
import com.github.oceanbbbbbb.utils.ConvertFXH;
import com.github.oceanbbbbbb.utils.DataCache;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.jsoup.select.Elements;

public class OceanRates {

  private static DataCache dataCache = DataCache.getDataCache();

  /**
   * 获取币价。综合指数
   *
   * @param coin 币种，如"BTC" 或"btc"
   * @param currency 计价币，如Currency.USD
   * @return 该coin币的currency价
   */
  public static BigDecimal getCoinPrice(String coin, Currency currency) {
    BigDecimal priceByCMC = null;
    try {
      priceByCMC = ReptileCMC.getPriceByCMC(coin, currency);
    } catch (Exception e) {
      e.printStackTrace();
    }
    BigDecimal priceByFXH = null;
    try {
      if (CommonUtil.isFxhBase(currency.name())) {//基础计价币可以直接拿
        priceByFXH = ConvertFXH.getPriceByFXH(coin, CurrencyFXH.valueOf(currency.name()));
      } else {
        Elements currencyRates = dataCache.getCoinMarketCap().getCurrencyRates();
        String dataKeyCur="data-"+currency.name().toLowerCase();
        BigDecimal curryRates = new BigDecimal(currencyRates.attr(dataKeyCur)).setScale(8, RoundingMode.HALF_UP);
        BigDecimal priceByUsd = ConvertFXH.getPriceByUsd(coin);
        priceByFXH = priceByUsd.divide(curryRates, 8, RoundingMode.HALF_UP);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (null == priceByCMC && null == priceByFXH) {
      CommonUtil.cannotNull(null);
    } else if (null != priceByCMC && null != priceByFXH) {
      return (priceByCMC.add(priceByFXH)).divide(new BigDecimal(2), 8, RoundingMode.HALF_UP);
    } else {
      return null == priceByCMC ? priceByFXH : priceByCMC;
    }
    return null;
  }

  //demo
  public static void main(String[] args) {
    for (int i = 0; i < 100; i++) {
//      System.out.println(getCoinPrice("BTC",Currency.EUR));// 获取综合指数价(汇率)
//      System.out.println(ReptileCMC.getPriceByCMC("BTC",Currency.CNY));// 获取coinmarketcap的价(汇率)
      System.out.println(ConvertFXH.getPriceByFXH("BTC",CurrencyFXH.USD));// 获取feixiaohao的价(汇率)
    }
  }


}
