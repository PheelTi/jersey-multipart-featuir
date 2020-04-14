[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

# jersey-multipart-upload-core-feature

##Introduction 

Alternative Multipart implementation for Jax-RS Jersey 2.x.
This implementation is largely inspired from the current Jersey 2.x implementation, with the following differences:
  - **SERVER SIDE**
    - __Request parsing:__
      - A global request size limit can be set, and a `413 Entity too large` exception is thrown.
      - Use of [Apache commons-fileupload](http://commons.apache.org/proper/commons-fileupload/) for MIME parts parsing in requests
      - MIME parts parsing is customisable, and you can provide your own implementation (see below)
    - __MIME Parts storing__
      - storing each MIME parts is customisable, and you can provide your own implementation (see below)
      - By default, all form fields are stored in memory, and all attachments are stored in temporary files on the file system.
      Temporary files are kept for the whole duration of the request, and are deleted after the response is finished. You may change this
      behaviour (see below).
    - **Value resolvers**
      - This implementation deals more nicely with collection of parameters, and can use MessageBodyWorkers of single object for collection of objects.
      - More control on the annotation `@FormDataParam`: filter on the field: type, content-type, or to map a content-type to another.    
  - **CLIENT SIDE**
    - Custom `ParamConverters` are taken into account to serialize the response.
    
##Quick-start

in your pom.xml:

```xml
    <dependency>
        <artifactId>jersey-multipart-upload-core-feature</artifactId>
        <groupId>io.github.archytas-it</groupId>
        <version>0.0.3</version>
    </dependency>
```

It is also recommended to remove the Jersey multipart official implementation. Some classes/annotations share the same names, but not located in same
packages. They will not conflict but it will be easier for the developer to import the right one.

### Server side

In your resource config:

```java
public class JerseyApplication extends ResourceConfig {
    public JerseyApplication() {
        register(MultipartFeature.class);
        [...]
    }
}
```

In your resource:

```java
@Path("/upload")
public class MyResource {

       @Consumes("multipart/*")
       public void upload(
               @FormDataParam("field") String field,
               @FormDataParam("file") List<FormDataBodyPart> files) {

           [...]


       }
}

```

You can use collections to get several parts named identically. Supported collections are `List`, `SortedSet` and `Set`.

The typing of the parameter is important, and if a `MessageBodyReader` for the particular content-type, or an extractor (via a `ParamConverter` for 
instance) exists, the field will be instanciated as desired.

It is also possible to use the `FormDataBodyPart` or any sub-class. If a sub-class is used, 
only the ones that are instances of the desired type will be returned.

Be careful if you use a type, or an invalid `FormDataBodyPart` sub-class, you will get some *null* values.

### Client side

```java
    Client client = ClientBuilder.newClient(new ClientConfig().register(MultipartFeature.class));

    MultiPart multiPart = MultiPart.formDataMultiPart()
        .add(new FormDataEntityBodyPart<>("field", Entity.text("foo bar")))
        .add(new FormDataFileBodyPart("file", new File("foo.txt")))
        .add(new FormDataFileBodyPart("file", new File("bar.txt")));
    
    Response response = client.target("http://myserver/upload").request().post(multiPart.entity());
    
```

You can fill parts of a `MultiPart` object with any objects that is inherited `BodyPart`, which includes:

- `FormDataBodyPart` abstract base class for any form-data field. 
- `FormDataStringBodyPart` field as a String
- `FormDataEntityBodyPart` can be used to provide a JAXRS entity, to inject any object
- `FormDataStreamBodyPart` body part with free input stream source content
- `FormDataFileBodyPart` field with a file as source content
- `MultiPart` to nest a multipart inside (`multipart/mixed`)
- any subclass you choose to implement

When using `FormDataEntityBodyPart`, a `MessageBodyWriter` for this type and content-type, or `ParamConverter` must exists.
    
## Global configuration

  - Instanciate a `MultiPartConfig` class
  - Register in the resource configuration with a `ContextResolver<MultiPartConfig>`
  
  Available parameters:

| Parameter | Default value | Description |
|----------| ------------|------------|
| tempFileDirectory | value of sytem property `java.io.tmpdir` | placee wher temporary files are created if needed.
| tempFilePrefix | `MULTIPART_` | temporary files prefix |
| tempFileSuffix| *null* | temporary files prefix |
| memorySizeLimit | 10240 bytes (10k) | Used by default form field provider to indicate the threshold to switch between in memory or file storage. Ignored if < 0 |
| defaultCharset | `ISO-8859-1` | default content encoding of incoming parts without charset specification |
| requestSizeLimit | -1 | maximum accepted whole request size, will execute the action defined in the following parameter if encountered. Ignored if < 0  |
| requestSizeLimitAction | throw a `413 Entity too large` exception | Action to take if the request size exceeds the defined value. Don't forget to sink the incoming inputStream if you want to interrupt the request reading with: `StreamUtils.toOutStream(is, new NullOutputStream());`|
| cleanResourceMode | `ALWAYS` | Stored resources have a `cleanup()` method. This parameter tells how resources are cleans. `ALWAYS`: resources are cleaned after the end of the request, `ON_ERROR`: resources are only cleaned if the request did not respond a statuc in the `2xx` range. `NEVER`: never cleaned. |
    
## Annotation `@FormDataParam` 

The annotation can be placed on a resource method parameter, or inside a `@BeanParam`

### option `formDataType()`

Can filter a field type: `BOTH`, `FORMFIELD` or `ATTACHMENT`.
A field is considered to be an attachement if it has a filename.

### option `mapContentTypeAs()`

For complex data transformation, allows to map some content types to others.
example, if you want to consider every `text/plain` content type to be parsed as Json, if you have a json media provider.

`@FormDataParam(value = "field", mapContentTypeAs = {@Map(to= MediaType.APPLICATION_JSON)})`

You can add a `from=` inside the `@Map` to restrict the mapping. By default `from=*/*`. The only first declared matching found will be executed, they are not call recursively.

### option `filterContentType()`

 ***To be documented***
 
## Low-level customizations

  It is possible to implement your own:
  - request multipart parser class
  - parts storage class

The parser is in charge to implement an "iterator" which provides data in an internal model `StreamingPart` 
containing all metadatats of the part, and an `InputStream` providing the part content.

The second one is in charge to consume the content of the `StreamingPart` and to provide an
 instance of `FormDataBodyPart` 

You can set one or the other during the feature registration:

```java
public class JerseyApplication extends ResourceConfig {
    public JerseyApplication() {
        register(new MultipartFeature()
            .withRequestParser(MyRequestParser.inst())
            .withBodyPartProvider(MyBodyPartProvider.inst()));
        [...]
    }
}
```

### Request parser
  
  **Class to implement:** `IRequestParser` 

The default implementation uses [Apache commons-fileupload](http://commons.apache.org/proper/commons-fileupload/)

If you implement yours, pay attention to provide parts as they are sequentially read in a streaming way.
This component **MUST NOT** store temporarly the whole part contents in memory or in the file system,
but only stream parts as they are received in the request `InputStream`.
The way each parts are stored is the responsability of the parts storage implementation of `IFormDataBodyPartProvider` (see below).

  ***To be more documented***
 
### Parts storage

  **Class to implement:** `IFormDataBodyPartProvider`

  The default implementation is:
  - Form field part
     - if size below value specified in the *memorySizeLimit* parameter (10kB by default), part stored **in memory** as a `FormDataStringBodyPart`
     - otherwise, part is stored in a temporary file.
  - Attachment part is stored in a temporary file.
     
Every implementation is in charge to provide an object that is inherited from `FormDataBodyParam`.
The is currenctly 4 implementations of `IFormDataBodyPartProvider`:
    
  - `FieldOrAttachementBodyPartProvider` delegates the job to two different ones, depending if the part is a form-field or not (attachment)
  - `MemoryLimitBodyPartProvider` delegates the job to two different ones, depending of the size of the stored content
  - `FormDataFileBodyPartProvider` stores the content in a `String` in a `FormDataStringBodyPart`
  - `FormDataStringBodyPartProvider`stores the content in a temporary file on the filesystem in a `FormDataFileBodyPart`
     
You can write your own `FormDataBodyPart` sub-class with your own `IFormDataBodyPartProvider`.

> I plan to write an `AmazonS3BodyPart` and its provider in the project that uses this library. 
     
  ***To be more documented***
 
  
 



