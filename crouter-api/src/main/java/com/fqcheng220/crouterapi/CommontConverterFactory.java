package com.fqcheng220.crouterapi;

import java.lang.reflect.Type;

/**
 * @author fqcheng220
 * @version V1.0
 * @Description: ${todo}(用一句话描述该文件做什么)
 * @date 2019/10/22 17:27
 */
public class CommontConverterFactory implements Converter.Factory {
  @Override public Converter requestConverter(Type type) {
    Converter converter = null;
    if(type == Integer.class || type == int.class){
      converter = sPrimitiveConverterInteger;
    }else if(type == Long.class || type == long.class){
      converter = sPrimitiveConverterLong;
    }else if(type == Short.class || type == short.class){
      converter = sPrimitiveConverterShort;
    }else if(type == Float.class || type == float.class){
      converter = sPrimitiveConverterFloat;
    }else if(type == Double.class || type == double.class){
      converter = sPrimitiveConverterDouble;
    }else if(type == Character.class || type == char.class){
      converter = sPrimitiveConverterCharacter;
    }else if(type == Byte.class || type == byte.class){
      converter = sPrimitiveConverterByte;
    }else if(type == Boolean.class || type == boolean.class){
      converter = sPrimitiveConverterBoolean;
    }
    return converter;
  }

  private static PrimitiveConverterLong sPrimitiveConverterLong = new PrimitiveConverterLong();
  private static PrimitiveConverterInteger sPrimitiveConverterInteger = new PrimitiveConverterInteger();
  private static PrimitiveConverterShort sPrimitiveConverterShort = new PrimitiveConverterShort();
  private static PrimitiveConverterFloat sPrimitiveConverterFloat = new PrimitiveConverterFloat();
  private static PrimitiveConverterDouble sPrimitiveConverterDouble = new PrimitiveConverterDouble();
  private static PrimitiveConverterCharacter sPrimitiveConverterCharacter = new PrimitiveConverterCharacter();
  private static PrimitiveConverterByte sPrimitiveConverterByte = new PrimitiveConverterByte();
  private static PrimitiveConverterBoolean sPrimitiveConverterBoolean = new PrimitiveConverterBoolean();

  public static class PrimitiveConverterLong implements Converter<String,Long> {
    @Override public Long convert(String o) {
      return Long.valueOf(o);
    }
  }

  public static class PrimitiveConverterInteger implements Converter<String,Integer> {
    @Override public Integer convert(String o) {
      return Integer.valueOf(o);
    }
  }

  public static class PrimitiveConverterShort implements Converter<String,Short> {
    @Override public Short convert(String o) {
      return Short.valueOf(o);
    }
  }

  public static class PrimitiveConverterFloat implements Converter<String,Float> {
    @Override public Float convert(String o) {
      return Float.valueOf(o);
    }
  }

  public static class PrimitiveConverterDouble implements Converter<String,Double> {
    @Override public Double convert(String o) {
      return Double.valueOf(o);
    }
  }

  public static class PrimitiveConverterCharacter implements Converter<String,Character> {
    @Override public Character convert(String o) {
      return o.charAt(0);
    }
  }

  public static class PrimitiveConverterByte implements Converter<String,Byte> {
    @Override public Byte convert(String o) {
      return Byte.valueOf(o);
    }
  }

  public static class PrimitiveConverterBoolean implements Converter<String,Boolean> {
    @Override public Boolean convert(String o) {
      return Boolean.valueOf(o);
    }
  }
}
