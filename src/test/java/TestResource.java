import com.archytasit.jersey.multipart.annotations.FormDataParam;
import com.archytasit.jersey.multipart.model.FileBodyPart;
import com.archytasit.jersey.multipart.model.IBodyPart;
import com.archytasit.jersey.multipart.model.StringBodyPart;
import org.junit.Assert;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Path("/upload")
public class TestResource {

      @POST
      @Path("/ws1")
      public void getHiGreeting(@FormDataParam("field") List<String> fields, @FormDataParam("date") LocalDate date, @FormDataParam("file")  List<IBodyPart> files) {
            fields.size();

      }

}
