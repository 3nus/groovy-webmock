package gwebmock

import groovy.mock.interceptor.MockFor
import groovyx.net.http.AsyncHTTPBuilder

class httpResult {

    def response
    def done

    def httpResult(object) {
        response = object
        done = true
    }

    def get() { response}
}

class MockAsyncHTTPBuilder{

    def mockHTTP
    def requestPath
    def response
    def requestDelegate = [response: ['statusLine': ['protocol': 'HTTP/1.1','statusCode': 200, 'status': 'OK'], 'success':true], uri: [:]]

    def MockAsyncHTTPBuilder() {

    }

    def stub_api_get(Map args=[:]) {
        requestPath = args.path
        response = new httpResult(args.returns)
        mockHTTP = new MockFor(AsyncHTTPBuilder)

        mockHTTP.demand.get(1) {Map opts, Closure body ->
            response
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