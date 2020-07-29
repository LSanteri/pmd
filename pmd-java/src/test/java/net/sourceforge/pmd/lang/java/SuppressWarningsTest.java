/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */

package net.sourceforge.pmd.lang.java;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import net.sourceforge.pmd.FooRule;
import net.sourceforge.pmd.Report;
import net.sourceforge.pmd.lang.LanguageRegistry;
import net.sourceforge.pmd.lang.LanguageVersion;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTCompilationUnit;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;
import net.sourceforge.pmd.testframework.RuleTst;

public class SuppressWarningsTest extends RuleTst {

    private final LanguageVersion java5 = LanguageRegistry.getLanguage(JavaLanguageModule.NAME).getVersion("1.5");

    private static class BarRule extends AbstractJavaRule {

        @Override
        public Object visit(ASTCompilationUnit cu, Object ctx) {
            // Convoluted rule to make sure the violation is reported for the
            // ASTCompilationUnit node
            for (ASTClassOrInterfaceDeclaration c : cu.findDescendantsOfType(ASTClassOrInterfaceDeclaration.class)) {
                if (c.getImage().equalsIgnoreCase("bar")) {
                    addViolation(ctx, cu);
                }
            }
            return super.visit(cu, ctx);
        }

        @Override
        public String getName() {
            return "NoBar";
        }
    }

    @Test
    public void testClassLevelSuppression() {
        Report rpt;
        rpt = runTestFromString(TEST1, new FooRule(), java5);
        assertEquals(0, rpt.getViolations().size());
        rpt = runTestFromString(TEST2, new FooRule(), java5);
        assertEquals(0, rpt.getViolations().size());
    }

    @Test
    public void testInheritedSuppression() {
        Report rpt = runTestFromString(TEST3, new FooRule(), java5);
        assertEquals(0, rpt.getViolations().size());
    }

    @Test
    public void testMethodLevelSuppression() {
        Report rpt;
        rpt = runTestFromString(TEST4, new FooRule(), java5);
        assertEquals(1, rpt.getViolations().size());
    }

    @Test
    public void testConstructorLevelSuppression() {
        Report rpt = runTestFromString(TEST5, new FooRule(), java5);
        assertEquals(0, rpt.getViolations().size());
    }

    @Test
    public void testFieldLevelSuppression() {
        Report rpt = runTestFromString(TEST6, new FooRule(), java5);
        assertEquals(1, rpt.getViolations().size());
    }

    @Test
    public void testParameterLevelSuppression() {
        Report rpt = runTestFromString(TEST7, new FooRule(), java5);
        assertEquals(1, rpt.getViolations().size());
    }

    @Test
    public void testLocalVariableLevelSuppression() {
        Report rpt = runTestFromString(TEST8, new FooRule(), java5);
        assertEquals(1, rpt.getViolations().size());
    }

    @Test
    public void testSpecificSuppression() {
        Report rpt = runTestFromString(TEST9, new FooRule(), java5);
        assertEquals(1, rpt.getViolations().size());
    }

    @Test
    public void testSpecificSuppressionValue1() {
        Report rpt = runTestFromString(TEST9_VALUE1, new FooRule(), java5);
        assertEquals(1, rpt.getViolations().size());
    }

    @Test
    public void testSpecificSuppressionValue2() {
        Report rpt = runTestFromString(TEST9_VALUE2, new FooRule(), java5);
        assertEquals(1, rpt.getViolations().size());
    }

    @Test
    public void testSpecificSuppressionValue3() {
        Report rpt = runTestFromString(TEST9_VALUE3, new FooRule(), java5);
        assertEquals(1, rpt.getViolations().size());
    }

    @Test
    public void testSpecificSuppressionMulitpleValues1() {
        Report rpt = runTestFromString(TEST9_MULTIPLE_VALUES_1, new FooRule(), java5);
        assertEquals(0, rpt.getViolations().size());
    }

