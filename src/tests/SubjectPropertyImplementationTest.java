package tests;

import org.junit.Test;
import ril.Object;
import ril.implementation.SubjectPropertyImplementation;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class SubjectPropertyImplementationTest {

    @Test
    public void grabObjects() {
        SubjectPropertyImplementation sp = new SubjectPropertyImplementation("Hawking","Awards");
        String filePath = "src/resource/Awards.txt";
        ArrayList<Object> objects = sp.grabObjects(filePath);
        assertTrue(objects != null);
    }
}