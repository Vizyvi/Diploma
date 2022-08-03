package main.service;

import main.api.response.Response;
import main.api.response.SettingsResponse;
import main.entity.GlobalSetting;
import main.entity.User;
import main.exception.DownloadFileException;
import main.repository.GlobalSettingRepository;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SettingsService {
    private static final long MAXSIZE = 2L * 1024 * 1024;

    @Value("${image.width}")
    private int width;

    @Value("${image.height}")
    private int height;

    private final GlobalSettingRepository globalSettingRepository;
    private final ValidateService validateService;
    private final UserService userService;

    public SettingsService(GlobalSettingRepository globalSettingRepository, ValidateService validateService, UserService userService) {
        this.globalSettingRepository = globalSettingRepository;
        this.validateService = validateService;
        this.userService = userService;
    }

    public SettingsResponse getGlobalSettings() {
        SettingsResponse settingsResponse = new SettingsResponse();
        List<GlobalSetting> globalSettings = globalSettingRepository.findAll();
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

    public String saveImage(MultipartFile photo) throws IOException {
        String extension = FilenameUtils.getExtension(photo.getOriginalFilename());
        if(!"jpg".equals(extension) && !"png".equals(extension)) {
            throw new DownloadFileException("Неверный формат файла!");
        }
        if(photo.getSize() > MAXSIZE) {
            throw new DownloadFileException("Размер файла превышает допустимый размер!");
        }
        byte[] bytes = photo.getBytes();
        File file = creatingPathForFile(photo);
        Path path = Paths.get(file.getAbsolutePath());
        Files.write(path, bytes);
        return path.toString();
    }

    public Response saveProfile(String name, String email, String password, Boolean removePhoto, MultipartFile photo) {
        Response response = validateService.validateProfile(name, email, password, photo);
        if(!response.result) {
            return response;
        }
        User user = userService.getCurrentUser();
        if (user == null) {
            return new Response();
        }
        user.setName(name);
        user.setEmail(email);
        if(password != null) {
            PasswordEncoder encoder = new BCryptPasswordEncoder();
            String encodedPassword = encoder.encode(password);
            user.setPassword(encodedPassword);
        }
        if(removePhoto) {
            user.setPhoto(null);
        } else {
            try {
                BufferedImage image = resizeImage(photo, width, height);
                String path = saveImage(image, photo);
                user.setPhoto(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        userService.save(user);
        return Response.success();
    }


    private BufferedImage resizeImage(MultipartFile photo, int targetWidth, int targetHeight) throws IOException {
        BufferedImage image = ImageIO.read(photo.getInputStream());
        Image resultingImage = image.getScaledInstance(targetWidth, targetHeight, Image.SCALE_DEFAULT);
        BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);
        return outputImage;
    }

    private String saveImage(BufferedImage image, MultipartFile photo) throws IOException {
        String extension = FilenameUtils.getExtension(photo.getOriginalFilename());
        File file = creatingPathForFile(photo);
        ImageIO.write(image, extension, file);
        return file.getCanonicalPath();
    }

    private File creatingPathForFile(MultipartFile photo) throws IOException {
        String uuid = UUID.randomUUID().toString();
        String fileSeparator = System.getProperty("file.separator");
        String extension = FilenameUtils.getExtension(photo.getOriginalFilename());
        String name = photo.getName();
        String directoryPath = "/upload/" + Arrays.stream(uuid.split("-")).limit(3).collect(Collectors.joining(fileSeparator));
        File outputFile = new File(directoryPath);
        outputFile.mkdirs();
        String fullFilePath = outputFile + fileSeparator + name + "." + extension;
        File file = new File(fullFilePath);
        file.createNewFile();
        return file;
    }
}
