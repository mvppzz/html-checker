
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

    // fixes html tags that dont match or are missing
    public void fixHTML() {
        Stack<HTMLTag> openTags = new Stack<HTMLTag>();
        Queue<HTMLTag> fixedTags = new LinkedList<HTMLTag>();

        // goes thru each tag
        while (!tags.isEmpty()) {
            HTMLTag current = tags.remove();

            // self closing tags dont need another tag
            if (current.isSelfClosing()) {
                fixedTags.add(current);
            }

            // adds opening tags to queue n stack
            else if (current.isOpening()) {
                fixedTags.add(current);
                openTags.push(current);
            }

            // checks if closing tags match
            else if (current.isClosing()) {
                while (!openTags.isEmpty() &&
                       !openTags.peek().matches(current)) {
                    fixedTags.add(openTags.pop().getMatching());
                }

                // adds closing tag if it matches
                if (!openTags.isEmpty()) {
                    openTags.pop();
                    fixedTags.add(current);
                }
                // skips extra closing tags
            }
        }

        // closes any tags still in stack
        while (!openTags.isEmpty()) {
            fixedTags.add(openTags.pop().getMatching());
        }

        // updates queue with the fixed tags
        tags = fixedTags;
    }
}
