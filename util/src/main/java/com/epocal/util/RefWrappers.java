package com.epocal.util;

/**
 * Created by dning on 6/16/2017.
 */

/***
 * this class is used to pass reference of primitive type as parameter to function/method
 * and bring the updated value back to caller
 * @param <E> generic type
 */
public class RefWrappers<E> {
    E ref;
    public RefWrappers( E e ){
        ref = e;
    }
    public E getRef() { return ref; }
    public void setRef( E e ){ this.ref = e; }

    public String toString() {
        return ref.toString();
    }
}
