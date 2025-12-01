package es.demo.test;

import com.snl.es.demo.EsApplication;
import com.snl.es.demo.po.StudentEsPO;
import com.snl.es.demo.repository.StudentEsRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@SpringBootTest(classes = EsApplication.class)
public class DemoTest {
    @Autowired
    private StudentEsRepository studentEsRepository;

    //1.新增
//1.1 新增单个文档
    @Test
    public void saveOne(){
        //1.封装一个studentEsPO
        StudentEsPO studentEsPO=new StudentEsPO(
                UUID.randomUUID().toString(),
                "王翠花",
                18,
                "王翠花喜欢美术,同时唱歌也不错",
                new Date()
        );
        //2.写入到索引作为文档存储
        studentEsRepository.save(studentEsPO);
    }

    //1.2批量新增
    @Test
    public void saveAll(){
        //1.封装列表studentEsPO
        StudentEsPO studentEsPO1=new StudentEsPO(
                UUID.randomUUID().toString(),
                "王五",
                19,
                "喜欢音乐,同时体育也不错",
                new Date()
        );
        StudentEsPO studentEsPO2=new StudentEsPO(
                UUID.randomUUID().toString(),
                "李四",
                20,
                "喜欢电影,同时阅读也不错",
                new Date()
        );
        //封装到列表
        List<StudentEsPO> pos=List.of(studentEsPO1,studentEsPO2);
        //2.调用api
        studentEsRepository.saveAll(pos);
    }
    //2.查询获取文档对象
//2.1查询单个,通过文档id获取查询的 id指的是每个文档属性_id
    @Test
    public void findOne(){
        //1参数 就是uuid
        String id="4a3ed3d0-8ff5-48bc-96b3-cda9b276a1f7";
        //2调用findById方法
        StudentEsPO studentEsPO = studentEsRepository.findById(id).orElseGet(() -> null);
        System.out.println(studentEsPO);
    }

    //2.2查询多个 利用ids列表
    @Test
    public void findAll(){
        //1.封装列表
        List<String> ids=List.of(
                "4a3ed3d0-8ff5-48bc-96b3-cda9b276a1f7",
                "0fae8d0d-387e-4037-bcf0-af4ad2c23d3e"
        );
        //2.调用方法
        Iterable<StudentEsPO> pos = studentEsRepository.findAllById(ids);
        //3.遍历打印
        for (StudentEsPO po : pos) {
            System.out.println(po);
        }
    }

    //3.删除
//3.1 删除单个
    @Test
    public void deleteOne(){
        //1.参数 就是uuid
        String id=
                "af52ecb8-5f40-49e9-b6a5-d34cb4cd022f";
        //2.调用方法
        studentEsRepository.deleteById(id);
    }

    @Test
    public void gen() {
    }
}
