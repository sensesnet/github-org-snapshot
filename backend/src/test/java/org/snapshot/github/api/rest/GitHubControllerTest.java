package org.snapshot.github.api.rest;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.snapshot.github.model.GetGitHubRepoResponse;
import org.snapshot.github.model.GitHubRepoResponseItem;
import org.snapshot.github.service.GetGitHubRepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = GitHubController.class)
class GitHubControllerTest {

    private static final GitHubRepoResponseItem REPO_A = GitHubRepoResponseItem.builder()
            .name("some-repo-A")
            .htmlUrl("some-a-url.com")
            .stargazersCount(150)
            .forksCount(30)
            .language("Java")
            .updatedAt("2024-01-01T00:00:00Z")
            .description("Some description A")
            .build();

    private static final GitHubRepoResponseItem REPO_B = GitHubRepoResponseItem.builder()
            .name("some-repo-B")
            .htmlUrl("some-b-url.com")
            .stargazersCount(100)
            .forksCount(20)
            .language("Java")
            .updatedAt("2024-01-02T00:00:00Z")
            .description("Some description B")
            .build();

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private GetGitHubRepoService getGitHubRepoService;

    @Test
    void shouldFetchTopRepositoriesSuccessfullyWithDefaults() throws Exception {
        GetGitHubRepoResponse expectedResponse = GetGitHubRepoResponse.builder()
                .repos(List.of(REPO_A, REPO_B))
                .build();

        when(getGitHubRepoService.fetchTopRepositories("test-org", 5, "stars"))
                .thenReturn(expectedResponse);

        mockMvc.perform(get("/github-org-snapshot/v1/api/org/test-org/repos")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));
    }

    @Test
    void shouldFetchTopRepositoriesSuccessfullyWithCustomLimitAndSort() throws Exception {
        GetGitHubRepoResponse expectedResponse = GetGitHubRepoResponse.builder()
                .repos(List.of(REPO_B))
                .build();

        when(getGitHubRepoService.fetchTopRepositories("test-org", 1, "updated"))
                .thenReturn(expectedResponse);

        mockMvc.perform(get("/github-org-snapshot/v1/api/org/test-org/repos")
                        .param("limit", "1")
                        .param("sort", "updated")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));
    }
}
