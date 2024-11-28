package com.example.logging.service;

import com.example.logging.dto.LoggedOrderDTO;
import com.example.logging.model.LoggedOrder;
import com.example.logging.repository.LoggedOrderRepository;
import com.example.logging.utils.mapper.LoggedOrderMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoggedOrderServiceImpl implements LoggedOrderService {

    private final LoggedOrderRepository loggedOrderRepository;
    private final LoggedOrderMapper mapper;

    public LoggedOrderServiceImpl(LoggedOrderRepository loggedOrderRepository,
                                  LoggedOrderMapper mapper) {
        this.loggedOrderRepository = loggedOrderRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public void createLoggedOrder(LoggedOrderDTO dto) {
        LoggedOrder loggedOrder = mapper.toLoggedOrder(dto);

        loggedOrderRepository.save(loggedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LoggedOrderDTO> getAllLoggedOrders(Pageable pageable) {
        return loggedOrderRepository.findAll(pageable)
                .map(mapper::toLoggedOrderDTO);
    }

}
