/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author adrian
 */
public class HTableDescriptorTest {

    public HTableDescriptorTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }


     @Test
     public void testSetName() {

         String firstname = "first-name";
         String secondname = "second-name";

         HTableDescriptor t = new HTableDescriptor(Bytes.toBytes(firstname));
         t.setName(Bytes.toBytes(secondname));

         Assert.assertEquals(secondname, t.getNameAsString());
     }

}