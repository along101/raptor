package com.ppdai.raptor.codegen.java.filter;



import java.util.List;

/**
 * @author zhangchengxi
 * Date 2018/5/8
 */
public class CodegenFilterFactory {

    public static final CodegenFilter DEFAULT = new CodegenFilter();

    // TODO: 2018/5/8 L2
    public static CodegenFilter create(List<Long> protoFileList) {
        return DEFAULT;
    }

    public static CodegenFilter create(){
        return DEFAULT;
    }
}
