package com.fqcheng220.croutercompiler;

import com.fqcheng220.crouterannotation.Host;
import com.fqcheng220.crouterannotation.Path;
import com.fqcheng220.crouterannotation.Query;
import com.fqcheng220.crouterannotation.entity.ParameterMeta;
import com.fqcheng220.crouterannotation.entity.RouteMeta;
import com.fqcheng220.croutercompiler.utils.Consts;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
@SupportedAnnotationTypes({Consts.ANNOTATION_TYPE_HOST/*,Consts.ANNOTATION_TYPE_PATH*/,Consts.ANNOTATION_TYPE_QUERY})
public class PathProcesser extends AbstractProcessor {
    private Filer mFiler;
    private Elements mElements;
    private Types mTypes;
    private Messager mMessager;

    private ClassName mClzNameObject;
    private ClassName mClzNameVoid;
    private ClassName mClzNameList;
    private ClassName mClzNameArrayList;

    private ClassName mClzNameObservable;
    private ClassName mClzNameResponse;
    private ClassName mClzNameRouteMeta;
    private ClassName mClzNameParameterMeta;
    private ClassName mClzNameMethodInvoker;
    private ClassName mClzNamePostCard;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mFiler = processingEnv.getFiler();
        mElements = processingEnv.getElementUtils();
        mTypes = processingEnv.getTypeUtils();
        mMessager = processingEnv.getMessager();

        mClzNameObject = ClassName.get(mElements.getTypeElement(Consts.OBJECT));
        mClzNameVoid = ClassName.get(mElements.getTypeElement(Consts.VOID));
        mClzNameList = ClassName.get(mElements.getTypeElement(Consts.LIST));
        mClzNameArrayList = ClassName.get(mElements.getTypeElement(Consts.ARRAYLIST));

