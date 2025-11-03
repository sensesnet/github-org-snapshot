package org.snapshot.github.model;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Value
@Builder(toBuilder = true)
@Jacksonized
public class GetGitHubRepoResponse {
    List<GitHubRepoResponseItem> repos;
}
