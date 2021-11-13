package main.component;

import main.api.bean.UserBean;
import main.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserComponent {

    public UserBean createBeanFromEntity(User user) {
        UserBean bean = new UserBean();
        bean.setId(user.getId());
        bean.setName(user.getName());
        bean.setPhoto(user.getPhoto());
        bean.setEmail(user.getEmail());
        if (user.isModerator()) {
            //TODO: some logic with moderationCount
            bean.setModeration(true);
        } else {
            bean.setModeration(false);
            bean.setModerationCount(0);
        }
        bean.setSettings(true);

        return bean;
    }
}
