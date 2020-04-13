package com.archytasit.jersey.multipart.parsers.apache;

import com.archytasit.jersey.multipart.ContentDisposition;
import com.archytasit.jersey.multipart.parsers.IRequestParser;
import com.archytasit.jersey.multipart.parsers.StreamingPart;
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

/**
 * The type Apache part parser.
 */
public class ApachePartParser implements IRequestParser {


    public StreamingPartIterator getPartIterator(MediaType mediaType, MultivaluedMap headers, InputStream stream) {
        RequestContext requestContext = new RequestContextJerseyWrapper(mediaType, headers, stream);

        try {
            FileItemIterator iter = new ServletFileUpload().getItemIterator(requestContext);

            return new StreamingPartIterator(iter::hasNext, () -> wrap(iter.next()));
        } catch (FileUploadException | IOException e) {
            throw new InternalServerErrorException(e);
        }

    }

    private StreamingPart wrap(FileItemStream item) throws IOException {
        MultivaluedMap<String, String> headers = HeadersUtils.toMultiValuedMap(item.getHeaders());
        ContentDisposition contentDisposition = ContentDisposition.fromHeaderValues(item.getName(), headers);
        return new StreamingPart(item.getFieldName(), HeadersUtils.getMediaType(item.getContentType()), contentDisposition, headers, item.isFormField(), item.getName(), item.openStream());
    }


}
