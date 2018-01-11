/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月14日 09:52
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package datasource.mq;

import com.alibaba.fastjson.JSON;
import com.suixingpay.datas.common.datasource.DataDriver;
import com.suixingpay.datas.common.datasource.DataDriverType;
import com.suixingpay.datas.common.datasource.meta.KafkaDriverMeta;
import com.suixingpay.datas.node.core.datasource.mq.KafkaSourceWrapper;
import com.suixingpay.datas.node.core.event.s.MessageEvent;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.*;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月14日 09:52
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月14日 09:52
 */
@RunWith(JUnit4.class)
public class KafkaSourceWrapperTest {
    private static KafkaSourceWrapper wrapper;
    private static DataDriver driver;
    @BeforeClass
    public static void initSource() {
        driver = new DataDriver();
        driver.setType(DataDriverType.KAFKA);
        driver.setUrl("172.16.135.30:9092,172.16.135.30:9093");
        driver.setExtendAttr(new HashMap<>());
        driver.getExtendAttr().put("topic","zkw.t_user_debug-insert");
        driver.getExtendAttr().put("converter","ogg");
        driver.getExtendAttr().put("pollTimeOut","1000");
        driver.getExtendAttr().put("group",UUID.randomUUID().toString());
        wrapper = new KafkaSourceWrapper(driver);
    }
    @Test
    @Ignore
    public void sendCeshi() throws ExecutionException, InterruptedException {
        //driver.setUrl("172.16.136.91:9092,172.16.136.92:9092");
        driver.getExtendAttr().put("topic","zkw.t_ceshi_table");
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, driver.getUrl());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(props);
        /**
        Future<RecordMetadata> a = producer.send(new ProducerRecord<String, String>(driver.getExtendAttr().get(KafkaDriverMeta.INSTANCE.TOPIC),"{\n" +
                "    \"table\":\"zkw.t_ceshi_table\",\n" +
                "    \"op_type\":\"I\",\n" +
                "    \"op_ts\":\"2017-01-01 00:00:00.000000\",\n" +
                "    \"current_ts\":\"2017-01-02T23:59:59.000000\",\n" +
                "    \"pos\":\"1\",\n" +
                "    \"after\":{\n" +
                "        \"ID\":\"1\"\n"+
                "    }\n" +
                "}"));
        a.get();

        Future<RecordMetadata> b = producer.send(new ProducerRecord<String, String>(driver.getExtendAttr().get(KafkaDriverMeta.INSTANCE.TOPIC),"{\n" +
                "    \"table\":\"zkw.t_ceshi_table\",\n" +
                "    \"op_type\":\"U\",\n" +
                "    \"op_ts\":\"2017-01-01 00:00:00.000000\",\n" +
                "    \"current_ts\":\"2017-01-02T23:59:59.000000\",\n" +
                "    \"pos\":\"1\",\n" +
                "    \"before\":{\n" +
                "        \"ID\":\"1\"\n"+
                "    },\"after\":{\n" +
                "        \"ID\":\"1\",\n" +
                "\t\t\"C2\":\"3\"\n" +
                "    }" +
                "}"));
        b.get();
         **/
        Future<RecordMetadata> b = producer.send(new ProducerRecord<String, String>(driver.getExtendAttr().get(KafkaDriverMeta.INSTANCE.TOPIC),"{\n" +
                "    \"table\":\"zkw.t_ceshi_table\",\n" +
                "    \"op_type\":\"D\",\n" +
                "    \"op_ts\":\"2017-01-01 00:00:00.000000\",\n" +
                "    \"current_ts\":\"2017-01-02T23:59:59.000000\",\n" +
                "    \"pos\":\"1\",\n" +"\"before\":{\n" +
                "        \"ID\":\"1\",\n" +
                "\t\t\"C2\":\"3\"\n" +
                "    }" +
                "}"));
        b.get();
    }
    @Test
    @Ignore
    public void sendMsgUpdate() throws ExecutionException, InterruptedException {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, driver.getUrl());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        KafkaProducer<String,String> producer = new KafkaProducer<String, String>(props);
        CyclicBarrier barrier = new CyclicBarrier(6);
        new Thread(){
            @Override
            public void run() {
                for(int i=0; i<50000;i++) {
                    Future<RecordMetadata> a =producer.send(new ProducerRecord<String, String>(driver.getExtendAttr().get(KafkaDriverMeta.INSTANCE.TOPIC),"{\"table\":\"zkw.t_user_debug\",\"op_type\":\"U\",\"op_ts\":\"2013-06-02 22:14:36.000000\",\"current_ts\":\"2015-09-18T13:39:35.447000\",\"pos\":\""+i+"\",\"before\":{\"ID\":\""+i+"\",\"REAL_NAME\":\"joe\"},\"after\":{\"ID\":\""+i+"\",\"REAL_NAME\":\"joe"+i+"\"}}"));
                    try {
                        a.get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    barrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        new Thread(){
            @Override
            public void run() {
                for(int i=50000; i<100000;i++) {
                    Future<RecordMetadata> a =producer.send(new ProducerRecord<String, String>(driver.getExtendAttr().get(KafkaDriverMeta.INSTANCE.TOPIC),"{\"table\":\"zkw.t_user_debug\",\"op_type\":\"U\",\"op_ts\":\"2013-06-02 22:14:36.000000\",\"current_ts\":\"2015-09-18T13:39:35.447000\",\"pos\":\""+i+"\",\"before\":{\"ID\":\""+i+"\",\"REAL_NAME\":\"joe\"},\"after\":{\"ID\":\""+i+"\",\"REAL_NAME\":\"joe"+i+"\"}}"));
                    try {
                        a.get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    barrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        new Thread(){
            @Override
            public void run() {
                for(int i=100000; i<150000;i++) {
                    Future<RecordMetadata> a =producer.send(new ProducerRecord<String, String>(driver.getExtendAttr().get(KafkaDriverMeta.INSTANCE.TOPIC),"{\"table\":\"zkw.t_user_debug\",\"op_type\":\"U\",\"op_ts\":\"2013-06-02 22:14:36.000000\",\"current_ts\":\"2015-09-18T13:39:35.447000\",\"pos\":\""+i+"\",\"before\":{\"ID\":\""+i+"\",\"REAL_NAME\":\"joe\"},\"after\":{\"ID\":\""+i+"\",\"REAL_NAME\":\"joe"+i+"\"}}"));
                    try {
                        a.get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    barrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        new Thread(){
            @Override
            public void run() {
                for(int i=150000; i<200000;i++) {
                    Future<RecordMetadata> a =producer.send(new ProducerRecord<String, String>(driver.getExtendAttr().get(KafkaDriverMeta.INSTANCE.TOPIC),"{\"table\":\"zkw.t_user_debug\",\"op_type\":\"U\",\"op_ts\":\"2013-06-02 22:14:36.000000\",\"current_ts\":\"2015-09-18T13:39:35.447000\",\"pos\":\""+i+"\",\"before\":{\"ID\":\""+i+"\",\"REAL_NAME\":\"joe\"},\"after\":{\"ID\":\""+i+"\",\"REAL_NAME\":\"joe"+i+"\"}}"));
                    try {
                        a.get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    barrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        new Thread(){
            @Override
            public void run() {
                for(int i=200000; i<250000;i++) {
                    Future<RecordMetadata> a =producer.send(new ProducerRecord<String, String>(driver.getExtendAttr().get(KafkaDriverMeta.INSTANCE.TOPIC),"{\"table\":\"zkw.t_user_debug\",\"op_type\":\"U\",\"op_ts\":\"2013-06-02 22:14:36.000000\",\"current_ts\":\"2015-09-18T13:39:35.447000\",\"pos\":\""+i+"\",\"before\":{\"ID\":\""+i+"\",\"REAL_NAME\":\"joe\"},\"after\":{\"ID\":\""+i+"\",\"REAL_NAME\":\"joe"+i+"\"}}"));
                    try {
                        a.get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    barrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        try {
            barrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
        System.out.println("ok");

    }

    @Test
    @Ignore
    public void sendMsg() throws ExecutionException, InterruptedException {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, driver.getUrl());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        KafkaProducer<String,String> producer = new KafkaProducer<String, String>(props);

        Future<RecordMetadata> b =producer.send(new ProducerRecord<String, String>(driver.getExtendAttr().get(KafkaDriverMeta.INSTANCE.TOPIC),"{\"table\":\"zkw.t_user_debug\",\"op_type\":\"I\",\"op_ts\":\"2013-06-02 22:14:36.000000\",\"current_ts\":\"2015-09-18T13:39:35.447000\",\"pos\":\""+999999999+"\",\"after\":{\"ID\":\"zkevin\",\"REAL_NAME\":\"joe\"}}"));
        try {
            b.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Future<RecordMetadata> c =producer.send(new ProducerRecord<String, String>(driver.getExtendAttr().get(KafkaDriverMeta.INSTANCE.TOPIC),"{\"table\":\"zkw.t_user_debug\",\"op_type\":\"I\",\"op_ts\":\"2013-06-02 22:14:36.000000\",\"current_ts\":\"2015-09-18T13:39:35.447000\",\"pos\":\""+999999999+"\",\"after\":{\"ID\":\"zkevin111\",\"REAL_NAME\":\"joe\"}}"));
        try {
            c.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        /**
         *
         *
        CyclicBarrier barrier = new CyclicBarrier(6);

        new Thread(){
            @Override
            public void run() {
                for(int i=0; i<50000;i++) {
                    Future<RecordMetadata> a =producer.send(new ProducerRecord<String, String>(driver.getExtendAttr().get(KafkaDriverMeta.INSTANCE.TOPIC),"{\"table\":\"zkw.t_user_debug\",\"op_type\":\"I\",\"op_ts\":\"2013-06-02 22:14:36.000000\",\"current_ts\":\"2015-09-18T13:39:35.447000\",\"pos\":\""+i+"\",\"after\":{\"ID\":\""+i+"\",\"REAL_NAME\":\"joe\"}}"));
                    try {
                        a.get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    barrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        new Thread(){
            @Override
            public void run() {
                for(int i=50000; i<100000;i++) {
                    Future<RecordMetadata> a =producer.send(new ProducerRecord<String, String>(driver.getExtendAttr().get(KafkaDriverMeta.INSTANCE.TOPIC),"{\"table\":\"zkw.t_user_debug\",\"op_type\":\"I\",\"op_ts\":\"2013-06-02 22:14:36.000000\",\"current_ts\":\"2015-09-18T13:39:35.447000\",\"pos\":\""+i+"\",\"after\":{\"ID\":\""+i+"\",\"REAL_NAME\":\"joe\"}}"));
                    try {
                        a.get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    barrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        new Thread(){
            @Override
            public void run() {
                for(int i=100000; i<150000;i++) {
                    Future<RecordMetadata> a =producer.send(new ProducerRecord<String, String>(driver.getExtendAttr().get(KafkaDriverMeta.INSTANCE.TOPIC),"{\"table\":\"zkw.t_user_debug\",\"op_type\":\"I\",\"op_ts\":\"2013-06-02 22:14:36.000000\",\"current_ts\":\"2015-09-18T13:39:35.447000\",\"pos\":\""+i+"\",\"after\":{\"ID\":\""+i+"\",\"REAL_NAME\":\"joe\"}}"));
                    try {
                        a.get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    barrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        new Thread(){
            @Override
            public void run() {
                for(int i=150000; i<200000;i++) {
                    Future<RecordMetadata> a =producer.send(new ProducerRecord<String, String>(driver.getExtendAttr().get(KafkaDriverMeta.INSTANCE.TOPIC),"{\"table\":\"zkw.t_user_debug\",\"op_type\":\"I\",\"op_ts\":\"2013-06-02 22:14:36.000000\",\"current_ts\":\"2015-09-18T13:39:35.447000\",\"pos\":\""+i+"\",\"after\":{\"ID\":\""+i+"\",\"REAL_NAME\":\"joe\"}}"));
                    try {
                        a.get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    barrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        new Thread(){
            @Override
            public void run() {
                for(int i=200000; i<250000;i++) {
                    Future<RecordMetadata> a =producer.send(new ProducerRecord<String, String>(driver.getExtendAttr().get(KafkaDriverMeta.INSTANCE.TOPIC),"{\"table\":\"zkw.t_user_debug\",\"op_type\":\"I\",\"op_ts\":\"2013-06-02 22:14:36.000000\",\"current_ts\":\"2015-09-18T13:39:35.447000\",\"pos\":\""+i+"\",\"after\":{\"ID\":\""+i+"\",\"REAL_NAME\":\"joe\"}}"));
                    try {
                        a.get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    barrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        try {
            barrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
        System.out.println("ok");
         **/

    }

    @Test
    @Ignore
    public void sendMsgDelete() throws ExecutionException, InterruptedException {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, driver.getUrl());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        KafkaProducer<String,String> producer = new KafkaProducer<String, String>(props);
        CyclicBarrier barrier = new CyclicBarrier(6);
        new Thread(){
            @Override
            public void run() {
                for(int i=0; i<50000;i++) {
                    Future<RecordMetadata> a =producer.send(new ProducerRecord<String, String>(driver.getExtendAttr().get(KafkaDriverMeta.INSTANCE.TOPIC),"{\"table\":\"zkw.t_user_debug\",\"op_type\":\"D\",\"op_ts\":\"2013-06-02 22:14:36.000000\",\"current_ts\":\"2015-09-18T13:39:35.447000\",\"pos\":\""+i+"\",\"before\":{\"ID\":\""+i+"\"}}"));
                    try {
                        a.get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    barrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        new Thread(){
            @Override
            public void run() {
                for(int i=50000; i<100000;i++) {
                    Future<RecordMetadata> a =producer.send(new ProducerRecord<String, String>(driver.getExtendAttr().get(KafkaDriverMeta.INSTANCE.TOPIC),"{\"table\":\"zkw.t_user_debug\",\"op_type\":\"D\",\"op_ts\":\"2013-06-02 22:14:36.000000\",\"current_ts\":\"2015-09-18T13:39:35.447000\",\"pos\":\""+i+"\",\"before\":{\"ID\":\""+i+"\"}}"));
                    try {
                        a.get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    barrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        new Thread(){
            @Override
            public void run() {
                for(int i=100000; i<150000;i++) {
                    Future<RecordMetadata> a =producer.send(new ProducerRecord<String, String>(driver.getExtendAttr().get(KafkaDriverMeta.INSTANCE.TOPIC),"{\"table\":\"zkw.t_user_debug\",\"op_type\":\"D\",\"op_ts\":\"2013-06-02 22:14:36.000000\",\"current_ts\":\"2015-09-18T13:39:35.447000\",\"pos\":\""+i+"\",\"before\":{\"ID\":\""+i+"\"}}"));
                    try {
                        a.get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    barrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        new Thread(){
            @Override
            public void run() {
                for(int i=150000; i<200000;i++) {
                    Future<RecordMetadata> a =producer.send(new ProducerRecord<String, String>(driver.getExtendAttr().get(KafkaDriverMeta.INSTANCE.TOPIC),"{\"table\":\"zkw.t_user_debug\",\"op_type\":\"D\",\"op_ts\":\"2013-06-02 22:14:36.000000\",\"current_ts\":\"2015-09-18T13:39:35.447000\",\"pos\":\""+i+"\",\"before\":{\"ID\":\""+i+"\"}}"));
                    try {
                        a.get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    barrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        new Thread(){
            @Override
            public void run() {
                for(int i=200000; i<250000;i++) {
                    Future<RecordMetadata> a =producer.send(new ProducerRecord<String, String>(driver.getExtendAttr().get(KafkaDriverMeta.INSTANCE.TOPIC),"{\"table\":\"zkw.t_user_debug\",\"op_type\":\"D\",\"op_ts\":\"2013-06-02 22:14:36.000000\",\"current_ts\":\"2015-09-18T13:39:35.447000\",\"pos\":\""+i+"\",\"before\":{\"ID\":\""+i+"\"}}"));
                    try {
                        a.get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    barrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        try {
            barrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
        System.out.println("ok");

    }

    @Test
    @Ignore
    public void fetch() {
        List<MessageEvent> msgs =  wrapper.fetch();
        for (MessageEvent e : msgs) {
            System.out.println(JSON.toJSONString(e));
        }
    }


}
