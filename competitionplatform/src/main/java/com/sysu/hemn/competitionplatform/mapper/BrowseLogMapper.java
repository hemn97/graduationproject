package com.sysu.hemn.competitionplatform.mapper;

import com.sysu.hemn.competitionplatform.entity.BrowseLog;
import org.apache.ibatis.annotations.Insert;

public interface BrowseLogMapper {
    @Insert("insert into t_browse_logs(theme_type, theme_id, user_id, log_time) " +
            "value(#{themeType}, #{themeId}, #{userId}, now())")
    void insert(BrowseLog browseLog);
}
