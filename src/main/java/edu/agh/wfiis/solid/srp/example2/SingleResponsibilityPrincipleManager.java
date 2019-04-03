package edu.agh.wfiis.solid.srp.example2;
import java.io.File;

public class StudentsCourseworkGenerator{

    public String provideDescription() {
        return "StudentsCoursework description";
    }

    public String generateCourseworkForStudents() {
        /* some magic happens here, irrelevant from this example perspective...*/
        return "very difficult coursework";
    }
}

class CodeReader{
    
    public String readCodeFromFile(File file) {
        /* some magic happens here, irrelevant from this example perspective...*/
        return "code from file";
    }

    public String readCodeFromClassPath(ClassLoader classLoader) {
        /* some magic happens here, irrelevant from this example perspective...*/
        return "code from class loader";
    }  
}

class CodeRefactor{

    public String extractBadCode(String allCode) {
        /* some magic happens here, irrelevant from this example perspective...*/
        return "bad code extracted";
    }
    public String refactorCode(String badCode) {
        /* some magic happens here, irrelevant from this example perspective...*/
        return "code refactored using SRP";
    }

}
