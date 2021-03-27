package com.xiaoye.classgenerator.util;

/**
 * @author xiaoye
 * @create 2021-03-16 1:15
 */
public class ArrayList<E> extends java.util.ArrayList<E> {


    public boolean add(E e) {
        if (e != null) {
            return super.add(e);
        }else{
            throw  new NullPointerException("the param [e] is null");
        }
    }
}
