package com.github.oceanbbbbbb.bean;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.jsoup.select.Elements;

@Data
@ToString
@Builder
public class CoinMarketCap {

  private long ctime;
  private Elements currencyRates;//计价币汇率
  private Elements prices;
  private Elements symbols;
}
