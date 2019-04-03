package edu.agh.wfiis.solid.srp.example2;

import java.io.File;
import java.net.URLClassLoader;
import java.net.URL;
import java.security.SecureClassLoader;

public class BadCodeFromClassPathExtractor extends BadCodeExtractor{
    private ClassLoader classLoader;

    public BadCodeFromClassPathExtractor(String path) {
        try {
            classLoader = new URLClassLoader(new URL[] {new URL(path)});
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
