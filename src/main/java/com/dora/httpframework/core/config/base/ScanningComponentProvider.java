package com.dora.httpframework.core.config.base;

import com.dora.httpframework.exception.DoraException;
import org.assertj.core.util.Sets;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * @Describe 扫描类（默认全局扫描）
 * @Author dora 1.0.1
 **/
public class ScanningComponentProvider {

    public static final String CLASSPATH_URL_PREFIX = "classpath:";

    public static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";

    public Class<? extends Annotation> annotationType;

    public ScanningComponentProvider(Class<? extends Annotation> annotationType) {
        this.annotationType = annotationType;
    }

    public Set<BeanDefinition> findCandidateComponents(String... basePackages) {
        Set<BeanDefinition> candidates = Sets.newLinkedHashSet();
        for (String basePackage : basePackages) {
            String convertPath = basePackage.replace(".", "/");
            String packageSearchPath = CLASSPATH_URL_PREFIX + convertPath + '/' + DEFAULT_RESOURCE_PATTERN;
            try {

                Resource[] resources = new PathMatchingResourcePatternResolver().getResources(packageSearchPath);

                for (Resource resource : resources) {
                    if (resource.isReadable()) {
                        MetadataReader metadataReader = new CachingMetadataReaderFactory().getMetadataReader(resource);
                        if (isCandidateComponent(metadataReader)) {
                            ScannedGenericBeanDefinition sbd = new ScannedGenericBeanDefinition(metadataReader);
                            sbd.setResource(resource);
                            sbd.setSource(resource);
                            if (isCandidateComponent(metadataReader)) {
                                candidates.add(sbd);
                            }
                        }
                    }
                }
            } catch (IOException e) {
                throw new DoraException("I/O failure during classpath scanning", e);
            }
        }
        return candidates;
    }

    //判断是否带有指定注解
    private boolean isCandidateComponent(MetadataReader metadataReader) {
        ClassMetadata metadata = metadataReader.getClassMetadata();
        if (!metadata.isInterface()) {
            return false;
        }
        try {
            Object o = Class.forName(metadata.getClassName()).getAnnotation(annotationType);
            if (o != null) {
                return true;
            }
            return false;

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

}
