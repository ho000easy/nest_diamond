package com.nest.diamond.common.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class HourOrTimeStampDeserializer extends JsonDeserializer<Long> {
    private static final List<String> DATE_FORMAT_PATTERN_LIST = Lists.newArrayList(
            "yyyy-MM-dd HH:mm:ss",
            "yyyy-MM-dd"
    );

    @Override
    public Long deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String dateString = jsonParser.getText();
        if(StringUtils.isBlank(dateString)){
            return null;
        }
        if(isValidDateTime(dateString)){
            for(String dateFormatPattern : DATE_FORMAT_PATTERN_LIST){
                SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatPattern);
                try {
                    return dateFormat.parse(dateString).getTime();
                } catch (ParseException e) {
                    log.error("解析日期出错", e);
                }
            }

        }
        if(isValidHour(dateString)){
            return Long.parseLong(dateString);
        }
        throw new IllegalArgumentException("字符串格式必须为0-23小时格式或者yyyy-MM-dd HH:mm:ss yyyy-MM-dd日期格式");
    }


    private static final List<String> DATE_TIME_PATTERNS = Arrays.asList(
            "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$", // yyyy-MM-dd HH:mm:ss
            "^\\d{4}-\\d{2}-\\d{2}$" // yyyy-MM-dd
    );

    public static boolean isValidDateTime(String dateTime) {
        for (String pattern : DATE_TIME_PATTERNS) {
            Pattern compiledPattern = Pattern.compile(pattern);
            Matcher matcher = compiledPattern.matcher(dateTime);
            if (matcher.matches()) {
                return true;
            }
        }
        return false;
    }

    private static final String HOUR_PATTERN = "^([01]?[0-9]|2[0-3])$";

    public static boolean isValidHour(String hour) {
        Pattern pattern = Pattern.compile(HOUR_PATTERN);
        Matcher matcher = pattern.matcher(hour);
        return matcher.matches();
    }
}
