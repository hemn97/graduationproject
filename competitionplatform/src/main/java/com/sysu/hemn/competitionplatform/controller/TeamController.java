package com.sysu.hemn.competitionplatform.controller;

import com.alibaba.fastjson.JSONObject;
import com.sysu.hemn.competitionplatform.annotation.CheckToken;
import com.sysu.hemn.competitionplatform.annotation.LoginToken;
import com.sysu.hemn.competitionplatform.entity.Team;
import com.sysu.hemn.competitionplatform.entity.Teammate;
import com.sysu.hemn.competitionplatform.service.TeamService;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@EnableAutoConfiguration
@RequestMapping("/competitionplatform/api/team")
@CrossOrigin(origins = "*", maxAge = 3600)
public class TeamController {

    @Resource
    private TeamService teamService;

    // 查询某竞赛中加入的队伍
    @CheckToken
    @RequestMapping(value="/findteam/my", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String findTeamMy(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        Long comptId = Long.parseLong(request.getParameter("comptId"));
        Team team = teamService.findMyCompetitionTeam(userId, comptId);
        JSONObject result = new JSONObject();
        if (team == null) {
            result.put("code", -1);
        } else {
            result.put("code", 1);
            result.put("data", team);
            result.put("isCaptain", team.getCaptainId().equals(userId));
        }
        return result.toJSONString();
    }

    // 查询我加入的队伍
    @CheckToken
    @RequestMapping(value="/findteams/my", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String findTeamsMy(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        Integer pageNumber = Integer.parseInt(request.getParameter("pageNumber"));
        Integer pageSize = Integer.parseInt(request.getParameter("pageSize"));
        String status = convertStatus(request.getParameter("status"));
        String name = request.getParameter("name");
        List<Team> list = teamService.findAllMyTeams(pageNumber, pageSize, userId, status, name);
        JSONObject result = new JSONObject();
        result.put("code", 1);
        result.put("list", list);
        return result.toJSONString();
    }

    // 查询某竞赛中注册的所有队伍
    @LoginToken
    @RequestMapping(value="/findteams/all", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String findTeamsAll(HttpServletRequest request) {
        Integer pageNumber = Integer.parseInt(request.getParameter("pageNumber"));
        Integer pageSize = Integer.parseInt(request.getParameter("pageSize"));
        Long comptId = Long.parseLong(request.getParameter("comptId"));
        String status = convertStatus(request.getParameter("status"));
        String name = request.getParameter("name");
        List<Team> list = teamService.findAllCompetitionTeams(pageNumber, pageSize, comptId, status, name);
        JSONObject result = new JSONObject();
        result.put("code", 1);
        result.put("list", list);
        return result.toJSONString();
    }

    @LoginToken
    @RequestMapping(value="/maxpage/teams", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String maxPageTeams(HttpServletRequest request) {
        Integer pageSize = Integer.parseInt(request.getParameter("pageSize"));
        Long comptId = request.getParameter("comptId")!=null ? Long.parseLong(request.getParameter("comptId")) : null;
        String status = convertStatus(request.getParameter("status"));
        Long captainId = request.getParameter("captainId")!=null ? Long.parseLong(request.getParameter("captainId")) : null;
        String name = request.getParameter("name");
        Long maxPage = teamService.teamsMaxPage(pageSize, comptId, status, captainId, name);
        JSONObject result = new JSONObject();
        result.put("code", 1);
        result.put("maxPage", maxPage);
        return result.toJSONString();
    }


    @LoginToken
    @RequestMapping(value="/maxpage/myteams", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String maxPageMyTeams(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        Integer pageSize = Integer.parseInt(request.getParameter("pageSize"));
        String status = convertStatus(request.getParameter("status"));
        String name = request.getParameter("name");
        Long maxPage = teamService.myTeamsMaxPage(pageSize, userId, status, name);
        JSONObject result = new JSONObject();
        result.put("code", 1);
        result.put("maxPage", maxPage);
        return result.toJSONString();
    }

    @LoginToken
    @RequestMapping(value="/findteammates/list", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String findTeammates(HttpServletRequest request) {
        Integer pageNumber = Integer.parseInt(request.getParameter("pageNumber"));
        Integer pageSize = Integer.parseInt(request.getParameter("pageSize"));
        Long teamId = Long.parseLong(request.getParameter("teamId"));
        String nickname = request.getParameter("nickname");
        String status = request.getParameter("status");
        List<Teammate> list = teamService.findTeammates(pageNumber, pageSize, teamId, status, nickname);
        JSONObject result = new JSONObject();
        result.put("code", 1);
        result.put("list", list);
        return result.toJSONString();
    }

    @LoginToken
    @RequestMapping(value="/maxpage/teammates", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String maxPageTeammates(HttpServletRequest request) {
        Integer pageSize = Integer.parseInt(request.getParameter("pageSize"));
        Long teamId = Long.parseLong(request.getParameter("teamId"));
        String nickName = request.getParameter("nickname");
        String status = request.getParameter("status");
        Long maxPage = teamService.teammatesMaxPage(pageSize, teamId, status, nickName);
        JSONObject result = new JSONObject();
        result.put("code", 1);
        result.put("maxPage", maxPage);
        return result.toJSONString();
    }

    @CheckToken
    @RequestMapping(value="/create", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String createTeam(HttpServletRequest request) {
        String name = request.getParameter("name");
        String motto = request.getParameter("motto");
        Long comptId = Long.parseLong(request.getParameter("comptId"));
        Team team = new Team();
        team.setComptId(comptId);
        team.setCaptainId((Long) request.getAttribute("userId"));
        team.setName(name);
        team.setMotto(motto);
        team.setStatus("正在招募");
        teamService.createTeam(team);
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("code", 1);
        return jsonResponse.toJSONString();
    }

    @CheckToken
    @RequestMapping(value="/dismiss", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String dismissTeam(HttpServletRequest request) {
        Long teamId = Long.parseLong(request.getParameter("teamId"));
        Long userId = (Long) request.getAttribute("userId");
        teamService.dismissTeam(teamId, userId);
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("code", 1);
        return jsonResponse.toJSONString();
    }

    @CheckToken
    @RequestMapping(value="/update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String updateTeam(HttpServletRequest request) {
        Long teamId = Long.parseLong(request.getParameter("teamId"));
        Long userId = (Long) request.getAttribute("userId");
        String name = request.getParameter("name");
        String motto = request.getParameter("motto");
        String status = request.getParameter("status");
        Team team = new Team();
        team.setId(teamId);
        team.setCaptainId(userId);
        team.setName(name);
        team.setMotto(motto);
        team.setStatus(status);
        teamService.updateTeam(team);
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("code", 1);
        return jsonResponse.toJSONString();
    }

    @CheckToken
    @RequestMapping(value="/leave", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String leaveTeam(HttpServletRequest request) {
        Long teamId = Long.parseLong(request.getParameter("teamId"));
        Long userId = (Long) request.getAttribute("userId");

        teamService.leaveTeam(teamId, userId);
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("code", 1);
        return jsonResponse.toJSONString();
    }

    @CheckToken
    @RequestMapping(value="/join", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String joinTeam(HttpServletRequest request) {
        Long teamId = Long.parseLong(request.getParameter("teamId"));
        Long userId = (Long) request.getAttribute("userId");
        teamService.requestJoinTeam(teamId, userId);
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("code", 1);
        return jsonResponse.toJSONString();
    }

    @CheckToken
    @RequestMapping(value="/join/handle", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String handleJoinTeam(HttpServletRequest request) {
        Long teamId = Long.parseLong(request.getParameter("teamId"));
        Long userId = Long.parseLong(request.getParameter("userId"));
        Long capatinId = (Long) request.getAttribute("userId");
        String status = request.getParameter("status");
        teamService.handleJoinTeam(teamId, userId, capatinId, status);
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("code", 1);
        return jsonResponse.toJSONString();
    }

    private static String convertStatus(String status) {
        if(status == null) return null;
        if(status.equals("所有队伍")) {
            return null;
        } else {
            return status;
        }
    }
}
