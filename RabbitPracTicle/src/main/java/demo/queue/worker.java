package demo.queue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

public class worker {

    private static final String TASK_QUEUE_NAME = "task_queue";
    public static void main(String[] args) {
        //创建连接
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            Connection connection = factory.newConnection();
            //创建管道
            Channel channel = connection.createChannel();
            //声明队列？
            //为什么在消费者里面声明队列呢？原因是：可能会先开启消费者服务
            /**
             * 参数说明：
             * 第一个参数-TASK_QUEUE_NAME：字符串类型,表示的是当前队列名称
             * 第二个参数-false：持久化队列，改参数的作用是：让rabbit记住该队列，这样子尽管rabbit服务器重启，也能保持该队列中的信息
             * 注意的是：你不能在已经存在的队列上重新使用queueDeclare方法
             * 不会发生遗失
             * 第三个参数-未知
             * 第四个参数-未知
             * 第五个参数-未知
             */
            boolean durable = false;
            channel.queueDeclare(
                    TASK_QUEUE_NAME,
                    durable,
                    false,
                    false,
                    null);
            System.out.println("[*] 正在接受信息，退出请按ctrl+c");
            /**
             * 表示一次只处理一个未传递的信息,当ack被设置为false时候，此效果无效
             */
            channel.basicQos(1); //这个是什么意思？
            /**
             * 信息传递的回调函数
             */
            DeliverCallback deliverCallback = (consumerTag,delivery)->{
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);

                System.out.printf("【*】接收信息：%s%n",message);
                try {
                    if (Objects.equals("play",message))
                        doWork();
                    else
                        doWork(message);
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    System.out.println("【√】 ✅");
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);

                }
            };
            /**
             * ack标识为true的时候，,当前工作线程一次只能处理一个传递的信息，
             * 当当前管道被终止的时候，rabbitmq不知道消息是否被正确处理，只是删除信息，导致信息在未被完全处理之前遗漏
             * 表示胃false的时候，一次接收多个信息，并且当管道被意外终止的时候，消息不会消失而是回退到rabbitmq队列中
             * 并传递给改管道内的其他工作进程
             */
            boolean ack = false; //这又是何意味？
            channel.basicConsume(TASK_QUEUE_NAME,ack,deliverCallback,consumeTag->{});
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }

    }

    private static void doWork(String message) {
        for (char c : message.toCharArray()) {
            if (c == '.') {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().isInterrupted();
                }
            }
        }
    }

    private static void doWork() {
        new Thread(playMusic(Path.of("手写的从前.wav")),"音乐播放器").start();
    }


    public static Runnable playMusic(Path musicFilePath) {
        return () -> {
            Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();
            Mixer mixer = AudioSystem.getMixer(mixerInfo[19]);

            Line.Info[] sourceLineInfo = mixer.getSourceLineInfo();
            try (AudioInputStream stream = AudioSystem.getAudioInputStream(new File(musicFilePath.toUri()))) {
                AudioFormat format = stream.getFormat();
                SourceDataLine line = (SourceDataLine) mixer.getLine(sourceLineInfo[0]);
                DataLine.Info info = new DataLine.Info(Clip.class, format);
                Clip line2 = (Clip) AudioSystem.getLine(info);
                line.addLineListener(new LineEventImpl(LocalDateTime.now()));

                Line[] lines = new Line[]{line, line2};

                if (!mixer.isSynchronizationSupported(lines, true)) {
                    System.out.println("不能并行播放！此混声器不能用于并行播放");
                }

                line.open(format);
                line.start();
                //声音长度

                boolean stopped = false;
                var numberByteStore = new byte[4096];
                int read = stream.read(numberByteStore, 0, 4096);
                while (read != -1) {
                    line.write(numberByteStore, 0, read);
                    read = stream.read(numberByteStore, 0, 4096);
                }
                line.stop();
                line.close();
            } catch (UnsupportedAudioFileException | LineUnavailableException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }

    private static class LineEventImpl implements LineListener {

        private LocalDateTime timeStamp;
        private Thread thread;
        private long framePosition;

        public LineEventImpl() {
        }

        public LineEventImpl(LocalDateTime timeStamp) {
            this.timeStamp = timeStamp;
            this.thread = Thread.currentThread();

        }

        @Override
        public void update(LineEvent event) {
            if (event.getType() == LineEvent.Type.START) {
                //获取当前帧
                System.out.println("【✅】开始播放音乐……");
                framePosition = event.getFramePosition();
            }

            if (event.getType() == LineEvent.Type.CLOSE) {
                System.out.printf("当前线程:%s%n", thread.getName());
                if (event.getFramePosition() >= 19) {
                    System.out.println("音乐正常结束");
                } else {
                    System.out.println("音乐被暂停或者终止");
                }
            }

//            if (event.getLine().isOpen()) {
//                System.out.println("开始播放音乐……");
//                System.out.println(LocalDateTime.now());
//            }

            if (event.getType() == LineEvent.Type.STOP) {
                long framePosition = event.getFramePosition();
                System.out.printf("初始的位置是:%d%n",this.framePosition);
                System.out.printf("当前的位置是:%d%n", framePosition);
                double length = Math.ceil(framePosition / 44100.00);
                System.out.printf("音乐总时长：%.2f秒%n",length);
                System.out.println("音乐结束！");
            }

            if (event.getType() == LineEvent.Type.OPEN) {
                System.out.println(" 【✅】开放线路");
            }
        }
    }
}
