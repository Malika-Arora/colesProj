

package com.coles.musicbrainz.service;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.patch;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.coles.musicbrainz.client.MusicBrainzServiceClient;
import com.github.tomakehurst.wiremock.WireMockServer;

class MusicBrainzServiceImpClientTests {
    private MusicBrainzServiceClient musicBrainzServiceClient;
    private WireMockServer wireMockServer;
    private int singleRequestTime = 1000;


    @BeforeEach
    public void init() {
        wireMockServer = new WireMockServer(wireMockConfig().dynamicPort());
        wireMockServer.start();
        configureFor("localhost", wireMockServer.port());
        musicBrainzServiceClient = new MusicBrainzServiceClient();
        musicBrainzServiceClient.musicBrainzServiceConnectTimeout = 5000;
        musicBrainzServiceClient.musicBrainzServiceReadTimeout = 5000;
        musicBrainzServiceClient.afterPropertiesSet();

    }


    @AfterEach
    public void tearDown() {
        wireMockServer.stop();
    }

    @Test
    void testGetByArtist() {
        String artist = "Fred";
        stubFor(get(urlEqualTo("http://musicbrainz.org/ws/2/artist/?query=artist:" + artist)).willReturn(aResponse().withFixedDelay(singleRequestTime)
                .withStatus(200)));
        Response response = musicBrainzServiceClient.getMusicByArtistOrRelease(artist, "http://musicbrainz.org/ws/2/artist/", "artist");
        assertEquals(200, response.getStatus());
    }
    
    @Test
    void testGetByRelease() {
        String artist = "Fred";
        stubFor(get(urlEqualTo("http://musicbrainz.org/ws/2/release/?query=artistname:" + artist)).willReturn(aResponse().withFixedDelay(singleRequestTime)
                .withStatus(200)));
        Response response = musicBrainzServiceClient.getMusicByArtistOrRelease(artist, "http://musicbrainz.org/ws/2/release/", "artistname");
        assertEquals(200, response.getStatus());
    }

    
}