package com.goopal.rate.jobs;

import com.alibaba.fastjson.JSONArray;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Feixiaohao {

  private long ctime;
  private JSONArray jsonArray;

  public Feixiaohao(long l, JSONArray array) {
    this.ctime=l;
    this.jsonArray=array;
  }
}
