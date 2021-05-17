package main.api.response;

import main.model.User;
import org.springframework.stereotype.Component;

@Component
public class AuthCheckResponse {
    private boolean result;

    private User user;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

// "result": true,
//         "user": {
//         "id": 576,
//         "name": "Дмитрий Петров",
//         "photo": "/avatars/ab/cd/ef/52461.jpg",
//         "email": "petrov@petroff.ru",
//         "moderation": true,
//         "moderationCount": 56,
//         "settings": true
