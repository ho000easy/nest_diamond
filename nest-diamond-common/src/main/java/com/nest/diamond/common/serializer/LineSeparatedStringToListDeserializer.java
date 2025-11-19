package com.nest.diamond.common.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LineSeparatedStringToListDeserializer extends JsonDeserializer<List<String>> {

    @Override
    public List<String> deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        String value = parser.readValueAs(String.class);
        if (StringUtils.isNotBlank(value)) {
            String[] values = value.split("\r\n|\n");
            List<String> stringList = new ArrayList<>();
            for (String str : values) {
                stringList.add(str);
            }
            return stringList;
        }
        return null;
    }
}
