package redis;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ AtomicCounterTest.class, PipelineSetAddPerformanceTest.class,TransactionTest.class })
public class SpringDataRedisSampleSuite {

}