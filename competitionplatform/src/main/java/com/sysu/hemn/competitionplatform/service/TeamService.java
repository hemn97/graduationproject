package com.sysu.hemn.competitionplatform.service;

import com.sysu.hemn.competitionplatform.entity.Team;
import com.sysu.hemn.competitionplatform.entity.Teammate;

import java.util.List;

public interface TeamService {
    Team findMyCompetitionTeam(Long userId, Long comptId); // 找到某竞赛下加入的队伍
    List<Team> findAllMyTeams(Integer pageNumber, Integer pageSize, Long userId, String status, String name);
    List<Team> findAllCompetitionTeams(Integer pageNumber, Integer pageSize, Long comptId, String status, String name);
    Long teamsMaxPage(Integer pageSize, Long comptId, String status, Long captainId, String name);
    Long myTeamsMaxPage(Integer pageSize, Long userId, String status, String name);
    List<Teammate> findTeammates(Integer pageNumber, Integer pageSize, Long teamId, String status, String nickname);
    Long teammatesMaxPage(Integer pageSize, Long teamId, String status, String nickname);
    void createTeam(Team team);
    void dismissTeam(Long teamId, Long userId);
    void updateTeam(Team team);
    void leaveTeam(Long teamId, Long userId);
    void requestJoinTeam(Long teamId, Long userId);
    void handleJoinTeam(Long teamId, Long userId, Long captainId, String status);
}