        mClzNameObservable = ClassName.get(mElements.getTypeElement(Consts.OBSERVABLE));
        mClzNameResponse = ClassName.get(mElements.getTypeElement(Consts.CLZ_RESPONSE));
        mClzNameRouteMeta = ClassName.get(mElements.getTypeElement(Consts.ROUTEMETA));
        mClzNameParameterMeta = ClassName.get(mElements.getTypeElement(Consts.PARAMETERMETA));
        mClzNameMethodInvoker = ClassName.get(mElements.getTypeElement(Consts.METHOD_INVOKER));
        mClzNamePostCard = ClassName.get(mElements.getTypeElement(Consts.POSTCARD));
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elementsSet = roundEnv.getElementsAnnotatedWith(Host.class);
        for (Element element : elementsSet) {
            if(element.getKind() != ElementKind.CLASS)
                continue;
            List<String> listStrClassName = new ArrayList<>();
            mMessager.printMessage(Diagnostic.Kind.NOTE,"Host start inner loop:" + element.getSimpleName().toString() + element.getKind());
            //System.out.println("Host start inner loop:" + element.getSimpleName().toString() + element.getKind());
            List<? extends Element> list = element.getEnclosedElements();
            String host = ((Host) element.getAnnotation(Host.class)).value();
            String hostForClassName = element.getSimpleName().toString();
            //System.out.println("host=" + host);
            //List<RouteMeta> routeMetaList = new ArrayList<>();
            for (Element element1 : list) {
                System.out.println(element1.getSimpleName().toString() + element1.getKind());
                if (element1.getKind() == ElementKind.METHOD) {
                    String path = ((Path) element1.getAnnotation(Path.class)).value();
                    String pathForClassName = element1.getSimpleName().toString();
                    //System.out.println("path=" + path);
                    List<ParameterMeta> parameterMetaList = new ArrayList<>();
                    boolean isRtnObservable = false;
                    //获取方法参数
                    for (VariableElement variableElement : ((ExecutableElement) element1).getParameters()) {
                        ParameterMeta parameterMeta = new ParameterMeta();
                        if (mTypes.isAssignable(variableElement.asType(), mElements.getTypeElement(Consts.POSTCARD).asType())) {
                            parameterMeta.mIsTypePostCard = true;
                        } else {
                            parameterMeta.mIsTypePostCard = false;
                            parameterMeta.mType = getType(variableElement);
                            parameterMeta.mQueryKey = ((Query) variableElement.getAnnotation(Query.class)).value();
                        }
                        parameterMetaList.add(parameterMeta);
                    }
                    //获取方法返回类型 如何判断是否是Observable<Response> 泛型类型 TypeMirror如何转换成TypeElement?
                    //复杂调用版
                    //TypeMirror typeMirrorRtn = ((ExecutableElement) element1).getReturnType();
                    //if(typeMirrorRtn instanceof DeclaredType){
                    //    DeclaredType declaredTypeRtn = (DeclaredType)typeMirrorRtn;
                    //    if (mTypes.isAssignable(declaredTypeRtn.asElement().asType(), mElements.getTypeElement(Consts.OBSERVABLE).asType())) {
                    //        List<? extends TypeMirror> listParameterized = ((DeclaredType) typeMirrorRtn).getTypeArguments();
                    //        isRtnObservable = listParameterized != null && listParameterized.size() == 1 && mTypes.isAssignable(listParameterized.get(0),
                    //            mElements.getTypeElement(Consts.CLZ_RESPONSE).asType());
                    //    }
                    //}
                    /**
                     * 简化调用版
                     * 使用{@link Types#getDeclaredType(TypeElement, TypeMirror...)}
                     */
                    TypeMirror typeMirrorRtn = ((ExecutableElement) element1).getReturnType();
                    if(mTypes.isAssignable(typeMirrorRtn,mTypes.getDeclaredType(mElements.getTypeElement(Consts.OBSERVABLE),mElements.getTypeElement(Consts.CLZ_RESPONSE).asType()))){
                        isRtnObservable = true;
                    }

                    //iterate all method
                    String strServiceMethodName = element1.getSimpleName().toString();
                    String methodClassName = Consts.CLASS_NAME_PREFIX + hostForClassName + Consts.CLASS_NAME_SEPERATOR + pathForClassName;
                    //field "mService"
                    ClassName clzNameService = ClassName.get((TypeElement)element);
                    //field "mRouteMeta"
                    FieldSpec fieldSpecmRouteMeta = FieldSpec.builder(RouteMeta.class,
                        "mRouteMeta",
                        Modifier.PRIVATE).build();
                    //func "getmRouteMeta"
                    MethodSpec methodSpecGetmRouteMeta = MethodSpec.methodBuilder("getmRouteMeta")
                        .addModifiers(Modifier.PUBLIC)
                        .returns(RouteMeta.class)
                        .addCode(CodeBlock.builder().addStatement("return mRouteMeta").build())
                        .build();
                    //nested class implement MethodInvoker start
                    TypeSpec typeSpecNestedClass = generateNestedClass(hostForClassName,pathForClassName,parameterMetaList,isRtnObservable,clzNameService,strServiceMethodName);
                    //nested class implement MethodInvoker end
                    //func constructor
                    MethodSpec methodSpecConstructor = generateConstructor(host,path,parameterMetaList,isRtnObservable,typeSpecNestedClass.name);
                    //generate java file
                    try{
                        JavaFile.builder(Consts.PKG_NAME_GENERATED, TypeSpec.classBuilder(methodClassName)
                            .addModifiers(Modifier.PUBLIC)
//                            .addField(fieldSpecmService)
                            .addField(fieldSpecmRouteMeta)
                            .addMethod(methodSpecConstructor)
                            .addMethod(methodSpecGetmRouteMeta)
                            .addType(typeSpecNestedClass)
                            .build())
                            .build().writeTo(mFiler);
                    }catch (IOException e){
                        e.printStackTrace();
                        mMessager.printMessage(Diagnostic.Kind.ERROR,e.getLocalizedMessage());
                    }
                    listStrClassName.add(Consts.PKG_NAME_GENERATED + Consts.POT + methodClassName);
                }
            }
            generateHostJavaFile(host,hostForClassName,listStrClassName);
        }
        return false;
    }

    private TypeSpec generateNestedClass(String host, String path, List<ParameterMeta> parameterMetaList, boolean isRtnObservable, ClassName clzNameService,String strServiceMethodName){
        TypeName typeName = mClzNameVoid;
        if(isRtnObservable){
            typeName = ParameterizedTypeName.get(
                mClzNameObservable, mClzNameResponse
            );;
        }
        ParameterizedTypeName parameterizedInterface = ParameterizedTypeName.get(
            mClzNameMethodInvoker,
            typeName
        );

        ParameterSpec parameterSpec = ParameterSpec.builder(ArrayTypeName.of(Object.class),"args")
            .build();
        String paras = "";
        for (int i = 0; i < parameterMetaList.size(); i++) {
            paras += "($T)args["+i+"]";
            if(i != parameterMetaList.size() - 1){
                paras += ",";
            }
        }
        ClassName[] types = new ClassName[parameterMetaList.size()];
        for(int i=0;i<parameterMetaList.size();i++){
            if(parameterMetaList.get(i).mIsTypePostCard){
                types[i] = mClzNamePostCard;
            }else{
                types[i] = ClassName.get((Class) parameterMetaList.get(i).mType);
            }
        }
        MethodSpec.Builder methodSpecBuilderInnerInvoke = MethodSpec.methodBuilder("invoke")
            .addAnnotation(Override.class)
            .addModifiers(Modifier.PUBLIC)
            .returns(typeName)
            .addParameter(parameterSpec);
        if(isRtnObservable){
            methodSpecBuilderInnerInvoke
                .beginControlFlow("try")
                .beginControlFlow("if(mService != null)")
                .addStatement("return mService." + strServiceMethodName + "(" + paras + ")", types)
                .endControlFlow()
                .endControlFlow()
                .beginControlFlow("catch($T e)",Exception.class)
                .addStatement("return $T.error(new $T($T.ARGS_MSG_SERVICE_API_PARAMS_CANNOT_FILL))",ClassName.get(mElements.getTypeElement(Consts.OBSERVABLE)),IllegalStateException.class,
                    ClassName.get(mElements.getTypeElement(Consts.EXCEPTIONCONSTS)))
                .endControlFlow()
                .addStatement("return $T.error(new $T($T.STATE_MSG_UNKONWN))",ClassName.get(mElements.getTypeElement(Consts.OBSERVABLE)),IllegalStateException.class,
                    ClassName.get(mElements.getTypeElement(Consts.EXCEPTIONCONSTS)));
        }else{
            methodSpecBuilderInnerInvoke.beginControlFlow("if(mService != null)")
                .addStatement("mService." + strServiceMethodName + "(" + paras + ")", types)
                .endControlFlow()
                .addStatement("return null");
        }
        MethodSpec methodSpecInnerInvoke  = methodSpecBuilderInnerInvoke.build();
        MethodSpec methodSpecInnerConstructor = MethodSpec.constructorBuilder()
            //                            .addParameter(ParameterSpec.builder(clzNameService,
            //                                    "service",
            //                                    Modifier.PRIVATE).build())
            .addModifiers(Modifier.PUBLIC)
            .addStatement("mService=new $T()",clzNameService)
            .build();
        FieldSpec fieldSpecInnermService = FieldSpec.builder(
            clzNameService,
            "mService",
            Modifier.PRIVATE).build();
        TypeSpec typeSpecNestedClass = TypeSpec.classBuilder(Consts.METHOD_INVOKER_CLASS_NAME_PREFIX + host + Consts.CLASS_NAME_SEPERATOR + path)
            .addModifiers(Modifier.PUBLIC,Modifier.STATIC)
            .addSuperinterface(parameterizedInterface)
            .addMethod(methodSpecInnerInvoke)
            .addMethod(methodSpecInnerConstructor)
            .addField(fieldSpecInnermService)
            .build();
        return typeSpecNestedClass;
    }

    private void generateHostJavaFile(String host,String hostForClassName,List<String> listStrClassName){
        //load函数
        TypeName rtnTypeName = ParameterizedTypeName.get(List.class,RouteMeta.class);
        MethodSpec.Builder methodSpecBuilderLoad = MethodSpec.methodBuilder("load")
                .addModifiers(Modifier.PRIVATE)
                .returns(rtnTypeName);
        methodSpecBuilderLoad.addStatement("$T<$T> routeMetaList = new $T<>();",List.class,RouteMeta.class,ArrayList.class);
        for(String strClassName:listStrClassName){
            try{
                methodSpecBuilderLoad.addStatement("routeMetaList.add(new $L().getmRouteMeta());",strClassName);
            }catch (Exception e){
                e.printStackTrace();
                mMessager.printMessage(Diagnostic.Kind.ERROR,e.getLocalizedMessage());
            }
        }
        methodSpecBuilderLoad.addStatement("return routeMetaList");
        //doWhenCRouterInit函数
        MethodSpec methodSpecDoWhenRouterInit = MethodSpec.methodBuilder("doWhenCRouterInit")
                .addModifiers(Modifier.PUBLIC)
                .addStatement("$T.getInstance().initRouteMetas($S, load())",
                        ClassName.get(mElements.getTypeElement(Consts.CROUTER)),
                        host)
                .build();
        //生成java文件
        try{
            String hostClassName = Consts.CLASS_NAME_PREFIX + hostForClassName;
            JavaFile.builder(Consts.PKG_NAME_GENERATED, TypeSpec.classBuilder(hostClassName)
                    .addModifiers(Modifier.PUBLIC)
                    .addMethod(methodSpecBuilderLoad.build())
                    .addMethod(methodSpecDoWhenRouterInit)
                    .build()).build().writeTo(mFiler);
        }catch (IOException e){
            e.printStackTrace();
            mMessager.printMessage(Diagnostic.Kind.ERROR,e.getLocalizedMessage());
        }
    }

    /**
     * 使用TypsSpec自定义的类 如何构造它的ClassName？？？
     * @param host
     * @param path
     * @param parameterMetaList
     * @param isRtnObservable
     * @param strClassNameMethodInvoker
     * @return
     */
    private MethodSpec generateConstructor(String host, String path, List<ParameterMeta> parameterMetaList, boolean isRtnObservable,
        String strClassNameMethodInvoker) {
        MethodSpec.Builder builder = MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC);
        builder.addStatement("$T<$T> parameterMetaList = new $T<>()", List.class,
            ParameterMeta.class, ArrayList.class);
        for (int i = 0; i < parameterMetaList.size(); i++) {
            ParameterMeta parameterMeta = parameterMetaList.get(i);
            builder.addStatement("$T parameterMeta$L = new $T()", ParameterMeta.class, i,
                ParameterMeta.class);
            if(parameterMeta.mIsTypePostCard){
                builder.addStatement("parameterMeta$L.mIsTypePostCard = $L", i, true);
            }else{
                builder.addStatement("parameterMeta$L.mIsTypePostCard = $L", i, false);
                builder.addStatement("parameterMeta$L.mType = $T.class", i, ClassName.get((Class) parameterMeta.mType));
                builder.addStatement("parameterMeta$L.mQueryKey = $S", i, parameterMeta.mQueryKey);
            }
            builder.addStatement("parameterMetaList.add(parameterMeta$L)", i);
        }
        builder.addStatement("mRouteMeta = new RouteMeta($S, $S, parameterMetaList, $L)", host, path, isRtnObservable);
        try{
            mMessager.printMessage(Diagnostic.Kind.NOTE,"strClassNameMethodInvoker="+strClassNameMethodInvoker);
            System.out.println("strClassNameMethodInvoker="+strClassNameMethodInvoker);
            builder.addStatement("mRouteMeta.mMethodInvoker = new $L()", strClassNameMethodInvoker);
        }catch (Exception e){

        }
        return builder.build();
    }

    private Type getType(Element element){
        Type type = Object.class;
        if(mTypes.isAssignable(element.asType(),mElements.getTypeElement(Consts.STRING).asType())){
            type = String.class;
        }else if(mTypes.isAssignable(element.asType(),mElements.getTypeElement(Consts.CHAR).asType())){
            type = Character.class;
        }else if(mTypes.isAssignable(element.asType(),mElements.getTypeElement(Consts.BOOLEAN).asType())){
            type = Boolean.class;
        }else if(mTypes.isAssignable(element.asType(),mElements.getTypeElement(Consts.BYTE).asType())){
            type = Byte.class;
        }else if(mTypes.isAssignable(element.asType(),mElements.getTypeElement(Consts.SHORT).asType())){
            type = Short.class;
        }else if(mTypes.isAssignable(element.asType(),mElements.getTypeElement(Consts.INTEGER).asType())){
            type = Integer.class;
        }else if(mTypes.isAssignable(element.asType(),mElements.getTypeElement(Consts.LONG).asType())){
            type = Long.class;
        }else if(mTypes.isAssignable(element.asType(),mElements.getTypeElement(Consts.FLOAT).asType())){
            type = Float.class;
        }else if(mTypes.isAssignable(element.asType(),mElements.getTypeElement(Consts.DOUBLE).asType())){
            type = Double.class;
        }

        /*else if(mTypes.isAssignable(element.asType(),mElements.getTypeElement(Consts.OBSERVABLE).asType())){
            type = Observable.class;
        }*/
        return type;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_8;
    }
}
