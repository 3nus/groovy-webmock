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
        mock.targetPath = testpath
        mock.requestedPath = testpath
        mock.assertPathWasRequested()
    }

    void test_assertPathWasRequested_throws_RequestedPathException() {
        mock.requestedPath = 'foo'
        mock.targetPath = 'bar'
        shouldFail(RequestedPathException) {
            mock.assertPathWasRequested()
        }
    }

    void test_assertPathWasRequested_via_stub() {
        def testpath = 'foo'
        mock.stubApiGet(path: testpath, returns: new JsonSlurper().parseText('{"hi":"there"}'))
        mock.mockHTTP.use {
            client.get(path: testpath, query: [foo: 'bar']) {resp, json -> json }
            mock.assertPathWasRequested()
        }
    }

    void test_sub_api_get() {
        def testpath = 'foo'
        def response = new JsonSlurper().parseText('{"hi":"there"}')
        mock.stubApiGet(path: testpath, returns: response)
        mock.mockHTTP.use {
            def resp = client.get(path: testpath, query: [foo: 'bar']) {resp, json -> json }
            assertEquals(response, resp.get())
        }
    }

}
