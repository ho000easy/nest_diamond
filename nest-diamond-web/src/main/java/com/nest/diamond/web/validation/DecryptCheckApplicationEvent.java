package com.nest.diamond.web.validation;

import org.springframework.context.ApplicationEvent;

public class DecryptCheckApplicationEvent extends ApplicationEvent {
    public DecryptCheckApplicationEvent(Object source) {
        super(source);
    }
}
