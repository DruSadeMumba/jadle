package dao;

import models.*;
import models.dao.*;
import org.junit.*;
import org.junit.jupiter.api.Test;
import org.sql2o.*;
import static org.junit.jupiter.api.Assertions.*;

public class SqlReviewDaoTest {
    static String connectionString = "jdbc:postgresql://localhost:5432/jadle_test";
    static Sql2o sql2o = new Sql2o(connectionString, null, null);
    private static Connection conn;
    private static SqlReviewDao reviewDao = new SqlReviewDao(sql2o);
    private static SqlRestaurantDao restaurantDao = new SqlRestaurantDao(sql2o);

    @BeforeClass
    public static void setUp() throws Exception {
        String connectionString = "jdbc:postgresql://localhost:5432/jadle_test";
        Sql2o sql2o = new Sql2o(connectionString, null, null);
        reviewDao = new SqlReviewDao(sql2o);
        restaurantDao = new SqlRestaurantDao(sql2o);
        conn = sql2o.open();
    }
    @AfterClass
    public static void tearDown() throws Exception {
        conn.close();
        System.out.println("connection closed");
    }

    @Test
    public void addingReviewSetsId() throws Exception {//passed
        Review testReview = setupReview();
        assertEquals(1, testReview.getId());
    }

    @Test
    public void getAll() throws Exception {//failed
        Review review1 = setupReview();
        Review review2 = setupReview();
        assertEquals(2, reviewDao.getAll().size());
    }

    @Test
    public void getAllReviewsByRestaurant() throws Exception {//failed
        Restaurant testRestaurant = setupRestaurant();
        Restaurant otherRestaurant = setupRestaurant(); //add in some extra data to see if it interferes
        Review review1 = setupReviewForRestaurant(testRestaurant);
        Review review2 = setupReviewForRestaurant(testRestaurant);
        Review reviewForOtherRestaurant = setupReviewForRestaurant(otherRestaurant);
        assertEquals(2, reviewDao.getAllReviewsByRestaurant(testRestaurant.getId()).size());
    }

    @Test
    public void deleteById() throws Exception {//failed
        Review testReview = setupReview();
        Review otherReview = setupReview();
        assertEquals(2, reviewDao.getAll().size());
        reviewDao.deleteById(testReview.getId());
        assertEquals(1, reviewDao.getAll().size());
    }

    @Test
    public void clearAll() throws Exception {//passed
        Review testReview = setupReview();
        Review otherReview = setupReview();
        reviewDao.clearAll();
        assertEquals(0, reviewDao.getAll().size());
    }
    @Test
    public void timeStampIsReturnedCorrectly() throws Exception {
        Restaurant testRestaurant = setupRestaurant();
        restaurantDao.add(testRestaurant);
        Review testReview = new Review("Captain Kirk", "foodcoma!", 3, testRestaurant.getId());
        reviewDao.add(testReview);

        String formattedCreationTime = testReview.getFormattedCreatedAt();
        String formattedSavedTime = reviewDao.getAll().get(0).getFormattedCreatedAt();
        long creationTime = testReview.getCreatedat();
        long savedTime = reviewDao.getAll().get(0).getCreatedat();
        assertEquals(formattedCreationTime,formattedSavedTime);
        assertEquals(creationTime, savedTime);
    }

    @Test
    public void reviewsAreReturnedInCorrectOrder() throws Exception {
        Restaurant testRestaurant = setupRestaurant();
        restaurantDao.add(testRestaurant);
        Review testReview = new Review("Captain Kirk", "foodcoma!",  3, testRestaurant.getId());
        reviewDao.add(testReview);
        try {
            Thread.sleep(2000);
        }
        catch (InterruptedException ex){
            ex.printStackTrace();
        }

        Review testSecondReview = new Review("Mr Spock", "passable", 1,  testRestaurant.getId());
        reviewDao.add(testSecondReview);
        try {
            Thread.sleep(2000);
        }
        catch (InterruptedException ex){
            ex.printStackTrace();
        }

        Review testThirdReview = new Review("Scotty",  "bloody good grub!", 4, testRestaurant.getId());
        reviewDao.add(testThirdReview);
        try {
            Thread.sleep(2000);
        }
        catch (InterruptedException ex){
            ex.printStackTrace();
        }

        Review testFourthReview = new Review("Mr. Sulu",  "I prefer home cooking", 2, testRestaurant.getId());
        reviewDao.add(testFourthReview);

        assertEquals(4, reviewDao.getAllReviewsByRestaurant(testRestaurant.getId()).size()); //it is important we verify that the list is the same size.
        assertEquals("I prefer home cooking", reviewDao.getAllReviewsByRestaurantSortedNewestToOldest(testRestaurant.getId()).get(0).getContent());
    }

    //helpers
    public Review setupReview() {
        Review review = new Review("great", "Kim", 4, 555);
        reviewDao.add(review);
        return review;
    }

    public Review setupReviewForRestaurant(Restaurant restaurant) {
        Review review = new Review("great", "Kim", 4, restaurant.getId());
        reviewDao.add(review);
        return review;
    }

    public Restaurant setupRestaurant() {
        Restaurant restaurant = new Restaurant("Fish Witch", "214 NE Broadway", "97232", "503-402-9874", "http://fishwitch.com", "hellofishy@fishwitch.com");
        restaurantDao.add(restaurant);
        return restaurant;
    }
}