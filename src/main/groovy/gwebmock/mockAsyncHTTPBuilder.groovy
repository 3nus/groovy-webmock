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
    //def requestDelegate = [response: ['statusLine': ['protocol': 'HTTP/1.1','statusCode': 200, 'status': 'OK'], 'success':true], uri: [:]]


    // ---------------------------
    // stub for a simple AsyncHTTPBuilder().get() request
    //      accepts: Map [path: String, returns: String, status: Integer]
    //      returns: sapic.resource.Bibs object
    def stubApiGet(Map args=[:]) {
        targetPath = args.path
        response = new AsyncHTTPResponse(result: args.returns)

        mockHTTP = new StubFor(AsyncHTTPBuilder)

        mockHTTP.demand.get() {Map opts, Closure closure ->
            //closure.delegate = requestDelegate
            //closure.setResolveStrategy(Closure.DELEGATE_FIRST)
            //closure.call()
            requestedPath = opts.path
            response
        }
    }

    // ---------------------------
    // throw exception if we can't match target and requested paths
    def assertPathWasRequested() throws RequestedPathException {
        if (targetPath != requestedPath) {
            throw new RequestedPathException("Expected request to path '${targetPath}', was instead '${requestedPath}'")
        }
    }

    // ---------------------------
    // throw exception if we can't match target and requested paths
    // if using a Mixin, the requestedPath class property will not be accessible (why is targetPath ok?)
    // allow the requestedPath to be passed in for evaluation
    def assertPathWasRequested(String requestedPath) throws RequestedPathException {
        if (targetPath != requestedPath) {
            throw new RequestedPathException("Expected request to path '${targetPath}', was instead '${requestedPath}'")
        }
    }
}