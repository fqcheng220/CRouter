package com.fqcheng220.crouterapi;

import java.lang.reflect.Type;

/**
 * @author fqcheng220
 * @version V1.0
 * @Description: ${todo}(用一句话描述该文件做什么)
 * @date 2019/10/22 17:24
 */
public interface Converter<F,T> {
  T convert(F f);

  public interface Factory<F,T>{
    Converter<F,T> requestConverter(Type type);
  }
}
