
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

    // turns all the html tags into one string
    public String toString() {
        StringBuilder result = new StringBuilder();
        int size = tags.size();

        for (int i = 0; i < size; i++) {
            HTMLTag tag = tags.remove();
            result.append(tag.toString().trim());
            tags.add(tag);
        }

        return result.toString();
    }
}
