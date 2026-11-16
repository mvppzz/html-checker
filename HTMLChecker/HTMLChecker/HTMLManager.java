
import java.util.*;

public class HTMLManager {
    private Queue<HTMLTag> tags;

    // makes html manager n copy of the given queue
    public HTMLManager(Queue<HTMLTag> html) {
        if (html == null) {
            throw new IllegalArgumentException();
        }

        tags = new LinkedList<HTMLTag>(html);
    }

    // gets the tags from queue
    public Queue<HTMLTag> getTags() {
        return tags;
    }
}
