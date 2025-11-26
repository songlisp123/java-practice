package com.snl.es.demo.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

/**
 * 框架spring-data-elasticsearch可以根据这个类上
 * 标注的各种注解 识别读写数据对应哪个索引,文档json属性对应po的哪个属性
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
//通过@Document定义当前持久化类对应的是es中文档数据结构
@Document(indexName = "students")
public class StudentEsPO {
    /**
     * {
     *     "id":1,
     *     "name":"张三",
     *     "age":18,
     *     "profile":"张三是一个好学生",
     *     "birthday":"2020-01-01"
     * }
     */
    //主键
    @Id
    @Field(type= FieldType.Keyword)
    private String id;
    @Field(type= FieldType.Keyword)
    private String name;
    @Field(type= FieldType.Integer)
    private Integer age;
    @Field(type= FieldType.Text)
    private String profile;//学生介绍
    @Field(type= FieldType.Date,format = DateFormat.strict_date_hour_minute_second)
    private Date birthday;
}