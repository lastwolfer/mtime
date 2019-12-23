package com.stylefeng.guns;

import org.junit.Test;

public class MyTest {
    public static synchronized void f(int a){
        if(a>0){
            System.out.println(a);
            f(a-1);
        }
        return;
    }
    @Test
    public void test(){
        f(10);
    }
}
