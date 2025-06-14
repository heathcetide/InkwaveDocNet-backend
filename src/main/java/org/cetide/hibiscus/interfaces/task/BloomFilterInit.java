package org.cetide.hibiscus.interfaces.task;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.util.concurrent.Callable;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;


@Component
public class BloomFilterInit {

    private ForkJoinPool forkJoinPool = new ForkJoinPool();

    private static final Logger logger = LoggerFactory.getLogger(BloomFilterInit.class);

    @PostConstruct
    public void init() {
//        long totalPostIds = commentMapper.countPostIds();  // 获取总数量
//        long batchCount = totalPostIds / BATCH_SIZE;
//        if (totalPostIds % BATCH_SIZE != 0) {
//            batchCount++;  // 计算剩余不足 BATCH_SIZE 的数据
//        }
//
//        // 创建 ForkJoin 任务
//        List<Callable<Void>> tasks = new ArrayList<>();
//        for (int i = 0; i < batchCount; i++) {
//            int offset = i * BATCH_SIZE;
//            tasks.add(new LoadBloomFilterTask(offset, BATCH_SIZE));
//        }

        // 执行所有任务
//        forkJoinPool.invokeAll(tasks);
        logger.info("布隆过滤器初始化完成");
    }

    class LoadBloomFilterTask extends RecursiveTask<Void> implements Callable<Void> {

        private int offset;
        private int batchSize;

        LoadBloomFilterTask(int offset, int batchSize) {
            this.offset = offset;
            this.batchSize = batchSize;
        }

        @Override
        protected Void compute() {
//            // 查询批次的数据
//            List<Long> postIds = commentMapper.getPostIdsByBatch(offset, batchSize);
//            // 将数据放入 Bloom Filter
//            for (Long postId : postIds) {
//                BloomFilterConfig.put(postId);
//            }
            return null;
        }

        @Override
        public Void call() {
            return compute(); // 执行计算任务
        }
    }
}
