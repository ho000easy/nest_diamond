package com.nest.diamond.web.checker;

import com.nest.diamond.common.config.AESEncryptHandler;
import com.nest.diamond.common.util.AES;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.SimpleCommandLinePropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import javax.swing.*;
import java.util.Arrays;
import java.util.Properties;

public class ApplicationStartCheck {
    public static void isValidationSuccessful(String[] args) {
        Properties properties = getProperties(args);
        String configValue = properties.getProperty("nest_diamond.console.input");
        boolean isOpen = StringUtils.isNotEmpty(configValue) && StringUtils.equals(configValue,"open");
        if(!isOpen){
            return;
        }

        String inputValue = getPasswordFromInput();
        String decryptKey = properties.getProperty("nest_diamond.aes.key") + inputValue;
        String decryptStr = AES.decrypt(properties.getProperty("nest_diamond.aes.encrypt"), decryptKey);
        if (StringUtils.isNotEmpty(decryptStr)) {
            AESEncryptHandler.INPUT_KEY = inputValue;
            return;
        }
        System.err.println("校验失败");
        System.exit(1);
    }

    @SneakyThrows
    public static Properties getProperties(String[] args){
        String application_api_file_path = "application-api.properties";
        if(args != null){
            SimpleCommandLinePropertySource simpleCommandLinePropertySource = new SimpleCommandLinePropertySource(args);
            String filePaths = simpleCommandLinePropertySource.getProperty("spring.config.location");
            if(filePaths != null){
                String[] files = filePaths.split(",");
                String specifiedPath = Arrays.stream(files).filter(file->file.contains(application_api_file_path)).findFirst().orElse(null);
                if(specifiedPath != null){
                    return PropertiesLoaderUtils.loadProperties(new FileSystemResource(specifiedPath.substring(5)));
                }
            }
        }
        return PropertiesLoaderUtils.loadProperties(new ClassPathResource(application_api_file_path));
    }


    public static String getPasswordFromInput() {
        // 创建一个 JFrame
        JFrame frame = new JFrame("密码输入对话框");
        // 创建一个密码输入框并设置列数
        JPasswordField passwordField = new JPasswordField(20);
        // 创建一个面板用于包含密码输入框
        JPanel panel = new JPanel();
        panel.add(new JLabel("请输入密码: "));
        panel.add(passwordField);
        // 显示对话框
        String passwordString = "";
        int result = JOptionPane.showConfirmDialog(frame, panel, "输入密码", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            // 用户点击了确定按钮
            char[] password = passwordField.getPassword();
            passwordString = new String(password);
        } else {
            // 用户点击了取消按钮或关闭对话框
            System.err.println("校验失败");
            System.exit(1);
        }
        return passwordString;
    }
}
