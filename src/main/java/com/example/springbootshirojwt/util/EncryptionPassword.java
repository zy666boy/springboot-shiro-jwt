package com.example.springbootshirojwt.util;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

public class EncryptionPassword {
    /**
     *产生一个加密过的密码
     * @param hashAlgorithmName 加密算法
     * @param orignalPassword 原始密码
     * @param saltName  盐值名称
     * @param hashIterations 加密次数
     * @return
     */
    public static Object encryptionPassword(String hashAlgorithmName,String orignalPassword,String saltName,int hashIterations){
        Object salt= ByteSource.Util.bytes(saltName);;         //处理盐值
        Object o=new SimpleHash(hashAlgorithmName, orignalPassword, salt, hashIterations);     //产生加密后的密码
       return o;
    }
}
