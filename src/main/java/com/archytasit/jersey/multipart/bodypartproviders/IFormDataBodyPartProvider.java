package com.archytasit.jersey.multipart.bodypartproviders;

import com.archytasit.jersey.multipart.MultiPartConfig;
import com.archytasit.jersey.multipart.FormDataBodyPart;
import com.archytasit.jersey.multipart.parsers.StreamingPart;

import java.io.IOException;

public interface IFormDataBodyPartProvider {

    <T extends FormDataBodyPart> T provideBodyPart(MultiPartConfig config, StreamingPart streamingPart) throws IOException;


}
