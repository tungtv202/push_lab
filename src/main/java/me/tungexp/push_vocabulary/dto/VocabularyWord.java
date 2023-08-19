package me.tungexp.push_vocabulary.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class VocabularyWord {
    @JsonProperty("english")
    private String word;

    @JsonProperty("vietnamese")
    private String meaning;
}
