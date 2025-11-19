package com.nest.diamond.common.config;

import com.nest.diamond.common.util.AES;
import com.nest.diamond.common.util.ApplicationContextUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author starmark
 * @date 19-12-17  下午8:38
 */
public class AESEncryptHandler extends BaseTypeHandler {

    public static String INPUT_KEY = null;
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, AES.encrypt((String)parameter, getNewAesKey()));
    }
    @Override
    public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String columnValue = rs.getString(columnName);
        return AES.decrypt(columnValue, getAesKey());
    }
    @Override
    public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String columnValue = rs.getString(columnIndex);
        return AES.decrypt(columnValue, getAesKey());
    }
    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex)
            throws SQLException {
        String columnValue = cs.getString(columnIndex);
        return AES.decrypt(columnValue, getAesKey());
    }

    public String getAesKey(){
        /*AESKeyConfig aesKeyConfig = ApplicationContextUtil.getApplicationContext().getBean(AESKeyConfig.class);
        if(aesKeyConfig.getAesKey() == null){
            throw new IllegalArgumentException("AES KEY不能为空");
        }
        return aesKeyConfig.getAesKey();*/
        return getNewAesKey();
    }

    public String getNewAesKey(){
        AESKeyConfig aesKeyConfig = ApplicationContextUtil.getApplicationContext().getBean(AESKeyConfig.class);
        if(aesKeyConfig.getAesKey() == null){
            throw new IllegalArgumentException("AES KEY不能为空");
        }

        String key = aesKeyConfig.getAesKey();
        Boolean checkOpen = checkOpen(aesKeyConfig);
        if (checkOpen) {
            if (StringUtils.isEmpty(INPUT_KEY)) {
                throw new IllegalArgumentException("控制台输入AES KEY不能为空");
            }
            key = key + INPUT_KEY;
        }
        return key;
    }

    public Boolean checkOpen(AESKeyConfig aesKeyConfig){
        return StringUtils.isNotEmpty(aesKeyConfig.getConsoleInput())
                && StringUtils.equals(aesKeyConfig.getConsoleInput(), "open");
    }
}