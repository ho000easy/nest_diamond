package com.nest.diamond.dubbo.provider;

import org.web3j.crypto.StructuredDataEncoder;

import java.io.IOException;

public class CustomStructuredDataEncoder extends StructuredDataEncoder {
    private byte[] domainSeparator;


    public CustomStructuredDataEncoder(String jsonMessageInString, byte[] domainSeparator) throws IOException {
        super(jsonMessageInString);
        this.domainSeparator = domainSeparator;
    }

    public CustomStructuredDataEncoder(String jsonMessageInString) throws IOException, RuntimeException {
        super(jsonMessageInString);
    }

    public byte[] hashDomain() throws RuntimeException {
        return domainSeparator;
    }

}