package com.theinnovationtrio.TeamFinderAPI.userProjectRole;

import com.theinnovationtrio.TeamFinderAPI.user.UserDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ViewProjectTeam {

    private List<UserDto> proposedMembers;

    private List<UserDto> activeMembers;

    private List<UserDto> pastMembers;
}
