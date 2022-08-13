package com.dora.httpframework.core.base;

/**
 * @Describe SaveData
 * @Author dora 1.0.1
 **/
public interface SaveDataResponseOptions<R> {
    /**
     * 存储全局缓存
     * @param key
     * @param path
     * @return
     */
    R saveGlobal(String key, String path);

    /**
     * 存入一个作用域在该测试方法的缓存
     * @param key
     * @param path
     * @return
     */
    R saveMethod(String key, String path);

    /**
     * 存入一个作用域在该class的缓存
     * @param key
     * @param path
     * @return
     */
    R saveClass(String key, String path);

    /**
     * 存入一个作用域在当前线程运行的所有用例
     * @param key
     * @param path
     * @return
     */
    R saveThread(String key, String path);
}
