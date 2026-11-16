// CSE 143, Homework: HTML Validator
//
// Instructor-provided code.
// This program tests your HTML manager object


import java.util.*;
import java.io.*;

public class HTMLChecker {
    public static void main(String[] args) {
        String failedTests = "";

        File folder = new File("tests/");
        File[] listOfFiles = folder.listFiles();

        for (File file: listOfFiles) {
            if (file.isFile()) {
                String actualResult = check(folder.getName() + "/" + file.getName());
                String expectedResult = getManager("expected_output/" + file.getName().replace("html", "txt")).toString();
                if (actualResult.equals(expectedResult))
                    System.out.println("----> Result matches Expected Output!");
                else {
                    System.out.println("----> Something isn't working right! \nFixed HTML should be: \n" + expectedResult);
                    failedTests += file.getName() + " ";
                }
                System.out.println();
            }
        }
        
        System.out.println("===============================");
        if(failedTests.equals(""))
            System.out.println("        All tests passed!");
        else
            System.out.println("Failed tests: " + failedTests);
        System.out.println("===============================");
    }

    public static String check(String file) {
        System.out.println("===============================");
        System.out.println("Processing " + file + "...");
        System.out.println("===============================");

        HTMLManager manager = getManager(file);
        //System.out.println("Loaded tags: " + manager.getTags());
        System.out.println("HTML: " + manager);
        System.out.println("Checking HTML for errors...");
        manager.fixHTML();
        System.out.println("HTML after fix: " + manager);
        return manager.toString();
    }

    public static HTMLParser getParser(String path) {
        HTMLParser result = null;
        try {
            result = new HTMLParser(new File(path));
        } catch (Exception e) {
            System.out.println("Something went wrong.");
            e.printStackTrace();
        }
        return result;
    }

    public static HTMLManager getManager(String file) {
        HTMLManager m = null;
        HTMLParser parser = getParser(file);
        if (parser != null) {
            Queue < HTMLTag > tags = parser.parse();
            m = new HTMLManager(tags);
        } else {
            System.err.println("Couldn't resolve input.  Try again!");
        }
        return m;
    }
}


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


/*
# PROGRAM OUTPUT

===============================
Processing tests/test1.html...
===============================
HTML: <b><i><br /></b></i>
Checking HTML for errors...
HTML after fix: <b><i><br /></i></b>
----> Result matches Expected Output!

===============================
Processing tests/test2.html...
===============================
HTML: <a><a><a></a>
Checking HTML for errors...
HTML after fix: <a><a><a></a></a></a>
----> Result matches Expected Output!

===============================
Processing tests/test3.html...
===============================
HTML: <br /></p></p>
Checking HTML for errors...
HTML after fix: <br />
----> Result matches Expected Output!

===============================
Processing tests/test4.html...
===============================
HTML: <div><div><ul><li></li><li></li><li></li></ul></div>
Checking HTML for errors...
HTML after fix: <div><div><ul><li></li><li></li><li></li></ul></div></div>
----> Result matches Expected Output!

===============================
Processing tests/test5.html...
===============================
HTML: <div><h1></h1><div><img /><p><br /><br /><br /></div></div></table>
Checking HTML for errors...
HTML after fix: <div><h1></h1><div><img /><p><br /><br /><br /></p></div></div>
----> Result matches Expected Output!

===============================
        All tests passed!
===============================

*/
