[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

# jersey-multipart-upload-core-feature

##Introduction 

Alternative Multipart implementation for Jax-RS Jersey 2.x.
This implementation is largely inspired from the current Jersey 2.x implementation, with the following differences :
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
      - This implementation deals more nicely with collection of parameters
      - More control on the annotation `@FormDataParam` : filter on the field: type, content-type, or to map a content-type to another.    
  - **CLIENT SIDE**
    - Custom `ParamConverters` are taken into account to serialize the response.
    
##Quick-start

in your pom.xml
It is also recommended to remove the Jersey multipart official implementation. Some classes/annotations has the same names, but not in same
packages. They will not conflict but it will be easier for the developer to import the right one.   

```xml
    <dependency>
        <artifactId>jersey-multipart-upload-core-feature</artifactId>
        <groupId>io.github.archytas-it</groupId>
        <version>0.0.3</version>
    </dependency>
```

### Server side

In your resource config :

```java
public class JerseyApplication extends ResourceConfig {
    public JerseyApplication() {
        register(MultipartFeature.class);
        [...]
    }
}
```

In your resource :

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

### Client side

```java
    Client client = ClientBuilder.newClient(new ClientConfig().register(MultipartFeature.class));

    MultiPart multiPart = MultiPart.formDataMultiPart()
        .add(new FormDataEntityBodyPart<>("field", Entity.text("foo bar")))
        .add(new FormDataFileBodyPart("file", new File("foo.txt")))
        .add(new FormDataFileBodyPart("file", new File("bar.txt")));
    
    Response response = client.target("http://myserver/upload").request().post(multiPart.entity());
    
```
    
## Global configuration
    
 *To be documented*, but quickly :
  - Instanciate a `MultiPartConfig` class
  - Inject in the resource configuration with a `ContextResolver<MultiPartConfig>`

    
## Annotation `@FormDataParam` 

The annotation can be placed on a resource method parameter, or inside a `@BeanParam`

### option `formDataType()`

Can filter a field type : `BOTH`, `FORMFIELD` or `ATTACHMENT`.
A field is considered to be an attachement if it has a filename.

### option `mapContentTypeAs()`

For complex data transformation, allows to map some content types to others.
example, if you want to consider every `text/plain` content type to be parsed as Json, if you have a json media provider.

`@FormDataParam(value = "field", mapContentTypeAs = {@Map(to= MediaType.APPLICATION_JSON)})`

> Note: you can add a `from=` inside the `@Map` to restrict the mapping. By default, `from=*/*`

### option `filterContentType()`

 *To be documented*
 
## Customise request parsing

 *To be documented* but quicky:
 - implement your own `IRequestParser`
 - set an instance of your implementation in the `MultiPartConfig` object
 
## Customise each parts storing

 *To be documented* but quicky:
 - implement your own `IFormDataBodyPartProvider`
 - set an instance of your implementation in the `MultiPartConfig` object
 
  
 



