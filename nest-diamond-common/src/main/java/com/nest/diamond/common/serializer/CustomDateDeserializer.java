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
import java.util.Date;
import java.util.List;

@Slf4j
public class CustomDateDeserializer extends JsonDeserializer<Date> {
    private static final List<String> DATE_FORMAT_PATTERN_LIST = Lists.newArrayList(
            "yyyy-MM-dd HH:mm:ss",
            "yyyy-MM-dd"
    );

    @Override
    public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String dateString = jsonParser.getText();
        if(StringUtils.isBlank(dateString)){
            return null;
        }
        for(String dateFormatPattern : DATE_FORMAT_PATTERN_LIST){
            SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatPattern);
            try {
                return dateFormat.parse(dateString);
            } catch (ParseException e) {
                // Handle the parse exception as per your requirement
                log.error("解析日期出错", e);
            }
        }
        return null;
    }
}
