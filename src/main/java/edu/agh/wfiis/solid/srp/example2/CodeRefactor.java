package edu.agh.wfiis.solid.srp.example2;

public class CodeRefactor {
    private DescriptionProvider descriptionProvider;
    private String badCode;
    private BadCodeExtractor badCodeExtractor;

    public CodeRefactor createCodeRefactorFromFile(String path) {
        CodeRefactor codeRefactor = new CodeRefactor();
        codeRefactor.badCodeExtractor = new BadCodeFromFileExtractor(path);
        return codeRefactor;
    }

    public CodeRefactor createCodeRefactorFromClassPath(String path) {
        CodeRefactor codeRefactor = new CodeRefactor();
        codeRefactor.badCodeExtractor = new BadCodeFromClassPathExtractor(path);
        return codeRefactor;
    }

    private CodeRefactor() {
        descriptionProvider = new DescriptionProvider("SRP");
    }


    public String refactorCode() {
        /* some magic happens here, irrelevant from this example perspective...*/
        return "code refactored using SRP";
    }
}
