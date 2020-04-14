package com.archytasit.jersey.multipart;

import com.archytasit.jersey.multipart.utils.HeadersUtils;
import com.archytasit.jersey.multipart.utils.HttpHeaderParameters;
import org.glassfish.jersey.message.internal.HttpDateFormat;
import org.glassfish.jersey.message.internal.HttpHeaderReader;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The type Content disposition.
 */
public class ContentDisposition {


    private String name;
    private String type;

    private String fileName;
    private Date creationDate;
    private Date modificationDate;
    private Date readDate;
    private long size = -1;

    /**
     * With file name content disposition.
     *
     * @param name     the name
     * @param fileName the file name
     * @return the content disposition
     */
    protected static ContentDisposition withFileName(String name, String fileName) {
        ContentDisposition contentDisposition =  new ContentDisposition(name);
        contentDisposition.fileName = fileName;
        return contentDisposition;
    }

    /**
     * From header values content disposition.
     *
     * @param name    the name
     * @param headers the headers
     * @return the content disposition
     */
    public static ContentDisposition fromHeaderValues(final String name, final MultivaluedMap<String, String> headers) {
        if (headers != null && headers.getFirst(HttpHeaders.CONTENT_DISPOSITION.toLowerCase()) != null) {
            try {
                return fromHeaderValue(name, headers.getFirst(HttpHeaders.CONTENT_DISPOSITION.toLowerCase()));
            } catch (ParseException e) {
                Logger.getLogger(HeadersUtils.class.getName()).log(Level.WARNING,e.getLocalizedMessage(), e);
            }
        }
        return defaultValue(name);
    }


    /**
     * From header value content disposition.
     *
     * @param name   the name
     * @param header the header
     * @return the content disposition
     * @throws ParseException the parse exception
     */
    protected static ContentDisposition fromHeaderValue(final String name, final String header) throws ParseException {

        HttpHeaderReader reader = HttpHeaderReader.newInstance(header);
        reader.hasNext();

        ContentDisposition cd = new ContentDisposition(name, reader.nextToken().toString());

        final Map<String, String> parameters = reader.hasNext()
                ? HttpHeaderReader.readParameters(reader, true)
                : null;
        if (parameters != null) {
            cd.fileName = parameters.get(HttpHeaderParameters.ContentDisposition.FILENAME);
            cd.creationDate = createDate(parameters.get(HttpHeaderParameters.ContentDisposition.CREATION_DATE));
            cd.modificationDate = createDate(parameters.get(HttpHeaderParameters.ContentDisposition.MODIFICATION_DATE));
            cd.readDate = createDate(parameters.get(HttpHeaderParameters.ContentDisposition.READ_DATE));
            cd.size = createLong(parameters.get(HttpHeaderParameters.ContentDisposition.SIZE));
        }
        return cd;
    }


    /**
     * Default value content disposition.
     *
     * @param name the name
     * @return the content disposition
     */
    public static ContentDisposition defaultValue(String name) {
        return new ContentDisposition(name);
    }

    private ContentDisposition(String name) {
        this(name, "form-data");
    }

    private ContentDisposition(String name, String type) {
        this.name = name;
        this.type = "form-data";
    }


    @Override
    public String toString() {
        return toStringBuffer().toString();
    }

    /**
     * To string buffer string builder.
     *
     * @return the string builder
     */
    protected StringBuilder toStringBuffer() {
        final StringBuilder sb = new StringBuilder();

        sb.append(type);

        addStringParameter(sb, HttpHeaderParameters.ContentDisposition.NAME, name);
        addStringParameter(sb, HttpHeaderParameters.ContentDisposition.FILENAME, fileName);
        addDateParameter(sb, HttpHeaderParameters.ContentDisposition.CREATION_DATE, creationDate);
        addDateParameter(sb, HttpHeaderParameters.ContentDisposition.MODIFICATION_DATE, modificationDate);
        addDateParameter(sb, HttpHeaderParameters.ContentDisposition.READ_DATE, readDate);
        addLongParameter(sb, HttpHeaderParameters.ContentDisposition.SIZE, size);

        return sb;
    }

    /**
     * Add string parameter.
     *
     * @param sb   the sb
     * @param name the name
     * @param p    the p
     */
    protected void addStringParameter(final StringBuilder sb, final String name, final String p) {
        if (p != null) {
            sb.append("; ").append(name).append("=\"").append(p).append("\"");
        }
    }

    /**
     * Add date parameter.
     *
     * @param sb   the sb
     * @param name the name
     * @param p    the p
     */
    protected void addDateParameter(final StringBuilder sb, final String name, final Date p) {
        if (p != null) {
            sb.append("; ").append(name).append("=\"").append(HttpDateFormat.getPreferredDateFormat().format(p)).append("\"");
        }
    }

    /**
     * Add long parameter.
     *
     * @param sb   the sb
     * @param name the name
     * @param p    the p
     */
    protected void addLongParameter(final StringBuilder sb, final String name, final Long p) {
        if (p != -1) {
            sb.append("; ").append(name).append('=').append(Long.toString(p));
        }
    }


    private static Date createDate(final String value) throws ParseException {
        if (value == null) {
            return null;
        }
        return HttpDateFormat.getPreferredDateFormat().parse(value);
    }

    private static long createLong(final String value) throws ParseException {
        if (value == null) {
            return -1;
        }
        try {
            return Long.parseLong(value);
        } catch (final NumberFormatException e) {
            throw new ParseException("Error parsing size parameter of value, " + value, 0);
        }
    }


}
