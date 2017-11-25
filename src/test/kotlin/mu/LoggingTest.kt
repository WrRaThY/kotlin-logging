package mu

import org.apache.log4j.Appender
import org.apache.log4j.Logger
import org.apache.log4j.PatternLayout
import org.apache.log4j.WriterAppender
import org.junit.After
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.StringWriter

class ClassWithLogging {
    companion object: KLogging()
    fun test() {
        LOG.info{"test ClassWithLogging"}
    }
    fun testThrowable() {
        val ex = Throwable()
        LOG.trace(ex){"test ChildClassWithLogging"}
    }
}
open class ClassHasLogging: KLoggable {
    override val LOG = logger()
    fun test() {
        LOG.info{"test ClassHasLogging"}
    }
}

/**
 * This class demonstrates the disadvantage of inheriting KLoggable in a class instead of companion object
 * the logger name will be of the sub-class
 * ie: it is not possible to log with parent class name this way
 */
class ClassInheritLogging: ClassHasLogging()

class ClassWithNamedLogging {
    companion object: Any(), KLoggable by NamedKLogging("mu.ClassWithNamedLogging")
    fun test() {
        LOG.info{"test ClassWithNamedLogging"}
    }
}
class CompanionHasLogging {
    companion object: Any(), KLoggable {
        override val LOG = logger()
    }
    fun test() {
        LOG.info{"test CompanionHasLogging"}
    }

    fun entry(inputObj: Any) {
        LOG.entry(inputObj)
    }

    fun exit(inputObj: Any): Any {
        return LOG.exit(inputObj)
    }
}
class ChildClassWithLogging {
    companion object: KLogging()
    fun test() {
        LOG.info{"test ChildClassWithLogging"}
    }
}

data class ClassWithIncorrectToString(val someVariable : String? = null){
    override fun toString(): String {
        return someVariable!!.toString()
    }
}

class LambdaRaisesError {
    companion object: KLogging()
    fun test() {
        val problematicClass = ClassWithIncorrectToString()

        LOG.info{" $problematicClass"}
    }
}

class LoggingTest {
    private lateinit var appenderWithWriter: AppenderWithWriter

    @Before
    fun setupAppender() {
        appenderWithWriter = AppenderWithWriter()
        Logger.getRootLogger().addAppender(appenderWithWriter.appender)
    }

    @After
    fun removeAppender() {
        Logger.getRootLogger().removeAppender(appenderWithWriter.appender)
    }

    @Test
    fun testMessages1() {
        ClassWithLogging().apply {
            test()
            testThrowable()
        }
        val lines = appenderWithWriter.writer.toString().trim().replace("\r", "\n").replace("\n\n", "\n").split("\n")
        Assert.assertEquals("INFO  mu.ClassWithLogging  - test ClassWithLogging", lines[0].trim())
        Assert.assertEquals("TRACE mu.ClassWithLogging  - test ChildClassWithLogging", lines[1].trim())
        Assert.assertEquals("java.lang.Throwable", lines[2].trim())
        Assert.assertTrue(lines[3].trim().startsWith("at mu.ClassWithLogging.testThrowable("))
    }
    @Test
    fun testMessages2() {
        ClassInheritLogging().test()
        Assert.assertEquals("INFO  mu.ClassInheritLogging  - test ClassHasLogging", appenderWithWriter.writer.toString().trim())
    }
    @Test
    fun testMessages3() {
        ChildClassWithLogging().test()
        Assert.assertEquals("INFO  mu.ChildClassWithLogging  - test ChildClassWithLogging", appenderWithWriter.writer.toString().trim())
    }
    @Test
    fun testMessages4() {
        ClassWithNamedLogging().test()
        Assert.assertEquals("INFO  mu.ClassWithNamedLogging  - test ClassWithNamedLogging", appenderWithWriter.writer.toString().trim())
    }
    @Test
    fun testMessages5() {
        ClassHasLogging().test()
        Assert.assertEquals("INFO  mu.ClassHasLogging  - test ClassHasLogging", appenderWithWriter.writer.toString().trim())
    }
    @Test
    fun testMessages6() {
        CompanionHasLogging().test()
        Assert.assertEquals("INFO  mu.CompanionHasLogging  - test CompanionHasLogging", appenderWithWriter.writer.toString().trim())
    }

    @Test
    fun testEntry() {
        CompanionHasLogging().entry("AAA")
        Assert.assertEquals("TRACE mu.CompanionHasLogging  - entry with (AAA)", appenderWithWriter.writer.toString().trim())
    }

    @Test
    fun testExit() {
        CompanionHasLogging().exit("BBB")
        Assert.assertEquals("TRACE mu.CompanionHasLogging  - exit with (BBB)", appenderWithWriter.writer.toString().trim())
    }

    @Test
    fun shouldNotFailForFailingLambdas(){
        LambdaRaisesError().test()
        Assert.assertEquals("INFO  mu.LambdaRaisesError  - Log message invocation failed: kotlin.KotlinNullPointerException", appenderWithWriter.writer.toString().trim())
    }
    @Test
    fun `check underlyingLogger property`() {
        ClassHasLogging().LOG.underlyingLogger
        Assert.assertTrue(ClassHasLogging().LOG.underlyingLogger is org.slf4j.Logger)
    }
}
class LoggingNameTest {
    @Test
    fun testNames() {
        assertEquals("mu.ClassWithLogging", ClassWithLogging.LOG.name)
        assertEquals("mu.ClassInheritLogging", ClassInheritLogging().LOG.name)
        assertEquals("mu.ChildClassWithLogging", ChildClassWithLogging.LOG.name)
        assertEquals("mu.ClassWithNamedLogging", ClassWithNamedLogging.LOG.name)
        assertEquals("mu.ClassHasLogging", ClassHasLogging().LOG.name)
        assertEquals("mu.CompanionHasLogging", CompanionHasLogging.LOG.name)
    }
}
data class AppenderWithWriter(val writer: StringWriter = StringWriter(), val appender: Appender =  WriterAppender(PatternLayout("%-5p %c %x - %m%n"), writer)) {

}
