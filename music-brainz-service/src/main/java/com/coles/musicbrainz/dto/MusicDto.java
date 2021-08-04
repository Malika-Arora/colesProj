package com.coles.musicbrainz.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties({ "created", "offset","artists" })
public class MusicDto {
private int count;
}
