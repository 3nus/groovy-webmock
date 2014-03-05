package gwebmock

/**
 * Created by aterrell on 3/5/14.
 */
class AsyncHTTPResponse {

    def done
    def result

    public AsyncHTTPResponse(Map args) {
        result = args.result
        done = true
    }

    def get() { result }


}
