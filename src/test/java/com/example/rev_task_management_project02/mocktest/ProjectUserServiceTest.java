package com.example.rev_task_management_project02.mocktest;

import com.example.rev_task_management_project02.dao.ProjectUserRepository;
import com.example.rev_task_management_project02.models.ProjectUser;
import com.example.rev_task_management_project02.services.ProjectUserService;
import com.example.rev_task_management_project02.utilities.EntityUpdater;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProjectUserServiceTest {

    @Mock
    private ProjectUserRepository projectUserRepository;

    @Mock
    private EntityUpdater entityUpdater;

    @InjectMocks
    private ProjectUserService projectUserService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateProjectUser() {
        ProjectUser projectUser = new ProjectUser();
        when(projectUserRepository.save(any(ProjectUser.class))).thenReturn(projectUser);

        ProjectUser createdProjectUser = projectUserService.createProjectUser(projectUser);

        assertNotNull(createdProjectUser);
        verify(projectUserRepository, times(1)).save(projectUser);
    }

    @Test
    void testGetProjectUserById() {
        long projectUserId = 1L;
        ProjectUser projectUser = new ProjectUser();
        when(projectUserRepository.findById(projectUserId)).thenReturn(Optional.of(projectUser));

        Optional<ProjectUser> foundProjectUser = projectUserService.getProjectUserById(projectUserId);

        assertTrue(foundProjectUser.isPresent());
        assertEquals(projectUser, foundProjectUser.get());
    }

    @Test
    void testGetProjectUserByIdNotFound() {
        long projectUserId = 1L;
        when(projectUserRepository.findById(projectUserId)).thenReturn(Optional.empty());

        Optional<ProjectUser> foundProjectUser = projectUserService.getProjectUserById(projectUserId);

        assertFalse(foundProjectUser.isPresent());
    }

    @Test
    void testGetAllProjectUsers() {
        ProjectUser projectUser1 = new ProjectUser();
        ProjectUser projectUser2 = new ProjectUser();
        List<ProjectUser> projectUsers = Arrays.asList(projectUser1, projectUser2);
        when(projectUserRepository.findAll()).thenReturn(projectUsers);

        List<ProjectUser> allProjectUsers = projectUserService.getAllProjectUsers();

        assertNotNull(allProjectUsers);
        assertEquals(2, allProjectUsers.size());
    }



    @Test
    void testUpdateProjectUserNotFound() {
        long projectUserId = 1L;
        ProjectUser projectUserDetails = new ProjectUser();
        when(projectUserRepository.findById(projectUserId)).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            projectUserService.updateProjectUser(projectUserId, projectUserDetails);
        });

        assertEquals("ProjectUser not found with id: " + projectUserId, thrown.getMessage());
    }

    @Test
    void testDeleteProjectUser() {
        long projectUserId = 1L;
        doNothing().when(projectUserRepository).deleteById(projectUserId);

        projectUserService.deleteProjectUser(projectUserId);

        verify(projectUserRepository, times(1)).deleteById(projectUserId);
    }
}
