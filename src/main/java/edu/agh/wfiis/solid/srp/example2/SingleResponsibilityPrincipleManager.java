package edu.agh.wfiis.solid.srp.example2;
import java.io.File;

interface Describable {
    String provideDescription();
}

class CodeCleaner implements Describable {

    private String refactoredCode;

    public String provideDescription() {
        return "SRP description";
    }

    public void refactorCode(String badCode) {
        /* some magic happens here, irrelevant from this example perspective...*/
        refactoredCode = "code refactored using SRP";
    }

    public String getRefactoredCode() {
        return refactoredCode;
    }
}

class CourseworkGenerator {

    private String coursework;

    public void generateCourseworkForStudents() {
        /* some magic happens here, irrelevant from this example perspective...*/
        coursework = "very difficult coursework";
    }

    public String getCoursework() {
        return coursework;
    }
}

class BadCodeExtractor {

    private String extractedBadCode;

    public void extractBadCode(File file) {
        /* some magic happens here, irrelevant from this example perspective...*/
        extractedBadCode = "bad code from file";
    }

    public void extractBadCode(ClassLoader classLoader) {
        /* some magic happens here, irrelevant from this example perspective...*/
        extractedBadCode = "bad code from class loader";
    }

    public String getExtractedBadCode() {
        return extractedBadCode;
    }
}