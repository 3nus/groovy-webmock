package gwebmock

import groovy.json.JsonSlurper
import groovyx.net.http.AsyncHTTPBuilder

import static groovyx.net.http.ContentType.JSON

class MockAsyncHTTPBuilderTest extends GroovyTestCase {

    def client
    def mock

    void setUp() {
        super.setUp()
        client = new AsyncHTTPBuilder(poolSize: 20, uri: 'http://localhost', contentType: JSON)
        mock = new MockAsyncHTTPBuilder()
    }

    void tearDown() {

    }

    void test_assertPathWasRequested_assert_match() {
        def testpath = 'foo'
        mock.requestedPath = testpath
        mock.assertPathWasRequested('foo')
    }

    void test_assertPathWasRequested_throws_RequestedPathException() {
        def testpath = 'foo'
        mock.requestedPath = testpath
        shouldFail(RequestedPathException) {
            mock.assertPathWasRequested('bar')
        }
    }

    void test_assertPathWasRequested_via_stub() {
        def testpath = 'foo'
        mock.stubApiGet(path: testpath, returns: new JsonSlurper().parseText('{"hi":"there"}'))
        mock.mockHTTP.use {
            def resp = client.get(path: testpath, query: [foo: 'bar']) {resp, json -> json }
            mock.assertPathWasRequested(testpath)
        }
    }

}
