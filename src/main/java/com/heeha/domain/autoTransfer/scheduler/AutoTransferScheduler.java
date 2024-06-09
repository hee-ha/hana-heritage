package com.heeha.domain.autoTransfer.scheduler;

import com.heeha.domain.autoTransfer.service.AutoTransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AutoTransferScheduler {
    private final AutoTransferService autoTransferService;


}
