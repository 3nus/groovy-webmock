groovy-webmock
==============

This groovy mock HTTP library for JUnit - gwebmock - is a partial implementation of HTTPBuilder mocking using JUnit's StubFor. Gwebmock implementations are intended to support unit testing of HTTP requests, simplifying the effort of testing HTTP requests by stubbing out responses and providing methods for test conveniences.

Modeled somewhat like Ruby's webmock library, gwebmock classes an be used as Mixins, or as class instances.

Mocked HTTPBuilder interfaces
-----------------------------

-   AsyncHTTPBuilder().get()
-   next? (this is p.o.c., be patient)

Usage
=====

Mixin example
-------------

The @Mixin annotation simplifies usage, but requires we specify the path when calling assertPathWasRequested().

```groovy
import gwebmock.MockAsyncHTTPBuilder

@Mixin(MockAsyncHTTPBuilder)
class ItemsTest extends GroovyTestCase {

    def client = new AsyncHTTPBuilder(poolSize: 2, uri: 'http://localhost', contentType: JSON)
    def expectedResponse = new JsonSlurper().parseText('{"hi":"there"}')
    def testPath = 'foo'
    
    void test_sub_api_get_response_is_what_we_waid_it_should_be() {
        stubApiGet(path: testPath, returns: expectedResponse)
        mockHTTP.use {
            def resp = client.get(path: testpath, query: [foo: 'bar']) {resp, json -> json }
            assertEquals(expectedResponse, resp.get())
            assertPathWasRequested(testPath)
        }
    }
```

Class instance example
----------------------

Using a class instance simplifies calling assertPathWasRequested(), but is generally more cumbersome.

```groovy
import gwebmock.MockAsyncHTTPBuilder

class ItemsTest extends GroovyTestCase {

    def client = new AsyncHTTPBuilder(poolSize: 2, uri: 'http://localhost', contentType: JSON)
    def expectedResponse = new JsonSlurper().parseText('{"hi":"there"}')
    def testPath = 'foo'
    def mock = new MockAsyncHTTPBuilder()
    
    void test_sub_api_get_response_is_what_we_waid_it_should_be() {
        mock.stubApiGet(path: testPath, returns: expectedResponse)
        mock.mockHTTP.use {
            def resp = client.get(path: testpath, query: [foo: 'bar']) {resp, json -> json }
            assertEquals(expectedResponse, resp.get())
            assertPathWasRequested()
        }
    }

```



