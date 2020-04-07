package com.archytasit.jersey.multipart.model.databags.file;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import com.archytasit.jersey.multipart.MultiPartConfig;
import com.archytasit.jersey.multipart.model.databags.IDataBagProvider;
import com.archytasit.jersey.multipart.utils.StreamUtils;

public class FileDataBagProvider implements IDataBagProvider<FileDataBag> {

    @Override
    public FileDataBag getDataBag(MultiPartConfig config, MediaType mediaType, MultivaluedMap headers, InputStream inputStream, String fileName) throws IOException {
        File f = File.createTempFile(config.getTempFilePrefix(), config.getTempFileSuffix(), new File(config.getTempFileDirectory()));
        StreamUtils.toTempFileNoClose(inputStream, f);
        return new FileDataBag(f);
    }

}
