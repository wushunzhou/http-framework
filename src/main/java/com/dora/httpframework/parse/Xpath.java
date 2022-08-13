package com.dora.httpframework.parse;

import com.dora.httpframework.exception.DoraException;
import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Describe 页面处理相关
 * @Author dora 1.0.1
 **/
public class Xpath implements PathParse{
    private Document document;

    private Xpath(String xml) {
        SAXReader reader = new SAXReader();
        try {
            this.document = reader.read(new ByteArrayInputStream(xml.getBytes("UTF-8")));
        } catch (Exception e) {
            throw new DoraException("xml解析失败", e);
        }
    }

    public static Xpath of(String xml)  {
        return new Xpath(xml);
    }

    @Override
    public String get(String path) {
        return document.selectSingleNode(path).getText();
    }

    public List<String> getList(String path) {
        List<Node> selectNodes = document.selectNodes(path);
        return selectNodes.stream().map(Node::getText).collect(Collectors.toList());
    }

    @Override
    public boolean isExist(String path) {
        return document.selectSingleNode(path) != null;
    }

    @Override
    public int size(String path) {
        return getList(path).size();
    }
}
