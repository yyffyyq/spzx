package zjc.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class CustomIdGenerator {

    @Value("${spzx.identifier:01}") // 默认值01
    private String serverId;

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    /**
     * 生成自定义ID（包含到秒的时间）
     */
    public Long generateCustomId() {
        try {
            // 1. 获取当前日期时间（精确到秒）
            String dateTimeStr = getCurrentDateTime();
            System.out.println("步骤1 - 当前日期时间: " + dateTimeStr);

            // 2. 日期时间从右到左每4个一组转二进制
            String dateTimeBinary = convertDateTimeToBinary(dateTimeStr);
            System.out.println("步骤2 - 日期时间二进制: " + dateTimeBinary);

            // 3. 服务器ID转二进制
            String serverBinary = convertServerIdToBinary(serverId);
            System.out.println("步骤3 - 服务器ID二进制: " + serverBinary);

            // 4. 生成随机位(0或1)
            String randomBit = generateRandomBit();
            System.out.println("步骤4 - 随机位: " + randomBit);

            // 5. 组合所有二进制
            String fullBinary = dateTimeBinary + serverBinary + randomBit;
            System.out.println("步骤5 - 完整二进制: " + fullBinary);
            System.out.println("二进制长度: " + fullBinary.length());

            // 6. 二进制转十进制
            Long finalId = Long.parseLong(fullBinary, 2);
            System.out.println("步骤6 - 最终ID(十进制): " + finalId);

            return finalId;

        } catch (Exception e) {
            System.err.println("生成自定义ID失败: " + e.getMessage());
            return System.currentTimeMillis();
        }
    }

    private String getCurrentDateTime() {
        return LocalDateTime.now().format(DATE_TIME_FORMATTER);
    }

    private String convertDateTimeToBinary(String dateTimeStr) {
        StringBuilder binaryBuilder = new StringBuilder();

        // 从右到左遍历，每4位一组
        for (int i = dateTimeStr.length() - 1; i >= 0; i -= 4) {
            int endIndex = i + 1;
            int startIndex = Math.max(0, i - 3);
            String group = dateTimeStr.substring(startIndex, endIndex);

            int decimal = Integer.parseInt(group);
            String binary = Integer.toBinaryString(decimal);

            // 补齐到14位
            String paddedBinary = String.format("%14s", binary).replace(' ', '0');
            binaryBuilder.insert(0, paddedBinary);
        }

        return binaryBuilder.toString();
    }

    private String convertServerIdToBinary(String serverId) {
        int serverIdInt = Integer.parseInt(serverId);
        String binary = Integer.toBinaryString(serverIdInt);
        return String.format("%6s", binary).replace(' ', '0');
    }

    private String generateRandomBit() {
        return String.valueOf(ThreadLocalRandom.current().nextInt(2));
    }
}