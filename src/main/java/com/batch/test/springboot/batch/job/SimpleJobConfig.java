package com.batch.test.springboot.batch.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j // log 사용을 위한 lombok 어노테이션
@RequiredArgsConstructor // 생성자 DI를 위한 lombok 어노테이션
@Configuration // @Configuration : Spring Batch의 모든 Job은 @Configuration으로 등록해 사용합니다.
public class SimpleJobConfig {
	
    private final JobBuilderFactory jobBuilderFactory; // 생성자 DI 받음
    private final StepBuilderFactory stepBuilderFactory; // 생성자 DI 받음

    /**
     * Simple Bacth Job은 Batch가 수행되면 단순히 log.info(">>>>> This is Step1")를 출력하는 Job 입니다.
     * 
     * Batch Job을 생성하는 simpleJob(Job) 코드를 보시면 simpleStep1(Step)을 품고 있음을 알 수 있습니다. Spring Batch에서 Job은 하나의 배치 작업단위를 의미합니다. 
     * Job 안에는 아래처럼 여러 Step이 존재하고, Step 안에 Tasklet 혹은 Reader & Processor & Writer 묶음이 존재합니다.
     * 하나의 Batch 작업을 수행하기위해 몇 단계의 Step을 밟도록 구성할 수 있다고 이해하시면 됩니다.
     * 
     * Job안에 여러 Step이 있다는건 쉽게 이해되지만, Step이 품고 있는 단위가 애매하게 보이실 수 있습니다.
     * Tasklet 하나와 Reader & Processor & Writer 한 묶음이 같은 레벨입니다.
     * 그래서 Reader & Processor가 끝나고 Tasklet으로 마무리 짓는 등으로 만들순 없다는걸 꼭 명심해주셔야 합니다.
     * Tasklet은 어찌보면 Spring MVC의 @Component, @Bean과 비슷한 역할이라고 보셔도 될 것 같습니다.
     * 명확한 역할은 없지만, 개발자가 지정한 커스텀한 기능을 위한 단위로 보시면 됩니다.
     */
//    @Bean
//    public Job simpleJob() {
//        return jobBuilderFactory.get("simpleJob") // "simpleJob"이란 이름의 Batch Job을 생성합니다. job 이름은 별도로 지정하지 않고, Builder를 통해 지정합니다.
//                .start(simpleStep1())
//                .build();
//    }
//
//    @Bean
//    public Step simpleStep1() {
//        return stepBuilderFactory.get("simpleStep1") // "simpleStep1"이란 이름의 Batch Step을 생성합니다. 마찬가지로 Builder를 통해 이름을 지정합니다.
//                .tasklet((contribution, chunkContext) -> { // step에서 수행될 기능들을 명시합니다. tasklet은 Step안에서 '단일로 수행될 custom 기능'들을 선언할 때 사용합니다. 여기서는 Batch가 수행되면 log.info(">>>>> This is Step1") 가 출력되도록 합니다.
//                    log.info(">>>>> This is Step1");
//                    return RepeatStatus.FINISHED;
//                })
//                .build();
//    }
    
    /**
     * 변경한 코드는 job parameter로 넘겨받은 requestDate 값을 로그에 출력시키는 기능입니다.
     * requestDate 값을 job parameter로 넘겨서 batch를 실행하기 위해선, program arguments에 해당값을 입력해야 합니다.
     * 각자 본인의 IDE에서 설정창에서 program arguments에 'requestDate=20200325'와 같이 입력하면 됩니다.
     * 
     * BATCH_JOB_INSTANCE 테이블은 job parameter에 따라 row가 생성되는 테이블 입니다.
     * 이때 Job Parameter란 Spring Batch가 실행될때 외부에서 받을 수 있는 parameter 값으로 생각할 수 있습니다.
     * 예를 들어, 특정 날짜를 job paramter로 넘기면 spring batch 에서는 해당 날짜 데이터로 조회/가공/입력 등의 작업을 할 수 있습니다.
     * 이때 같은 batch job에 사용한 job parameter가 '다르면' batch_job_instance에 기록되지만, 만약 '같은' job paramter를 사용한 경우 기록되지 않습니다.
     * 
     * 같은 job parameter 값을 입력 후 batch를 실행하면 위와 같이 JobInstanceAlreadyCompleteException 에러가 발생하게되며
     * A job instance already exists and is complete for parameters={requestDate=20200325}. If you want to run this job again, change the parameters.
     * 동일 job을 실행시키고 싶다면 job parameter를 변경하라는 문구를 확인할 수 있습니다.
     * 즉, 동일한 Batch Job을 실행할때는 job parameter 값이 달라질때마다 BATCH_JOB_INSTANCE가 생성되게 됩니다.
     */
//    @Bean
//    public Job simpleJob(){
//        return jobBuilderFactory.get("simpleJob")
//                .start(simpleStep1(null))
//                .build();
//    }
//
//    @Bean
//    @JobScope	
//    public Step simpleStep1(@Value("#{jobParameters[requestDate]}") String requestDate){
//        return stepBuilderFactory.get("simpleStep1")
//                .tasklet((contribution, chunkContext) -> {
//                    log.info(">>>>> This is Step1");
//                    log.info(">>>>> requestDate = {}", requestDate);
//                    return RepeatStatus.FINISHED;
//                })
//                .build();
//    }
    
