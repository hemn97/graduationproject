package com.sysu.hemn.competitionplatform.service.impl;

import com.sysu.hemn.competitionplatform.entity.BrowseLog;
import com.sysu.hemn.competitionplatform.mapper.BrowseLogMapper;
import com.sysu.hemn.competitionplatform.service.BrowseLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("browseLogService")
public class BrowseLogServiceImpl implements BrowseLogService {

    @Resource
    private BrowseLogMapper browseLogMapper;

    @Override
    public void browse(BrowseLog browseLog) {
        browseLogMapper.insert(browseLog);
    }
}
