package com.fqcheng220.crouterapi.exception;

/**
 * @author fqcheng220
 * @version V1.0
 * @Description: ${todo}(用一句话描述该文件做什么)
 * @date 2019/10/22 16:00
 */
public class ExceptionConsts {
  //IllegalArgumentException
  public static final String ARGS_MSG_SERVICE_API_PARAMS_LOST_ANNOTATION = "SERVICE API METHOD PARAMS WITH @QUERY ANNOTATION NOT PRESENT ";
  public static final String ARGS_MSG_SERVICE_API_PARAMS_CANNOT_FILL = "SERVICE API METHOD PARAMS CANNOT FILL ";
  //IllegalStateException
  public static final String STATE_MSG_NOT_CONTEXT_FRAGMENT_ACTIVITY = "STATE_MSG_NOT_CONTEXT_FRAGMENT_ACTIVITY";
  public static final String STATE_MSG_ROUTEMETA_NOT_OBSERVABLE = "STATE_MSG_ROUTEMETA_NOT_OBSERVABLE";
  public static final String STATE_MSG_NOT_ROUTEMETA_MATCHED = "STATE_MSG_NOT_ROUTEMETA_MATCHED";
  public static final String STATE_MSG_UNKONWN = "STATE_MSG_UNKONWN";
}
