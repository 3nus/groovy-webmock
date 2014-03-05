package gwebmock
import groovy.mock.interceptor.StubFor
import groovyx.net.http.AsyncHTTPBuilder

class RequestedPathException extends Exception {
    public RequestedPathException(String msg) {
        super(msg);
    }
}

class MockAsyncHTTPBuilder{

    def mockHTTP
    def requestedPath
    def response
    def targetPath
    def requestDelegate = [response: ['statusLine': ['protocol': 'HTTP/1.1','statusCode': 200, 'status': 'OK'], 'success':true], uri: [:]]


    // ---------------------------
    // stub for a simple AsyncHTTPBuilder().get() request
    //      accepts: Map [path: String, returns: String, status: Integer]
    //      returns: sapic.resource.Bibs object
    def stubApiGet(Map args=[:]) {
        targetPath = args.path

        mockHTTP = new StubFor(AsyncHTTPBuilder)

        mockHTTP.demand.get() {Map opts, Closure closure ->
            closure.delegate = requestDelegate
            closure.setResolveStrategy(Closure.DELEGATE_FIRST)
            //closure.call()
            this.requestedPath = opts.path
            response
        }
    }

    def assertPathWasRequested(String targetPath) throws RequestedPathException {
        if (targetPath != requestedPath) {
            throw new RequestedPathException("Expected request to path '${targetPath}', was instead '${requestedPath}'")
        }
    }

    /*
    def stub_api_request(path) {
        requestPath = path
        mockHTTP = new MockFor(AsyncHTTPBuilder)

        mockHTTP.demand.request(1) {Method method, Closure body ->

            body.delegate = requestDelegate
            body.setResolveStrategy(Closure.DELEGATE_FIRST)
            body.call()
            requestDelegate.response.success(requestDelegate.response, ['success': true]) // or failure depending on what's being tested

            reqParams << [method: m, path: b.uri.path, type: c, body: b.body]
        }
    }
    */
}