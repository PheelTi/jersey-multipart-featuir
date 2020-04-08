package com.archytasit.jersey.multipart.parsers.apache;

import com.archytasit.jersey.multipart.model.IBodyPart;
import com.archytasit.jersey.multipart.model.internal.StreamingPart;
import com.archytasit.jersey.multipart.parsers.IRequestParser;
import com.archytasit.jersey.multipart.parsers.StreamingPartIterator;
import com.archytasit.jersey.multipart.utils.HeadersUtils;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.RequestContext;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Apache part parser.
 */
public class ApachePartParser implements IRequestParser {


    public StreamingPartIterator getPartIterator(MediaType mediaType, MultivaluedMap headers, InputStream stream) {
        RequestContext requestContext = new RequestContextJerseyWrapper(mediaType, headers, stream);

        List<IBodyPart> result = new ArrayList<>();
        try {
            FileItemIterator iter = new ServletFileUpload().getItemIterator(requestContext);

            return new StreamingPartIterator(iter::hasNext, () -> wrap(iter.next()));
        } catch (FileUploadException | IOException e) {
            throw new InternalServerErrorException(e);
        }

    }

    private StreamingPart wrap(FileItemStream item) throws IOException {

        return new StreamingPart(item.getFieldName(),  HeadersUtils.getMediaType(item.getContentType(), null), HeadersUtils.toMultiValuedMap(item.getHeaders()), item.isFormField(), item.getName(), item.openStream());
    }


}
