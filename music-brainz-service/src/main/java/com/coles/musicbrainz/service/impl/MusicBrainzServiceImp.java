package com.coles.musicbrainz.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.coles.musicbrainz.client.MusicBrainzServiceClient;
import com.coles.musicbrainz.dto.MusicDto;
import com.coles.musicbrainz.exceptions.CustomException;
import com.coles.musicbrainz.service.MusicBrainzService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MusicBrainzServiceImp implements MusicBrainzService {
	@Autowired
	private MusicBrainzServiceClient musicBrainzServiceClient;

    @Value("${musicBrainz.artist.url}")
    public String artistServiceUrl;
    
    @Value("${musicBrainz.release.url}")
    public String releaseServiceUrl;

	@Override
	public String getMusicByArtist(String artist) {
		String payload = null;
		try {
			payload = musicBrainzServiceClient.getMusicByArtistOrRelease(artist,artistServiceUrl,"artist")
					.readEntity(String.class);
			MusicDto musicDto = new ObjectMapper().readValue(payload, MusicDto.class);
			if(musicDto.getCount() == 1) {
				payload = musicBrainzServiceClient.getMusicByArtistOrRelease(artist,releaseServiceUrl,"artistname")
						.readEntity(String.class);
			}
		} catch (JsonProcessingException e) {
			throw new CustomException("Cannot convert the json to object");
		}
		return payload;
	}
}
