import com.github.balintrudas.commentparser.marker.CommentElement;

import java.io.File;
import java.util.LinkedHashSet;

public class CommentParserTestUtil {

    public static String getEnvironmentPath(){
        String dirPath = System.getProperty("user.dir") + "/src/test/java/environment";
        return dirPath.replace("/", File.separator);
    }

    public static Boolean hasComment(LinkedHashSet<CommentElement> commentElements, String commentValue){
        return commentElements.stream().anyMatch(commentElement -> commentElement.getValue().equals(commentValue));
    }

}
