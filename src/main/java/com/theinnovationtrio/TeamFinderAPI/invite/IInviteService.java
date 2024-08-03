package com.theinnovationtrio.TeamFinderAPI.invite;

import com.theinnovationtrio.TeamFinderAPI.organization.Organization;

import java.util.List;
import java.util.UUID;

public interface IInviteService {

    Invite createInvite();

    boolean existsById(UUID inviteId);

    Invite getInviteById(UUID inviteId);

    List<Invite> getAllInvites();

    Invite updateInvite(UUID inviteId, boolean available);

    void deleteInviteById(UUID inviteId);
}