    /**
     * BATCH_JOB_EXECUTION 테이블은 batch_job_instance와 부모-자식 관계에 있는 테이블로, BATCH_JOB_EXECUTION은 부모 BATCH_JOB_INSTANCE의 성공/실패한 모든 내역을 관리합니다.
     * job parameter가 같은 job의 실행은 batch_job_instance가 생성되지 않고 에러가 발생하기 때문에 기록되지 않는다. 실행한 job instance의 기록만 남는다.
     * 
     * 이번에는 batch job을 강제로 실패시킨뒤 job_execution 테이블에 어떻게 데이터가 insert 되는지 확인해보겠습니다.
     * job parameter를 requestDate=20200327로 변경해 실행시켜 보겠습니다. 의도한대로 batch job이 실패하는 것을 확인할 수 있습니다.
     * 또한 BATCH_JOB_EXECUTION 테이블에는 status fail인 상태로 data가 insert 되는 것을 확인할 수 있습니다.
     */
//    @Bean
//    public Job simpleJob(){
//        return jobBuilderFactory.get("simpleJob")
//                .start(simpleStep1(null))
//                .next(simpleStep2(null))
//                .build();
//    }
//
//    @Bean
//    @JobScope
//    public Step simpleStep1(@Value("#{jobParameters[requestDate]}") String requestDate){
//        return stepBuilderFactory.get("simpleStep1")
//                .tasklet((contribution, chunkContext) -> {
//                    throw new IllegalArgumentException("step1에서 실패합니다.");
//                })
//                .build();
//    }
//
//    @Bean
//    @JobScope
//    public Step simpleStep2(@Value("#{jobParameters[requestDate]}") String requestDate){
//        return stepBuilderFactory.get("simpleStep2")
//                .tasklet((contribution, chunkContext) -> {
//                    log.info(">>>>> This is Step2");
//                    log.info(">>>>> requestDate = {}", requestDate);
//                    return RepeatStatus.FINISHED;
//                })
//                .build();
//    }
    
    /**
     * 이번에는 코드를 수정해 job을 성공시켜보겠습니다.
     * 이전과 동일한 job parameter인 requestDate=20200327를 입력해 batch job을 실행해보면 job이 성공적으로 수행되는 것을 확인할 수 있습니다.
     * BATCH_JOB_EXECUTION 테이블에도 정상적으로 data가 insert 되었습니다.
     * 
     * execution vs instance 
     * 위에서 뭔가 이상한것을 느끼시지 않았나요? 맞습니다. 동일한 job parameter로 동일한 job을 수행을 성공했습니다.
     * 사실 spring batch는 동일한 job parameter로 '성공한 기록'이 있을때에만 재수행을 허용하지 않습니다.
     * 또한 BATCH_JOB_INSTANCE 테이블의 instance id가 BATCH_JOB_EXECUTION 테이블에서는 여러번 나타날 수 있습니다.
     * BATCH_JOB_EXECUTION 테이블을 자세히 보시면 성공했을때와 실패했을때 중복된 job_instance_id가 테이블의 값으로 insert 되어있는 것을 확인할 수 있습니다.
     */
    @Bean
    public Job simpleJob(){
        return jobBuilderFactory.get("simpleJob")
                .start(simpleStep1(null))
                .next(simpleStep2(null))
                .build();
    }

    @Bean
    @JobScope
    public Step simpleStep1(@Value("#{jobParameters[requestDate]}") String requestDate){
        return stepBuilderFactory.get("simpleStep1")
        		.tasklet((contribution, chunkContext) -> {
                    log.info(">>>>> This is Step1");
                    log.info(">>>>> requestDate = {}", requestDate);
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    @JobScope
    public Step simpleStep2(@Value("#{jobParameters[requestDate]}") String requestDate){
        return stepBuilderFactory.get("simpleStep2")
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>>>> This is Step2");
                    log.info(">>>>> requestDate = {}", requestDate);
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}