package com.shdev.oukongli.sample.flink.kafka;

import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.serialization.Encoder;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.filesystem.StreamingFileSink;
import org.apache.flink.streaming.api.functions.sink.filesystem.bucketassigners.DateTimeBucketAssigner;
import org.apache.flink.streaming.api.functions.sink.filesystem.rollingpolicies.DefaultRollingPolicy;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.PrintStream;
import java.util.Properties;

@SpringBootApplication
public class KafkaStreamingFileSink {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(KafkaStreamingFileSink.class);
        startFlinkApplication(args);
    }

    private static void startFlinkApplication(String[] args) throws Exception {
        Properties props = new Properties();
        props.setProperty("bootstrap.servers", "localhost:9092");
        FlinkKafkaConsumer<String> consumer = new FlinkKafkaConsumer<>("test", new SimpleStringSchema(), props);

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        env.enableCheckpointing(5000);

        DataStreamSource<String> dataStreamSource = env.addSource(consumer);
        DataStream<String> dataStream = dataStreamSource.filter((FilterFunction<String>) StringUtils::isNotBlank);
        final StreamingFileSink<String> sink = StreamingFileSink
                .forRowFormat(new Path("file:/Users/ouyangkongli/Downloads/temp"), (Encoder<String>) (element, stream) -> {
                    PrintStream out = new PrintStream(stream);
                    out.println(element);
                })
                .withBucketAssigner(new DateTimeBucketAssigner<>("yyyy-MM-dd"))
                .withRollingPolicy(DefaultRollingPolicy.create().build())
                .build();
        dataStream.addSink(sink);
//        dataStream.print();
        env.execute("StreamingFileSinkProgram");
    }
}
