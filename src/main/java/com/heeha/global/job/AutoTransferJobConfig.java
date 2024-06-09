package com.heeha.global.job;

import com.heeha.domain.account.service.AccountService;
import com.heeha.domain.autoTransfer.service.AutoTransferService;
import com.heeha.domain.history.service.HistoryService;
import com.heeha.domain.statistics.service.StatisticsSettlementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.DuplicateJobException;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class AutoTransferJobConfig extends DefaultBatchConfiguration {

    private final AccountService accountService;
    private final AutoTransferService autoTransferService;

    @Bean
    public Job autoTransferJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) throws DuplicateJobException {
        log.info("=== autoTransferJob ===");
        Job job = new JobBuilder("autoTransferJob", jobRepository)
                .start(autoTransferStep(jobRepository, transactionManager))
                .build();
        return job;
    }
    // 어떤 역할을 할지

    @Bean
    @JobScope
    public Step autoTransferStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager) {
        log.info("== autoTransferStep ==");
        //log.info(dealDate.toString());
        return new StepBuilder("autoTransferStep", jobRepository)
                .tasklet(new AutoTransferTasklet(autoTransferService, accountService), transactionManager)
                .build();
    }


}
