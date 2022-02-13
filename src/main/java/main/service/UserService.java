package main.service;

import main.api.response.LoginResponse;
import main.api.response.Response;
import main.api.response.UserResponse;
import main.entity.User;
import main.repository.PostRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final PostRepository postRepository;

    public UserService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public User getCurrentUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        return authentication.getPrincipal() != "anonymousUser" ? (User) authentication.getPrincipal() : null;
    }

    public Response getLoginResponse(User user) {
        Integer moderationPostsCount = user.isModerator() ? postRepository.findAllNewPostsForModerator().size() : 0;
        UserResponse userResponse = new UserResponse(user, moderationPostsCount);
        return new LoginResponse(userResponse);
    }
}
