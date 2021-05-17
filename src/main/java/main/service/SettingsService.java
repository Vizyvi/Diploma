package main.service;

import main.api.response.SettingsResponse;
import main.model.GlobalSetting;
import main.repository.GlobalSettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SettingsService {

    @Autowired
    private GlobalSettingRepository repository;

    public SettingsResponse getGlobalSettings() {
        SettingsResponse settingsResponse = new SettingsResponse();
        Iterable<GlobalSetting> globalSettings = repository.findAll();
        for(GlobalSetting gs : globalSettings) {
            switch (gs.getCode()) {
                case "MULTIUSER_MODE":
                    settingsResponse.setMultiuserMode(gs.getValue().equals("YES"));
                    break;
                case "POST_PREMODERATION" :
                    settingsResponse.setPostPremoderation(gs.getValue().equals("YES"));
                    break;
                case "STATISTICS_IS_PUBLIC" :
                    settingsResponse.setStatisticsIsPublic(gs.getValue().equals("YES"));
                    break;
                default: break;
            }
        }
        return settingsResponse;
    }
}
