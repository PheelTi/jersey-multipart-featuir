package com.archytasit.jersey.multipart.model.bodyparts.apache;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.RequestContext;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.archytasit.jersey.multipart.MultiPartConfig;
import com.archytasit.jersey.multipart.model.databags.IDataBagProvider;
import com.archytasit.jersey.multipart.model.bodyparts.AttachmentBodyPart;
import com.archytasit.jersey.multipart.model.bodyparts.FormBodyPart;

import com.archytasit.jersey.multipart.model.bodyparts.IBodyPart;
import com.archytasit.jersey.multipart.model.bodyparts.IBodyPartProvider;
import com.archytasit.jersey.multipart.utils.HeadersUtils;
import com.archytasit.jersey.multipart.utils.StreamUtils;

public class ApacheFileUploadBodyPartProvider implements IBodyPartProvider {


    @Override
    public List<IBodyPart> parseRequest(MultiPartConfig config, IDataBagProvider dataBagProvider, MediaType mediaType, MultivaluedMap headers, InputStream stream) {
        RequestContext requestContext = new RequestContextJerseyWrapper(mediaType, headers, stream);

        List<IBodyPart> result = new ArrayList<>();
        try {
            FileItemIterator iter = new ServletFileUpload().getItemIterator(requestContext);
            while (iter.hasNext()) {
                FileItemStream item = iter.next();
                InputStream fieldStream = item.openStream();
                String fieldName = item.getFieldName();
                MultivaluedMap<String, String> partHeaders = HeadersUtils.toMultiValuedMap(item.getHeaders());
                MediaType itemMediaType = HeadersUtils.getMediaType(item.getContentType(), null);
                if (!item.isFormField()) {
                    result.add(new AttachmentBodyPart(fieldName, itemMediaType, partHeaders, item.getName(), dataBagProvider.getDataBag(config, itemMediaType,partHeaders, fieldStream, item.getName())));
                } else {
                    result.add(new FormBodyPart(fieldName, itemMediaType, partHeaders, HeadersUtils.getCharset(StandardCharsets.UTF_8, itemMediaType, headers), StreamUtils.toBytesNoClose(fieldStream)));
                }
            }
        } catch (FileUploadException | IOException e) {
            throw new InternalServerErrorException(e);
        }
        return result;
    }


}
