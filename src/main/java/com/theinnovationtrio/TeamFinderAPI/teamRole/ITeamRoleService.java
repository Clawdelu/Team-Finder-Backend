package com.theinnovationtrio.TeamFinderAPI.teamRole;

import java.util.List;
import java.util.UUID;

public interface ITeamRoleService {
    TeamRole createTeamRole(TeamRoleDto teamRoleDto);
    List<TeamRole> getListOfTeamRoleByIds(List<UUID> teamRolesIds);
    List<TeamRole> getAllTeamRoles();
    List<TeamRole> getAllSameOrgTeamRoles();
    TeamRole getTeamRoleById(UUID teamRoleId);
    TeamRole updateTeamRole(UUID teamRoleId, TeamRoleDto teamRoleDto);
    void deleteTeamRole(UUID teamRoleId);

}
