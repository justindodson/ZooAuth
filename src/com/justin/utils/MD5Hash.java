package com.justin.utils;

import java.security.MessageDigest;
public class MD5Hash{

    // private variables to handle the password
    private String password;
    private byte[] digest;
    private MessageDigest md = MessageDigest.getInstance("MD5");
    private StringBuffer sb = new StringBuffer();

    // constructor
    public MD5Hash(String pw) throws Exception{
        this.password = pw;
    }

    // public method to retrieve the hash string
    public String getHashString(){
       return hashPassword().toString();
    }

    // private method for handling the hash
    // this keeps the hashing logic from outside the class
    private StringBuffer hashPassword(){
        md.update(password.getBytes());
        this.digest = md.digest();
        for(byte b : digest){
            sb.append(String.format("%02x", b & 0xff));
        }
        return sb;
    }
}
