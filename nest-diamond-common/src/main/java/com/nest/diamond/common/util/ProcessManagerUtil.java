package com.nest.diamond.common.util;

import com.google.common.collect.Lists;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

public class ProcessManagerUtil {

    @SneakyThrows
    public static void checkAndKill(String processNameFind, List<String> processNameKillList) {
        // 检查进程是否运行
        for (int i = 0; i < 10; i++) {
            Boolean isRunning = checkProcessExist(processNameFind);

            // 如果进程运行中，则关闭
            if (isRunning) {
                System.out.println("第" + (i + 1) + "次kill进程 " + processNameFind);
                for (String  processNameKill : processNameKillList) {
                    ProcessBuilder killBuilder = new ProcessBuilder("taskkill", "/F", "/IM", processNameKill);
                    killBuilder.start();
                }
                SleepUtil.randomSleep(10, 15);
            } else {
                System.out.println(processNameFind + " 进程已经被杀死.");
                return;
            }

        }
        System.out.println("尝试5次杀死进程仍然没有成功");
    }

    @SneakyThrows
    public static Boolean checkProcessExist(String processName) {
        // 检查进程是否运行
        ProcessBuilder builder = new ProcessBuilder("tasklist");
        Process process = builder.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        boolean isRunning = false;

        while ((line = reader.readLine()) != null) {
            if (line.contains(processName)) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

    public static void main(String[] args) {
        String glitches_process_name_find = "MAYG";
        List<String> glitches_process_name_kills = Lists.newArrayList("MAYG Game Launcher Production.exe", "MAYG-Production.exe");
        ProcessManagerUtil.checkAndKill(glitches_process_name_find,glitches_process_name_kills);
    }
}
