package com.screening.impl;

import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.parser.ConfigurableFTPFileEntryParserImpl;

public class UnixFTPEntryParser extends ConfigurableFTPFileEntryParserImpl {
    public UnixFTPEntryParser(String regex) {
        super(regex);
    }

    @Override
    protected FTPClientConfig getDefaultConfiguration() {
        return null;
    }

    @Override
    public FTPFile parseFTPEntry(String s) {
        return null;
    }
}
