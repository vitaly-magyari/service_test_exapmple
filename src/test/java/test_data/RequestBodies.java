package test_data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class RequestBodies {
    // register name of body from json here
//    public Body valid_sep_semi;
//    public Body valid_no_sep;
//    public Body valid_sep_comma;
//    public Body valid_floating_point_no_sep;
//    public Body valid_letter_separator;
//
//    public Body invalid_zero_sides;
//
//    public String getDefault() {
//        return  valid_no_sep.asJson();
//    }
//
//    // just in case volatile & synchronized below, didn't try parallelization though
//    private RequestBodies() {
//    }
//    private volatile static RequestBodies instance = null;
//
//
//    public synchronized static RequestBodies get() {
//        if (instance == null) {
//            Gson gson = new Gson();
//
//            Type listOfStrings = new TypeToken<ArrayList<String>>() {
//            }.getType();
//
//            InputStream dataFile = RequestBodies.class.getClassLoader().getResourceAsStream("request_bodies.json");
//            instance = gson.fromJson(new InputStreamReader(dataFile), RequestBodies.class);
//        }
//        return instance;
//    }
//
//    public class Body {
//        String separator;
//        String input;
//
//        public String asJson() {
//            return new Gson().toJson(this);
//        }
//
//        @Override
//        public String toString() {
//            return this.asJson();
//        }
//    }
}
