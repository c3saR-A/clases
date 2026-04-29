package consumo.worpress.clases.services.impl;


import consumo.worpress.clases.dto.PostDto;
import consumo.worpress.clases.services.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class PostServiceImpl implements PostService {

    @Value("${wordpress.url}")
    private String wordpressUrl;

    private final RestTemplate restTemplate;

    public PostServiceImpl(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    @Override
    public List<PostDto> getAllPosts() {

        try {

            log.info("Iniciando el Endpoint");
            ResponseEntity<PostDto[]> response = restTemplate.getForEntity(wordpressUrl, PostDto[].class);

            if (response.getStatusCode().is2xxSuccessful()){
                return Arrays.asList(response.getBody());
            } else {
                throw new RuntimeException("Failed Fetch " + response.getStatusCode());
            }

        } catch (Exception e) {
            throw new RuntimeException("Error connecting to Wordpress: " + e.getMessage());
        }
    }

    @Override
    public PostDto getPostById(Long id) {

        String url = wordpressUrl + "/" + id;
        ResponseEntity<PostDto> response = restTemplate.getForEntity(url, PostDto.class);

        if (response.getStatusCode().is2xxSuccessful()){
            return response.getBody();
        } else {
            throw new RuntimeException("Post not found or error: " + response.getStatusCode());
        }
    }

    @Override
    public List<PostDto> getPost() {
        return List.of();
    }
}