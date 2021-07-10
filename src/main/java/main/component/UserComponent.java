package main.component;

import main.api.bean.UserBean;
import main.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserComponent {

    public UserBean createBeanFromEntity(User user) {
        UserBean bean = new UserBean();
        bean.id = user.getId();
        bean.name = user.getName();
        bean.photo = user.getPhoto();
        bean.email = user.getEmail();
        if (user.isModerator()) {
            //TODO: some logic with moderationCount
            bean.moderation = true;
        } else {
            bean.moderation = false;
            bean.moderationCount = 0;
        }
        bean.settings = true;

        return bean;
    }
}
