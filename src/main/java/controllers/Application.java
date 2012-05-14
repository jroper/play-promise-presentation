package controllers;

import model.SlicedBread;
import play.libs.Akka;
import play.libs.F;
import play.libs.F.*;
import play.libs.Json;
import play.libs.WS;
import play.mvc.Controller;
import play.mvc.Result;
import async.Baker;

import java.util.concurrent.Callable;

public class Application extends Controller {
    private static Baker baker = new Baker();

    public static Result action() {

        // Order the sliced bread
        Promise<SlicedBread> slicedBreadPromise = baker.orderSlicedBread();

        // Map it to a result
        Promise<Result> resultPromise = slicedBreadPromise.map(new F.Function<SlicedBread, Result>() {
            public Result apply(SlicedBread slicedBread) throws Throwable {
                return ok(Json.toJson(slicedBread));
            }
        });

        // Return an asynchronous result
        return async(resultPromise);
    }

    public static Result wsAction() {

        // Make a REST call
        Promise<WS.Response> response = WS.url("http://baker.com/slicedBread").get();

        // Map it to a result and return as an asynchronous result
        return async(response.map(new Function<WS.Response, Result>() {
            public Result apply(WS.Response response) throws Throwable {
                return ok(response.asJson());
            }
        }));
    }

    public static Promise<SlicedBread> orderSlicedBread() {

        // Make a rest call
        Promise<WS.Response> response = WS.url("http://baker.com/slicedBread").get();

        // Convert the result to sliced bread
        Promise<SlicedBread> result = response.map(new Function<WS.Response, SlicedBread>() {
            @Override
            public SlicedBread apply(WS.Response response) throws Throwable {
                return Json.fromJson(response.asJson(), SlicedBread.class);
            }
        });

        return result;
    }

    public static Result akkaAction() {

        // Schedule something that will take a while
        Promise<Long> sum = Akka.future(new Callable<Long>() {
            public Long call() throws Exception {
                long total = 0;
                for (long i = 0; i < 1000000000000L; i++) {
                    total = total + i;
                }
                return total;
            }
        });

        // Map it to JSON
        return async(sum.map(new Function<Long, Result>() {
            @Override
            public Result apply(Long total) throws Throwable {
                return ok(Json.toJson(total));
            }
        }));
    }
}
