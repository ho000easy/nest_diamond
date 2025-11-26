package com.nest.diamond.web.validation;

import com.nest.diamond.model.domain.Account;
import com.nest.diamond.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Credentials;

@Slf4j
@Component
public class DecryptCheckApplicationListener implements ApplicationListener<DecryptCheckApplicationEvent> {

    @Autowired
    private AccountService accountService;

    @Override
    public void onApplicationEvent(DecryptCheckApplicationEvent decryptCheckApplicationEvent) {
        // 执行校验逻辑，如果校验失败，抛出异常
        if (!isValidationSuccessful()) {
            System.err.println("【校验失败，应用程序即将退出】");
            System.exit(1); // 强制退出应用程序
        }
    }

    private boolean isValidationSuccessful() {
        Account account = accountService.selectNonCustodyOne();
        if ( account == null || StringUtils.isEmpty(account.getPrivateKey())){
            return false;
        }
        String address = Credentials.create(account.getPrivateKey()).getAddress();
        if (StringUtils.equals(address,account.getAddress())){
            log.info("【校验通过 校验通过 校验通过】");
            return true;
        }
        return false;
    }

}
