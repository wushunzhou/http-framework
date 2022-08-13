package com.dora.httpframework.core.config.base;

import com.dora.httpframework.plugin.PluginSupport;

/**
 * @Describe TODO:
 * @Author dora 1.0.1
 **/
public class ConstantConfig {
    /**
     * testFail teardown注解中调用方法所在类
     */
    public static final Class<?> TEST_ANNOTATION_HOOK_METHOD_BY_CLASS = PluginSupport.class;

    /**
     * plugin节点调用方法所在类
     */
    public static final Class<?> PLUGIN_METHOD_BY_CLASS = PluginSupport.class;

    /**
     * setUp 节点调用方法所在
     */
    public static final Class<?> SETUP_METHOD_BY_CLASS = PluginSupport.class;

    /**
     * TEARDOWN 节点调用方法所在
     */
    public static final Class<?> TEARDOWN_METHOD_BY_CLASS = PluginSupport.class;

    /**
     * onFailure 节点调用方法所在
     */
    public static final Class<?> ONFAILURE_METHOD_BY_CLASS = PluginSupport.class;
}
