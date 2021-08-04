package com.coles.musicbrainz.client;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLEncoder;

import javax.inject.Singleton;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.apache.http.client.utils.URIBuilder;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.filter.EncodingFilter;
import org.glassfish.jersey.message.GZipEncoder;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import com.coles.musicbrainz.exceptions.CustomException;

import lombok.Data;

@Singleton
@Service
@Data
public class MusicBrainzServiceClient extends APIClient implements InitializingBean {

    @Value("${musicBrainz.read.timeout:5000}")
    public int musicBrainzServiceReadTimeout;

    @Value("${musicBrainz.connect.timeout:1000}")
    public int musicBrainzServiceConnectTimeout;

    private Client client;

    public void afterPropertiesSet() {
        ClientConfig configuration = new ClientConfig();
        configuration.register(GZipEncoder.class);
        configuration.register(EncodingFilter.class);
        configuration.property(ClientProperties.CONNECT_TIMEOUT, musicBrainzServiceReadTimeout);
        configuration.property(ClientProperties.READ_TIMEOUT, musicBrainzServiceConnectTimeout);
        client = ClientBuilder.newClient(configuration);
    }
    public Response getMusicByArtistOrRelease(String artist, String serviceUrl, String fieldName) {
    	URIBuilder ub = null;
		try {
			ub = new URIBuilder(serviceUrl);
			artist = URLEncoder.encode(artist, "UTF-8").toString();	
			ub.addParameter("query",fieldName +":"+artist);
			String url = ub.toString();
			WebTarget webTarget = client.target(url);
	        return operation(HttpMethod.GET, null, webTarget);
		} catch (URISyntaxException e) {
			throw new CustomException("URI syntax exception");
		} catch (UnsupportedEncodingException e) {
			throw new CustomException("Unsupported encoding exception");
		}
    	
    }
}