    @Test
    public void testSpecificSuppressionMulitpleValues2() {
        Report rpt = runTestFromString(TEST9_MULTIPLE_VALUES_2, new FooRule(), java5);
        assertEquals(0, rpt.getViolations().size());
    }

    @Test
    public void testNoSuppressionBlank() {
        Report rpt = runTestFromString(TEST10, new FooRule(), java5);
        assertEquals(2, rpt.getViolations().size());
    }

    @Test
    public void testNoSuppressionSomethingElseS() {
        Report rpt = runTestFromString(TEST11, new FooRule(), java5);
        assertEquals(2, rpt.getViolations().size());
    }

    @Test
    public void testSuppressAll() {
        Report rpt = runTestFromString(TEST12, new FooRule(), java5);
        assertEquals(0, rpt.getViolations().size());
    }

    @Test
    public void testSpecificSuppressionAtTopLevel() {
        Report rpt = runTestFromString(TEST13, new BarRule(), java5);
        assertEquals(0, rpt.getViolations().size());
    }

    private static final String TEST1 = "@SuppressWarnings(\"PMD\")\npublic class Foo {}";

    private static final String TEST2 = "@SuppressWarnings(\"PMD\")\npublic class Foo {\n void bar() {\n  int foo;\n }\n}";

    private static final String TEST3 = "public class Baz {\n @SuppressWarnings(\"PMD\")\n public class Bar {\n  void bar() {\n   int foo;\n  }\n }\n}";

    private static final String TEST4 = "public class Foo {\n @SuppressWarnings(\"PMD\")\n void bar() {\n  int foo;\n }\n}";

    private static final String TEST5 = "public class Bar {\n @SuppressWarnings(\"PMD\")\n public Bar() {\n  int foo;\n }\n}";

    private static final String TEST6 = "public class Bar {\n @SuppressWarnings(\"PMD\")\n int foo;\n void bar() {\n  int foo;\n }\n}";

    private static final String TEST7 = "public class Bar {\n int foo;\n void bar(@SuppressWarnings(\"PMD\") int foo) {}\n}";

    private static final String TEST8 = "public class Bar {\n int foo;\n void bar() {\n  @SuppressWarnings(\"PMD\") int foo;\n }\n}";

    private static final String TEST9 = "public class Bar {\n int foo;\n void bar() {\n  @SuppressWarnings(\"PMD.NoFoo\") int foo;\n }\n}";

    private static final String TEST9_VALUE1 = "public class Bar {\n int foo;\n void bar() {\n  @SuppressWarnings(value = \"PMD.NoFoo\") int foo;\n }\n}";

    private static final String TEST9_VALUE2 = "public class Bar {\n int foo;\n void bar() {\n  @SuppressWarnings({\"PMD.NoFoo\"}) int foo;\n }\n}";

    private static final String TEST9_VALUE3 = "public class Bar {\n int foo;\n void bar() {\n  @SuppressWarnings(value = {\"PMD.NoFoo\"}) int foo;\n }\n}";

    private static final String TEST9_MULTIPLE_VALUES_1 = "@SuppressWarnings({\"PMD.NoFoo\", \"PMD.NoBar\"})\npublic class Bar {\n int foo;\n void bar() {\n  int foo;\n }\n}";

    private static final String TEST9_MULTIPLE_VALUES_2 = "@SuppressWarnings(value = {\"PMD.NoFoo\", \"PMD.NoBar\"})\npublic class Bar {\n int foo;\n void bar() {\n  int foo;\n }\n}";

    private static final String TEST10 = "public class Bar {\n int foo;\n void bar() {\n  @SuppressWarnings(\"\") int foo;\n }\n}";

    private static final String TEST11 = "public class Bar {\n int foo;\n void bar() {\n  @SuppressWarnings(\"SomethingElse\") int foo;\n }\n}";

    private static final String TEST12 = "public class Bar {\n @SuppressWarnings(\"all\") int foo;\n}";

    private static final String TEST13 = "@SuppressWarnings(\"PMD.NoBar\")\npublic class Bar {\n}";
}
