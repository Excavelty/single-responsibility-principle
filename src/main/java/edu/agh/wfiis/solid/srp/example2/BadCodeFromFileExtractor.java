package edu.agh.wfiis.solid.srp.example2;

import java.io.File;

public class BadCodeFromFileExtractor extends BadCodeExtractor{
    private File file;

    public BadCodeFromFileExtractor(String path) {
        try {
            file = new File(path);
        }
        catch(Exception e) {
            System.out.println("Not found file");
        }
    }

    @Override
    public String extract()  {
        /* some magic happens here, irrelevant from this example perspective...*/
        return "bad code from file";
    }

}
