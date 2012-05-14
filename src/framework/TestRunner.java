package framework;

import framework.interfaces.*;
import framework.interfaces.AcceptanceInterface;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import javax.naming.OperationNotSupportedException;
import java.lang.reflect.Constructor;
import java.util.Set;

/**
 * Class to run several tests, and handle the results
 *
 * @author Matthew Moss (matthew.moss)
 * @author Lasath Fernando (lasath.fernando)
 * @author Benjamin James Wright (ben.wright)
 * @author Damon Stacey (damon.stacey)
 * @author Alexis Shaw (alexis.shaw)
 */
public class TestRunner {

    AcceptanceInterface testee;
    int[] numTestsPassed;
    int[] numTestFailed;
    int[] numNotImplemented;
    int[] numInvalidTests;


    private static Test[] getUnverifiedTests() {
        return getTestsInPackage("tests.unverified");
    }

    private static Test[] getBorderlineTests() {
        return getTestsInPackage("tests.borderline");
    }

    private static Test[] getVerifiedTests() {
        return getTestsInPackage("tests.verified");
    }

    private static Test[] getTestsInPackage(String packageName){
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .filterInputsBy(new FilterBuilder.Include(FilterBuilder.prefix(packageName)))
                .setUrls(ClasspathHelper.forJavaClassPath())
                .setScanners(new SubTypesScanner()));
        Set<Class<? extends Test>> testClasses = reflections.getSubTypesOf(Test.class);

        Test[] returnValue = new Test[testClasses.size()];
        int NumClassesWithEmptyConstructor = 0;
        for(Class<? extends Test> testClass : testClasses){
             try{
                Constructor<? extends Test> testConstructor = testClass.getConstructor();
                returnValue[NumClassesWithEmptyConstructor] = testConstructor.newInstance();
                NumClassesWithEmptyConstructor++;
            } catch (Exception e){}
        }
        assert(NumClassesWithEmptyConstructor == testClasses.size());

        return returnValue;
    }
    private static AcceptanceInterface[] getAcceptanceInterfacesInPackage(String packageName){
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forJavaClassPath())
                .setScanners(new SubTypesScanner()));
        Set<Class<? extends AcceptanceInterface>> acceptanceInterfaceClasses =
                    reflections.getSubTypesOf(AcceptanceInterface.class);
        for(Class<? extends AcceptanceInterface> c : acceptanceInterfaceClasses){
            System.out.println(c.getCanonicalName());
        }
        AcceptanceInterface[] returnValue = new AcceptanceInterface[acceptanceInterfaceClasses.size()];
        int noClassesWithEmptyConstructor = 0;
        for(Class<? extends AcceptanceInterface> acceptanceInterfaceClass:acceptanceInterfaceClasses){
            try{
                Constructor<? extends AcceptanceInterface> acceptanceInterfaceConstructor =
                        acceptanceInterfaceClass.getConstructor();
                returnValue[noClassesWithEmptyConstructor] = acceptanceInterfaceConstructor.newInstance();
                noClassesWithEmptyConstructor++;
            } catch (Exception e){
               e.printStackTrace();
            }
        }
        assert(noClassesWithEmptyConstructor == acceptanceInterfaceClasses.size());
        return returnValue;
    }

    public static void main (String[] args) {
         TestRunner runner = new TestRunner();
         runner.testGame();
    }

    public void testGame() {
        System.out.println("Roma acceptance tests starting...");

        boolean assertionsEnabled = false;
        try {
            assert (false);
        } catch (AssertionError e) {
            assertionsEnabled = true;
        }
        AcceptanceInterface[] acceptanceInterfaces = getAcceptanceInterfacesInPackage("");
        int interfaceTestNumber = 0;
        numTestsPassed = new int[acceptanceInterfaces.length];
        numTestFailed = new int[acceptanceInterfaces.length];
        numNotImplemented = new int[acceptanceInterfaces.length];
        numInvalidTests = new int[acceptanceInterfaces.length];

        if (!assertionsEnabled) {
            System.out.println("Please enable assertions, run with java -ea");
            return;
        } else {
            for (AcceptanceInterface acceptanceInterface: acceptanceInterfaces) {
               System.out.println("Now testing: " + acceptanceInterface.getClass()  + ".");
               runTests(getVerifiedTests(), acceptanceInterface, interfaceTestNumber);
               runTests(getBorderlineTests(), acceptanceInterface, interfaceTestNumber);
               runTests(getUnverifiedTests(), acceptanceInterface, interfaceTestNumber);
               interfaceTestNumber++;
            }
        }
        interfaceTestNumber = 0;
        for (AcceptanceInterface acceptanceInterface: acceptanceInterfaces) {
            if ((numNotImplemented[interfaceTestNumber] > 0)) {
                System.out.println("Not fully implemented: " +  acceptanceInterface.getClass());
            } else if ((numTestFailed[interfaceTestNumber] > 0)) {
                System.out.println("Needs work: " +  acceptanceInterface.getClass());
            } else {
                System.out.println("Accepted: " +  acceptanceInterface.getClass() + "is awesome!!!");
            }
            interfaceTestNumber++;
        }
    }

    private void runTests(Test[] tests, AcceptanceInterface acceptanceInterface, int interfaceTestNumber) {
        if (acceptanceInterface == null){
            System.out.println("Cannot find your acceptanceInterface,");
            System.out.println("please place your jar containg you accpetanceInterface");
            System.out.println("implementation in the testee folder.");
        }
        for (Test current : tests) {
            try {
                System.out.println("   " + current.getClass().toString().split("class tests.")[1] + ":");
                System.out.println("      " + current.getShortDescription());
                GameState state = acceptanceInterface.getInitialState();
                MoveMaker mover = acceptanceInterface.getMover(state);
                SanityChecker checkedMover = new SanityChecker(mover, state, current.out);
                current.run(state,mover);

                numTestsPassed[interfaceTestNumber]++;
                System.out.println("      Test passed");

            } catch (UnsupportedOperationException ex) {
                numNotImplemented[interfaceTestNumber]++;
                System.out.println("      Feature not implemented yet. Skipping test...");
            } catch (IllegalArgumentException ex) {
                numInvalidTests[interfaceTestNumber]++;
                System.out.println(current.getOutputSteam());
                Logger.getLogger(TestRunner.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("      Error in test. Please report this to your "
                        + "representative.");

            } catch (Exception ex) {
                numTestFailed[interfaceTestNumber]++;
                System.out.println(current.getOutputSteam());
                Logger.getLogger(TestRunner.class.getName()).log(Level.SEVERE, null, ex);

                System.out.println("      Test Failed");

            } catch (AssertionError ex) {
                numTestFailed[interfaceTestNumber]++;
                System.out.print(current.getOutputSteam());
                ex.printStackTrace(System.out);
                System.out.println("      Test Failed:");
            }
        }
    }

}
