package controllers;

import model.SlicedBread;
import play.libs.F.*;
import play.libs.Json;
import play.libs.WS;
import play.mvc.Controller;
import play.mvc.Result;
import promise.Baker;

public class Application extends Controller {
    private static Baker baker;

    public static Promise<Result> action() {

        // Order the sliced bread
        Promise<SlicedBread> slicedBreadPromise = baker.orderSlicedBread();

        // Map it to a result
        Promise<Result> resultPromise = slicedBreadPromise.map(
                slicedBread -> (Result) ok(Json.toJson(slicedBread))
        );

        // Return an asynchronous result
        return resultPromise;
    }

    public static Promise<Result> wsAction() {

        // Make a REST call
        Promise<WS.Response> response = WS.url("http://baker.com/slicedBread").get();

        // Map it to a result and return as an asynchronous result
        return response.map(resp -> (Result) ok(resp.asJson()));
    }

    public static Promise<SlicedBread> orderSlicedBread() {

        // Make a rest call
        Promise<WS.Response> response = WS.url("http://baker.com/slicedBread").get();

        // Convert the result to sliced bread
        Promise<SlicedBread> result = response.map(
                resp -> Json.fromJson(resp.asJson(), SlicedBread.class)
        );

        return result;
    }

    public static Promise<Result> backgroundAction() {

        // Schedule something that will take a while
        Promise<Long> sum = Promise.promise(() -> {
            long total = 0;
            for (long i = 0; i < 1000000000000L; i++) {
                total = total + i;
            }
            return total;
        });

        // Map it to JSON
        return sum.map(total -> (Result) ok(Json.toJson(total)));
    }
}
