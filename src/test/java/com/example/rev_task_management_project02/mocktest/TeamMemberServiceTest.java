package com.example.rev_task_management_project02.mocktest;

import com.example.rev_task_management_project02.dao.TeamMemberRepository;
import com.example.rev_task_management_project02.dao.TeamRepository;
import com.example.rev_task_management_project02.dao.UserRepository;
import com.example.rev_task_management_project02.models.Team;
import com.example.rev_task_management_project02.models.TeamMember;
import com.example.rev_task_management_project02.models.User;
import com.example.rev_task_management_project02.services.TeamMemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TeamMemberServiceTest {

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TeamMemberRepository teamMemberRepository;

    @InjectMocks
    private TeamMemberService teamMemberService;

    private User user;
    private Team team;
    private TeamMember teamMember;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setUserId(1L);

        team = new Team();
        team.setTeamId(1L);

        teamMember = new TeamMember();
        teamMember.setUser(user);
        teamMember.setTeam(team);
    }

    @Test
    public void testAddTeamMember_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> teamMemberService.addTeamMember(1L, 1L));
        verify(userRepository, times(1)).findById(1L);
        verify(teamRepository, never()).findById(anyLong());
        verify(teamMemberRepository, never()).save(any(TeamMember.class));
    }

    @Test
    public void testAddTeamMember_TeamNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(teamRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> teamMemberService.addTeamMember(1L, 1L));
        verify(userRepository, times(1)).findById(1L);
        verify(teamRepository, times(1)).findById(1L);
        verify(teamMemberRepository, never()).save(any(TeamMember.class));
    }

    @Test
    public void testAddTeamMember_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));

        teamMemberService.addTeamMember(1L, 1L);

        verify(userRepository, times(1)).findById(1L);
        verify(teamRepository, times(1)).findById(1L);
        verify(teamMemberRepository, times(1)).save(any(TeamMember.class));
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testRemoveTeamMember_TeamMemberNotFound() {
        when(teamMemberRepository.findByTeamIdAndUserId(1L, 1L)).thenReturn(null);

        teamMemberService.removeTeamMember(1L, 1L);

        verify(teamMemberRepository, times(1)).findByTeamIdAndUserId(1L, 1L);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testRemoveTeamMember_Success() {
        when(teamMemberRepository.findByTeamIdAndUserId(1L, 1L)).thenReturn(teamMember);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        teamMemberService.removeTeamMember(1L, 1L);

        verify(teamMemberRepository, times(1)).findByTeamIdAndUserId(1L, 1L);
        verify(teamMemberRepository, times(1)).delete(teamMember);
        verify(userRepository, times(1)).save(user);
    }
}
