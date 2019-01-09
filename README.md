# exchange-rate

说明：
由于coinmarketcap关闭了他们的免费api,而统计比较公认的权威和完善的汇率数据主要来自于coinmarketcap和非小号，本程序为通过http和爬虫获取汇率数据。

获取加密货币的实时汇率,获取货币的美元、人民币和比特币等102种计价币的价格

当前版本支持非小号上交易排名前100的币种，需要拓展更多币种的价格可以修改源码中的url做翻页等方式。

使用方法：
下载后打包引入。
公共仓库maven:


代码示例：

System.out.println(getCoinPrice("BTC",Currency.EUR));// 获取综合指数价(汇率)

System.out.println(ReptileCMC.getPriceByCMC("BTC",Currency.CNY));// 获取coinmarketcap的价(汇率)

System.out.println(ConvertFXH.getPriceByFXH("BTC",CurrencyFXH.USD));// 获取feixiaohao的价(汇率)


问题：

1.只支持查询主流的100个币，但可以自己拓展；

2.偶尔会有javax.net.ssl.SSLHandshakeException,于是在创建client的时候，忽略了ssl认证，SslUtil.ignoreSsl

