package com.github.oceanbbbbbb.utils;

import com.github.oceanbbbbbb.bean.CoinMarketCap;
import com.github.oceanbbbbbb.bean.Currency;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ReptileCMC {

  static String url_cmc = "https://coinmarketcap.com/coins/";
  private static DataCache dataCache = DataCache.getDataCache();

  public static BigDecimal getPriceByCMC(String coin, Currency currency) {
    BigDecimal result = null;
    if (!dataCache.isEffeCMC()){// set date
      Document doc = getArticleListFromUrl(url_cmc);
      Elements currency_rates = doc.select("#currency-exchange-rates");// 这是所有主流火币（含法币）的汇率vs美元
      Elements prices = doc.select(".price");
      Elements symbols = doc.select(".hidden-xs");
      CoinMarketCap marketCap = CoinMarketCap.builder().ctime(System.currentTimeMillis()).currencyRates(currency_rates).prices(prices).symbols(symbols).build();
      dataCache.setCoinMarketCap(marketCap);
    }
    result = dealData(dataCache.getCoinMarketCap(),coin,currency);
    CommonUtil.cannotNull(result);
    return result;
  }

  private static BigDecimal dealData(CoinMarketCap coinMarketCap, String coin, Currency currency) {
    Elements currencyRates = coinMarketCap.getCurrencyRates();
    String dataKeyCur="data-"+currency.name().toLowerCase();
    Elements prices = coinMarketCap.getPrices();
    Elements symbols = coinMarketCap.getSymbols();
    BigDecimal curryRates = new BigDecimal(currencyRates.attr(dataKeyCur)).setScale(8, RoundingMode.HALF_UP);// 这样就得到了美元价
    for (int i = 0; i < prices.size(); i++) {
      String symbol = symbols.get(i + 2).ownText();// 币名
      if(symbol.equalsIgnoreCase(coin)){
        Element element = prices.get(i);
        BigDecimal priceUsd = new BigDecimal(element.attr("data-usd"));
        return priceUsd.divide(curryRates, 8, RoundingMode.HALF_UP);
      }
    }
    return null;
  }

  private static Document getArticleListFromUrl(final String url) {
    boolean isStop = false;
    Document doc = null;
    try {
      return Jsoup.connect(url).timeout(10000).get();
    } catch (Exception e) {
      throw new RuntimeException("connect fail", e);
    }
  }

//  public static void main(String[] args) {
//    getArticleListFromUrl(url_cmc);
//  }
}
