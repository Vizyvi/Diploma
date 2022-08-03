package main.service;

import main.api.response.LoginResponse;
import main.api.response.Response;
import main.api.response.UserResponse;
import main.entity.User;
import main.repository.PostRepository;
import main.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final EmailServiceImpl emailServiceImpl;

    public UserService(PostRepository postRepository, UserRepository userRepository, EmailServiceImpl emailServiceImpl) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.emailServiceImpl = emailServiceImpl;
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

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public Response restorePassword(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " has not been found"));
        String hash = emailServiceImpl.sendRecoveryPassword(user);
        user.setCode(hash);
        userRepository.save(user);
        return Response.success();
    }
}
