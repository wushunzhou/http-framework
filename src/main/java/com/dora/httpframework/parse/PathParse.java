package com.dora.httpframework.parse;

/**
 * @Describe 解析
 * @Author dora 1.0.1
 **/
public interface PathParse {

    /**
     * 获取path值
     *
     * @param path
     * @return
     */
    Object get(String path);

    /**
     * path是否存在
     *
     * @param path
     * @return
     */
    boolean isExist(String path);

    int size(String path);


}
