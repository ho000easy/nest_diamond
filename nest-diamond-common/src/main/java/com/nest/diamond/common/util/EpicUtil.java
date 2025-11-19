package com.nest.diamond.common.util;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Slf4j
public class EpicUtil {
    private static String glitches_process_name = "LostGlitches.exe";

    public static Boolean startLostGlitchesGame() throws Exception {
        // 创建 ProcessBuilder 对象，使用 cmd /c 执行 start 命令
        ProcessBuilder processBuilder = new ProcessBuilder("cmd", "/c", "start \"\" \"com.epicgames.launcher://apps/fa193e6390404683a6002376f276bfed%3A3a3c13e1304c4dbb925162338934a29e%3A81757143173e4154b143618068e61b92?action=launch&silent=true\"");

        // 启动进程
        Process process = processBuilder.start();

        // 获取命令的输出
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }

        // 获取命令的错误输出（如果有）
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        while ((line = errorReader.readLine()) != null) {
            System.err.println(line);
        }

        // 等待命令执行完成
        process.waitFor();

        // 检查命令是否成功执行
        int exitCode = process.exitValue();
        System.out.println("glitches启动完成code: " + exitCode);

        for (int i = 0; i < 20; i++) {
            Boolean isExist = ProcessManagerUtil.checkProcessExist(glitches_process_name);
            if(isExist){
                System.out.println("游戏启动成功,耗时 " + i*10 + "秒");
                return Boolean.TRUE;
            }
            System.out.println("启动后没有找到进程，睡眠5秒钟");
            SleepUtil.sleep(10);
        }
        return Boolean.FALSE;
    }


    public static boolean startGameLauncher(String launcherProcessNameFind,String launcherPath) throws Exception {
        ProcessBuilder processBuilder = new ProcessBuilder("cmd", "/c", "start \"\" \"" + launcherPath + "\"");

        // 启动进程
        Process process = processBuilder.start();

        // 等待命令执行完成
        process.waitFor();

        // 检查命令是否成功执行
        int exitCode = process.exitValue();
        log.info("游戏启动完成，退出代码: " + exitCode);

        for (int i = 0; i < 20; i++) {
            Boolean isExist = ProcessManagerUtil.checkProcessExist(launcherProcessNameFind);
            if (isExist) {
                log.info("游戏启动成功，耗时 " + i * 5 + "秒");
                return true;
            }
            log.info("启动后没有找到进程，睡眠5秒钟");
            SleepUtil.sleep(5);
        }
        return false;
    }





    @SneakyThrows
    public static void main(String[] args) {
        String launcherPath = "steam://rungameid/2782830";
        String processNameFind = "steam";
        startGameLauncher(processNameFind, launcherPath);

    }
}
