package cn.tedu.charging.order.points;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@ServerEndpoint("/ws/server/{userId}")
public class WebSocketServerPoint {

    private static final Map<Integer,Session> SESSIONS = new ConcurrentHashMap<>();

    /**
     * 客户端连接成功时调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") Integer userId){
//        log.info("客户端连接成功,连接会话id:{},用户id:{}",session.getId(),userId);
        //通过session对象,给客户端推送一条打招呼消息
        try{
            //准备好消息数据
            String msg="你好,我是服务端,欢迎光临";
            session.getBasicRemote().sendText(msg);
        }catch (Exception e){
            log.error("推送消息给用户失败,连接会话id:{},异常信息:{}",session.getId(),e.getMessage());
        }
        //onOpen是用户建立连接,生成新session对象,将userId和session做put操作,同时避免同一个用户
        //userId在map里已有session,后来的把前面的顶替掉
        //a.判断userId是否在map中已存在
        boolean hasUser = SESSIONS.containsKey(userId);
        if (hasUser){
//            log.info("用户连接已存在,用户id:{}",userId);
            //b.如果已经存在,userId应该先删除
            Session userSession = SESSIONS.get(userId);
            try {
                //断开上一次连接的回话
                userSession.close();
            } catch (IOException e) {
                log.error("发生异常,异常原因:{}",e.getMessage());
            }
            SESSIONS.remove(userId);
        }
        //c.将本次映射数据写到map里
        SESSIONS.put(userId,session);
        //打印在线任务 map里的userId key值个数,就是在线人数
//        log.info("当前在线人数:{}",SESSIONS.size());
    }
    /**
     * 客户端断开连接时调用的方法
     */
    @OnClose
    public void onClose(Session session,@PathParam("userId") Integer userId){
//        log.info("客户端断开连接,连接会话id:{},用户id:{}",session.getId(),userId);
        //如果用户断开连接,说明曾经建立的userId和session的关系就应该消失
        SESSIONS.remove(userId);
//        log.info("当前在线人数:{}",SESSIONS.size());
    }
    /**
     * 客户端发送消息时调用的方法
     */
    @OnMessage
    public void onMessage(String message,Session session,@PathParam("userId") Integer userId){
//        log.info("客户端发送消息,消息内容:{},连接会话id:{},用户id:{}",message,session.getId(),userId);
    }
    /**
     * 连接过程出现异常时调用的方法
     */
    @OnError
    public void onError(Throwable error,Session session,@PathParam("userId") Integer userId){
        log.error("连接过程出现异常,连接会话id:{},用户id:{},异常信息:{}",session.getId(),userId,error.getMessage());
    }

    public void pushMessage(String message, Integer userId) {
        Session session = SESSIONS.get(userId);
        if (Objects.nonNull(session)) {
            //用户连接成功，可以发送信息
            log.info("用户:{},在线,连接会话id:{}",userId,session.getId());
            try{
                //准备好消息数据
                session.getBasicRemote().sendText(message);
            }catch (Exception e){
                log.error("推送消息给用户失败,连接会话id:{},异常信息:{}",session.getId(),e.getMessage());
            }
        }else {
            log.error("用户{},不在线",userId);
        }
    }
}
