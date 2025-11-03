package org.snapshot.github.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder(toBuilder = true)
@Jacksonized
public class GitHubRepoResponseItem {
    String name;
    @JsonProperty("html_url")
    String htmlUrl;
    @JsonProperty("stargazers_count")
    int stargazersCount;
    @JsonProperty("forks_count")
    int forksCount;
    String language;
    @JsonProperty("updated_at")
    String updatedAt;
    String description;
}
