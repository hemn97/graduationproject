package com.sysu.hemn.competitionplatform.service.impl;

import com.sysu.hemn.competitionplatform.entity.Team;
import com.sysu.hemn.competitionplatform.entity.Teammate;
import com.sysu.hemn.competitionplatform.entity.User;
import com.sysu.hemn.competitionplatform.mapper.TeamMapper;
import com.sysu.hemn.competitionplatform.mapper.TeammateMapper;
import com.sysu.hemn.competitionplatform.service.TeamService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("teamService")
public class TeamServiceImpl implements TeamService {

    @Resource
    private TeamMapper teamMapper;
    @Resource
    private TeammateMapper teammateMapper;

    @Override
    public Team findMyCompetitionTeam(Long userId, Long comptId) {
        return teamMapper.selectMyTeam(userId, comptId, "已加入");
    }

    @Override
    public List<Team> findAllMyTeams(Integer pageNumber, Integer pageSize, Long userId, String status, String name) {
        return teamMapper.listMyTeams((pageNumber-1)*pageSize, pageSize, userId, status, name);
    }

    @Override
    public List<Team> findAllCompetitionTeams(Integer pageNumber, Integer pageSize, Long comptId, String status, String name) {
        return teamMapper.listTeam((pageNumber-1)*pageSize, pageSize, comptId, status, null, name);
    }

    @Override
    public Long teamsMaxPage(Integer pageSize, Long comptId, String status, Long captainId, String name) {
        Long count = teamMapper.count(comptId, status, captainId, name);
        return (count + pageSize - 1) / pageSize;
    }

    @Override
    public Long myTeamsMaxPage(Integer pageSize, Long userId, String status, String name) {
        Long count = teamMapper.countMy(userId, status, name);
        return (count + pageSize - 1) / pageSize;
    }

    @Override
    public List<Teammate> findTeammates(Integer pageNumber, Integer pageSize, Long teamId, String status, String nickname) {
        return teammateMapper.list((pageNumber-1)*pageSize, pageSize, teamId, status, nickname);
    }

    @Override
    public Long teammatesMaxPage(Integer pageSize, Long teamId, String status, String nickname) {
        Long count = teammateMapper.count(teamId, status, nickname);
        return (count + pageSize - 1) / pageSize;
    }

    @Override
    public void createTeam(Team team) {
        teamMapper.insert(team);
        teammateMapper.insert(team.getId(), team.getCaptainId(), "已加入");
    }

    @Override
    public void dismissTeam(Long teamId, Long userId) {
        Team team = teamMapper.select(teamId);
        if (team.getCaptainId().equals(userId)) {    // 只有队长才具备权限
            teamMapper.delete(teamId);
            teammateMapper.delete(teamId, null, null);
        }
    }

    @Override
    public void updateTeam(Team team) {
        if (teamMapper.select(team.getId()).getCaptainId().equals(team.getCaptainId())) { // 只有队长才具备权限
            teamMapper.update(team);
        }
    }

    @Override
    public void leaveTeam(Long teamId, Long userId) {
        teammateMapper.delete(teamId, userId, null);
    }

    @Override
    public void requestJoinTeam(Long teamId, Long userId) {
        Teammate teammate = teammateMapper.select(teamId, userId, null);
        if (teammate == null) {
            teammateMapper.insert(teamId, userId, "审批中");
        }
    }

    @Override
    public void handleJoinTeam(Long teamId, Long userId, Long captainId, String status) {
        if (teamMapper.select(teamId).getCaptainId().equals(captainId)) {
            if (status.equals("同意")) {
                teammateMapper.update(teamId, userId, "已加入");
                //teammateMapper.delete(null, userId, "审批中");
            } else if (status.equals("拒绝")){
                teammateMapper.delete(teamId, userId, "审批中");
            } else if (status.equals("移出")){
                teammateMapper.delete(teamId, userId, "已加入");
            }
        }
    }

}
