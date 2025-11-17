package demo;


import com.snl.redis.RedisApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.RedisSerializer;


@SpringBootTest(classes = RedisApplication.class)
public class TestRedisDemo {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void test() {
        //1.使用string的序列化,我们要保证读写redis的数据都是java的字符串类型
        String key="name";
        String value="snl";
        //2.从redisTemplate中按到底层连接操作读写,需要人为配置序列化
        RedisConnection connection = redisTemplate.getConnectionFactory().getConnection();
        //3.set key value 对数据做序列化
        RedisSerializer<String> serializer = RedisSerializer.string();
        //拿到redis提供的3种序列化方式string序列化器
        byte[] keyBytes = serializer.serialize(key);
        byte[] valueBytes = serializer.serialize(value);
        connection.stringCommands().set(keyBytes,valueBytes);
    }

    @Test
    public void ObjectSe() {
        //1.使用java的序列化,可以在代码中对任意实现了Serializable接口的java对象进行序列化
        String key="user";
        User userValue=new User("snl",18);
        //2.从redisTemplate中按到底层连接操作读写,需要人为配置序列化
        RedisConnection connection = redisTemplate.getConnectionFactory().getConnection();
        //3.set key value 对数据做序列化
        RedisSerializer<String> serializer = RedisSerializer.string();
        RedisSerializer<Object> javaSerializer = RedisSerializer.java();
        //拿到redis提供的3种序列化方式string序列化器
        byte[] keyBytes = serializer.serialize(key);
        byte[] valueBytes = javaSerializer.serialize(userValue);
        connection.stringCommands().set(keyBytes,valueBytes);
    }

    //没有泛型的使用
    @Test
    public void noFanXin() {
        String key="user";
        User userValue=new User("zy",56);
        //从redis中拿到操作zset命令的子命令
        ValueOperations operations = redisTemplate.opsForValue();
        operations.set(key,userValue);

        //为什么呢？为什么这里面会产生十六进制代码？
        Object o = operations.get(key);
        if (o instanceof User) {
            System.out.println(o);
        }
    }

    //使用泛型
    @Test
    public void fanXin() {
        String key = "user";
        User user = new User("ls",65);
        //从redisTemplate中获取set命令子命令
        ValueOperations<String,User> operations = redisTemplate.opsForValue();

        //写入指令
        operations.set(key,user);

        //查询插入的数据
        User userVo = operations.get(key);
        System.out.println(userVo);
    }


}
