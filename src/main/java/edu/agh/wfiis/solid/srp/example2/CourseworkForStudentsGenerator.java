package edu.agh.wfiis.solid.srp.example2;

public class CourseworkForStudentsGenerator {
    private String badCode;
    private BadCodeExtractor badCodeExtractor;

    public CourseworkForStudentsGenerator createCourseworkFromFile(String path) {
        CourseworkForStudentsGenerator courseworkForStudentsGenerator = new CourseworkForStudentsGenerator();
        courseworkForStudentsGenerator.badCodeExtractor = new BadCodeFromFileExtractor(path);
        return courseworkForStudentsGenerator;
    }

    public CourseworkForStudentsGenerator createCourseworkFromClassPath(String path) {
        CourseworkForStudentsGenerator courseworkForStudentsGenerator = new CourseworkForStudentsGenerator();
        courseworkForStudentsGenerator.badCodeExtractor = new BadCodeFromClassPathExtractor(path);
        return courseworkForStudentsGenerator;
    }

    private CourseworkForStudentsGenerator() {
    }

    public String generateCoursework() {
        /* some magic happens here, irrelevant from this example perspective...*/
        return "code refactored using SRP";
    }
}
