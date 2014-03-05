package gwebmock

class RequestedPathExceptionTest extends GroovyTestCase {

    void setUp() {
        super.setUp()

    }

    void tearDown() {

    }

    void testRequestedPathException() {
        def exc = new RequestedPathException("foo")
        assertEquals("foo", exc.message)
    }

}
