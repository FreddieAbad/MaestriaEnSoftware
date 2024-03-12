package org.apache.camel.learn;

public class Mask {
    public String masking (String data){
        return data.replaceAll(".(?=.{10})","*");
    }
}
