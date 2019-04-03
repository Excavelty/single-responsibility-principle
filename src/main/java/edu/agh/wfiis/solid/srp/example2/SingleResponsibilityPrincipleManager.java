package edu.agh.wfiis.solid.srp.example2;

import java.io.File;

public class SRPConverter {
    private String description;
    
    private String codeToBeRefactored;

    private SRPConverter(String description){
        this.description = description;
    }

    public static SRPConverter withDefaultDescription(){
        return new SRPConverter("SRP description");
    }

    public static SRPConverter withProvidedDescription(String description){
        return new SRPConverter(description);
    }

    public String getDescription() {
        return this.description;
    }

    private String refactorCode(){
        /* some magic happens here, irrelevant from this example perspective...*/
        return "code refactored using SRP";
    }

    public String refactorCodeFromFile(File file) {
        try {
            this.codeToBeRefactored = SRPCodeExtractor.extractFromFile(file);
        }
        catch (Exception e){
            /* some magic happens here */
        }
        return refactorCode();        
    }

    public String refactorCodeFromClassPath(ClassLoader classLoader) {
        try {
            this.codeToBeRefactored = SRPCodeExtractor.extractFromClassPath(classLoader);
        }
        catch (Exception e){
            /* some magic happens here */
        }
        return refactorCode();        
    }
}

class SRPCodeExtractor {

    public static string extractFromFile(File file) throws Exception {
        /* some magic happens here, irrelevant from this example perspective...*/
        return "bad code from file"; 
    }

    public static string extractFromClassPath(Class classLoader) throws Exception {
        /* some magic happens here, irrelevant from this example perspective...*/
        return "bad code from class loader";
    }
}


class CourseworkGenerator() {

    public String generateCourseworkForStudents() {
        /* some magic happens here, irrelevant from this example perspective...*/
        return "very difficult coursework";
    }
}
