package tests;

import org.junit.Test;
import ril.Object;
import ril.implementation.SubjectPropertyImplementation;

import static org.junit.Assert.assertTrue;

public class SubjectPropertyImplementationTest {

    @Test
    public void grabObjects() {
        SubjectPropertyImplementation sp = new SubjectPropertyImplementation("Hawking","Awards");
        String filePath = "src/main/java/resource/Awards.txt";
        sp.grabObjects(filePath);
        assertTrue(sp.getObjects() != null);
        sp.grabNum_objs();
        assertTrue(sp.getObjects().get(0).getNum_object() != -1);
        sp.grabNum_subj_objs();
        assertTrue(sp.getObjects().get(0).getNum_subj_object() != -1);
        sp.calcRank_coeff();
        assertTrue(sp.getObjects().get(0).getRank_coeff() != -1);
        sp.sortObject(Object.Parameter.RANK_COEFF);
        assertTrue(sp.getObjects().get(0).getRank_coeff() >= sp.getObjects().get(1).getRank_coeff());
        assertTrue(sp.sort_order == Object.Parameter.RANK_COEFF);
        sp.printResults(10);
    }

}