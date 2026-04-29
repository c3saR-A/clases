package consumo.worpress.clases.services;



import consumo.worpress.clases.dto.CreatePostRequest;
import consumo.worpress.clases.dto.PagedResponse;
import consumo.worpress.clases.dto.PostDto;

import java.util.List;

public interface WordpressService {
    List<PostDto> getAllPosts();
    PostDto createPost(CreatePostRequest request); // Para @CacheEvict
    PagedResponse<PostDto> getPosts(int page, int size); // Para @Cacheable
}