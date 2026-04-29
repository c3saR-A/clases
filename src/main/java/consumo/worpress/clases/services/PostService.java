package consumo.worpress.clases.services;



import consumo.worpress.clases.dto.PostDto;

import java.util.List;

public interface PostService {
    List<PostDto> getAllPosts();
    PostDto getPostById(Long id);
    List<PostDto> getPost();
}
