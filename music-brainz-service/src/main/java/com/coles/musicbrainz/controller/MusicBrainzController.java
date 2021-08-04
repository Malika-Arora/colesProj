package com.coles.musicbrainz.controller;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coles.musicbrainz.service.MusicBrainzService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/v1/musicbrainz")
public class MusicBrainzController {
	
	@Autowired
	private MusicBrainzService musicBrainzService;
	
	@GetMapping(path = "/getMusic/{artist}")
	@Operation(description = "${sw.musicbrainzsetvice.operation.getMusic}", responses = {
			@ApiResponse(responseCode = "200", description = "Success"),
			@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
			@ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content) })
	public ResponseEntity<String> getMusicByArtist( @PathVariable(value = "artist") String artist){
		String response = musicBrainzService.getMusicByArtist(artist);
		return ResponseEntity.ok(response);
		
	}
	
	


}
