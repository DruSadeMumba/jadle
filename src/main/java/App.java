import com.google.gson.Gson;
import models.Foodtype;
import models.Restaurant;
import models.Review;
import models.dao.SqlFoodtypeDao;
import models.dao.SqlRestaurantDao;
import models.dao.SqlReviewDao;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import exceptions.ApiException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;

public class App {
    /*static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567;
    }*/
    public static void main(String[] args) {
        /*port(getHerokuAssignedPort());
        staticFileLocation("/public");*/

        SqlFoodtypeDao foodtypeDao;
        SqlRestaurantDao restaurantDao;
        SqlReviewDao reviewDao;
        Connection conn;
        Gson gson = new Gson();

        String connectionString = "jdbc:postgresql://localhost:5432/jadle";
        Sql2o sql2o = new Sql2o(connectionString, null, null);

        restaurantDao = new SqlRestaurantDao(sql2o);
        foodtypeDao = new SqlFoodtypeDao(sql2o);
        reviewDao = new SqlReviewDao(sql2o);
        conn = sql2o.open();

        Map<String, Object> model = new HashMap<>();

                                    /*RESTAURANTS*/
        get("/restaurants", "application/json", (req, res) -> {//1.0 view all restaurants
            System.out.println(restaurantDao.getAll());
            if(restaurantDao.getAll().size() > 0){
                return gson.toJson(restaurantDao.getAll());
            }
            else {
                return "{\"message\":\"I'm sorry, but no restaurants are currently listed in the database.\"}";
            }
        });

        get("/restaurants/:id", "application/json", (req, res) -> { //1.1 view individual restaurant
            int restaurantId = Integer.parseInt(req.params("id"));
            return gson.toJson(restaurantDao.findById(restaurantId));
        });

        post("/restaurants/new", "application/json", (req, res) -> {//1.2 add new restaurant
            Restaurant restaurant = gson.fromJson(req.body(), Restaurant.class);
            restaurantDao.add(restaurant);
            res.status(201);
            return gson.toJson(restaurant);
        });

        //restaurant-reviews relationship
        get("/restaurants/:id/reviews", "application/json", (req, res) -> {//1.3 view restaurant reviews
            int restaurantId = Integer.parseInt(req.params("id"));
            Restaurant restaurantToFind = restaurantDao.findById(restaurantId);
            List<Review> allReviews;
            if (restaurantToFind == null){
                throw new ApiException(404, String.format("No restaurant with the id: \"%s\" exists", req.params("id")));
            }
            allReviews = reviewDao.getAllReviewsByRestaurant(restaurantId);
            return gson.toJson(allReviews);
        });

        post("/restaurants/:restaurantId/reviews/new", "application/json", (req, res) -> {//1.4 add new restaurant review
            int restaurantId = Integer.parseInt(req.params("restaurantId"));
            Review review = gson.fromJson(req.body(), Review.class);
            review.setCreatedat();
            review.setFormattedCreatedAt();
            review.setRestaurantId(restaurantId);
            reviewDao.add(review);
            res.status(201);
            return gson.toJson(review);
        });

        get("/restaurants/:id/sortedReviews", "application/json", (req, res) -> { //// TODO: 1/18/18 generalize this route so that it can be used to return either sorted reviews or unsorted ones.
            int restaurantId = Integer.parseInt(req.params("id"));
            Restaurant restaurantToFind = restaurantDao.findById(restaurantId);
            List<Review> allReviews;
            if (restaurantToFind == null){
                throw new ApiException(404, String.format("No restaurant with the id: \"%s\" exists", req.params("id")));
            }
            allReviews = reviewDao.getAllReviewsByRestaurantSortedNewestToOldest(restaurantId);
            return gson.toJson(allReviews);
        });

        //restaurant-foodtype relationship
        get("/restaurants/:id/foodtypes", "application/json", (req, res) -> {//1.5 view foodtypes in a restaurant
            int restaurantId = Integer.parseInt(req.params("id"));
            Restaurant restaurantToFind = restaurantDao.findById(restaurantId);
            if (restaurantToFind == null){
                throw new ApiException(404, String.format("No restaurant with the id: \"%s\" exists", req.params("id")));
            }
            else if (restaurantDao.Restaurant(restaurantId).size()==0){
                return "{\"message\":\"I'm sorry, but no foodtypes are listed for this restaurant.\"}";
            }
            else {
                return gson.toJson(restaurantDao.getAllFoodtypesByRestaurant(restaurantId));
            }
        });//path not working

        post("/restaurants/:restaurantId/foodtype/:foodtypeId", "application/json", (req, res) -> {//1.6 find foodtype in restaurant
            int restaurantId = Integer.parseInt(req.params("restaurantId"));
            int foodtypeId = Integer.parseInt(req.params("foodtypeId"));
            Restaurant restaurant = restaurantDao.findById(restaurantId);
            Foodtype foodtype = foodtypeDao.findById(foodtypeId);
            if (restaurant != null && foodtype != null){
                //both exist and can be associated - we should probably not connect things that are not here.
                foodtypeDao.addFoodtypeToRestaurant(foodtype, restaurant);
                res.status(201);
                return gson.toJson(String.format("Restaurant '%s' and Foodtype '%s' have been associated",foodtype.getName(), restaurant.getName()));
            }
            else {
                throw new ApiException(404, String.format("Restaurant or Foodtype does not exist"));
            }
        });//path not working

                                        /*end of restaurants*/


                                            /*FOODTYPES*/
        get("/foodtypes", "application/json", (req, res) -> {//2.0 view all foodtypes
            return gson.toJson(foodtypeDao.getAll());
        });

        post("/foodtypes/new", "application/json", (req, res) -> {//2.1 add new foodtypes
            Foodtype foodtype = gson.fromJson(req.body(), Foodtype.class);
            foodtypeDao.add(foodtype);
            res.status(201);
            return gson.toJson(foodtype);
        });//path not working

        //foodtypes-restaurant relationship
        get("/foodtypes/:id/restaurants", "application/json", (req, res) -> {//2.3 find restaurants with particular food type
            int foodtypeId = Integer.parseInt(req.params("id"));
            Foodtype foodtypeToFind = foodtypeDao.findById(foodtypeId);
            if (foodtypeToFind == null){
                throw new ApiException(404, String.format("No foodtype with the id: \"%s\" exists", req.params("id")));
            }
            else if (foodtypeDao.getAllRestaurantsForAFoodtype(foodtypeId).size()==0){
                return "{\"message\":\"I'm sorry, but no restaurants are listed for this foodtype.\"}";
            }
            else {
                return gson.toJson(foodtypeDao.getAllRestaurantsForAFoodtype(foodtypeId));
            }
        });//path not working

                                        /*end of foodtypes*/


        //FILTERS
        exception(ApiException.class, (exception, req, res) -> {
            Map<String, Object> jsonMap = new HashMap<>();
            jsonMap.put("status", ((ApiException) exception).getStatusCode());
            jsonMap.put("errorMessage", exception.getMessage());
            res.type("application/json");
            res.status(((ApiException) exception).getStatusCode());
            res.body(gson.toJson(jsonMap));
        });

        /*exception(ApiException.class, (exc, req, res) -> {
           ApiException err = (ApiException) exc;
           Map<String, Object> jsonMap = new HashMap<>();
           jsonMap.put("status", err.getStatusCode());
           jsonMap.put("errorMessage", err.getMessage());
           res.type("application/json"); //after does not run in case of an exception.
           res.status(err.getStatusCode()); //set the status
           res.body(gson.toJson(jsonMap));  //set the output.
        });*/

        after((req, res) ->{
            res.type("application/json");
        });
    }
}
