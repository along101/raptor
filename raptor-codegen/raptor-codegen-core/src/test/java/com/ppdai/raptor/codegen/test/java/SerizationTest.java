//package com.ppdai.raptor.codegen.java.test;
//
//import com.ppdai.framework.raptor.proto.AllTypes;
//
//import java.io.IOException;
//
///**
// * @author zhangchengxi
// * Date 2018/5/3
// */
//public class SerizationTest {
//    public static void main(String[] args) throws IOException {
//        AllTypes allTypes = new AllTypes();
//        allTypes.setInt32(1994);
//        byte[] encode = AllTypes.ADAPTER.encode(allTypes);
//
//        AllTypes decode = AllTypes.ADAPTER.decode(encode);
//        System.out.println(decode.getInt32());
//    }
//}
