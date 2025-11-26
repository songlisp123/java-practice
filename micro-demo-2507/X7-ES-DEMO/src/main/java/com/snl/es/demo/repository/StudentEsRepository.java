package com.snl.es.demo.repository;

import com.snl.es.demo.po.StudentEsPO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * 1. 继承父接口(继承读写现成抽象方法)
 * 2. 指定的当前代理实现的接口类对应持久化对象(泛型)
 * 除了指定持久化类型,还要指定在这个索引中_id的文档主键值 在java语言是用什么类型封装的
 * Integer Long String
 */
public interface StudentEsRepository extends ElasticsearchRepository<StudentEsPO,String> {
}