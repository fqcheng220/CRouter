package com.fqcheng220.croutercompiler.utils;

public class Consts {
    public static final String PKG_NAME = "com.fqcheng220.crouterannotation";

    public static final String ANNOTATION_TYPE_HOST = PKG_NAME + ".Host";
    public static final String ANNOTATION_TYPE_PATH = PKG_NAME + ".Path";
    public static final String ANNOTATION_TYPE_QUERY = PKG_NAME + ".Query";

    public static final String POT = ".";
    public static final String NEST_CLS_LINK = "$";

    // Java type
    private static final String LANG = "java.lang";
    public static final String BYTE = LANG + ".Byte";
    public static final String SHORT = LANG + ".Short";
    public static final String INTEGER = LANG + ".Integer";
    public static final String LONG = LANG + ".Long";
    public static final String FLOAT = LANG + ".Float";
    public static final String DOUBLE = LANG + ".Double";
    public static final String BOOLEAN = LANG + ".Boolean";
    public static final String CHAR = LANG + ".Character";
    public static final String STRING = LANG + ".String";
    public static final String SERIALIZABLE = "java.io.Serializable";

    public static final String VOID = LANG + ".Void";

    public static final String OBJECT = LANG + ".Object";
    public static final String LIST = "java.util.List";
    public static final String ARRAYLIST = "java.util.ArrayList";

    //
    public static final String OBSERVABLE = "io.reactivex.Observable";
    public static final String CLZ_RESPONSE = "com.fqcheng220.crouterapi.Response";
    public static final String OBSERVABLE_RESPONSE = OBSERVABLE+"<"+CLZ_RESPONSE+">";
    public static final String POSTCARD = "com.fqcheng220.crouterapi.PostCard";
    public static final String ROUTEMETA = "com.fqcheng220.crouterannotation.entity.RouteMeta";
    public static final String PARAMETERMETA = "com.fqcheng220.crouterannotation.entity.ParameterMeta";
    public static final String METHOD_INVOKER = "com.fqcheng220.crouterannotation.entity.RouteMeta.MethodInvoker";
    public static final String CROUTER = "com.fqcheng220.crouterapi.CRouter";
    public static final String EXCEPTIONCONSTS = "com.fqcheng220.crouterapi.exception.ExceptionConsts";

    public static final String PKG_NAME_GENERATED = "com.fqcheng220.crouterannotation.generated";
    public static final String CLASS_NAME_SEPERATOR = "$$";
    public static final String CLASS_NAME_PREFIX = "CRouter$$";

    public static final String METHOD_INVOKER_CLASS_NAME_PREFIX = "MethodInvoker$$";
}
