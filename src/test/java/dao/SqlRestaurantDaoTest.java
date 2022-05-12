package dao;

import java.util.Arrays;
import models.*;
import models.dao.*;
import org.junit.*;
import org.junit.jupiter.api.Test;
import org.sql2o.*;
import static org.junit.jupiter.api.Assertions.*;

public class SqlRestaurantDaoTest {
    static String connectionString = "jdbc:postgresql://localhost:5432/jadle_test";
    static Sql2o sql2o = new Sql2o(connectionString, null, null);
    private static Connection conn;
    private static SqlRestaurantDao restaurantDao = new SqlRestaurantDao(sql2o);
    private static SqlFoodtypeDao foodtypeDao = new SqlFoodtypeDao(sql2o);
    private static SqlReviewDao reviewDao = new SqlReviewDao(sql2o);

    @BeforeClass
    public static void setUp() throws Exception {
        String connectionString = "jdbc:postgresql://localhost:5432/jadle_test";
        Sql2o sql2o = new Sql2o(connectionString, null, null);
        restaurantDao = new SqlRestaurantDao(sql2o);
        foodtypeDao = new SqlFoodtypeDao(sql2o);
        reviewDao = new SqlReviewDao(sql2o);
        conn = sql2o.open();
    }

    @AfterClass
    public static void shutDown() throws Exception{
        conn.close();
        System.out.println("connection closed");
    }

    @Test
    public void addingFoodSetsId() throws Exception {
        Restaurant testRestaurant = setupRestaurant();
        assertNotEquals(0, testRestaurant.getId());
    }

    @Test
    public void addedRestaurantsAreReturnedFromGetAll() throws Exception {
        Restaurant testRestaurant = setupRestaurant();
        assertEquals(1, restaurantDao.getAll().size());
    }

    @Test
    public void noRestaurantsReturnsEmptyList() throws Exception {
        assertEquals(0, restaurantDao.getAll().size());
    }

    @Test
    public void findByIdReturnsCorrectRestaurant() throws Exception {
        Restaurant testRestaurant = setupRestaurant();
        Restaurant otherRestaurant = setupRestaurant();
        assertEquals(testRestaurant, restaurantDao.findById(testRestaurant.getId()));
    }

    @Test
    public void updateCorrectlyUpdatesAllFields() throws Exception {
        Restaurant testRestaurant = setupRestaurant();
        restaurantDao.update(testRestaurant.getId(), "a", "b", "c", "d", "e", "f");
        Restaurant foundRestaurant = restaurantDao.findById(testRestaurant.getId());
        assertEquals("a", foundRestaurant.getName());
        assertEquals("b", foundRestaurant.getAddress());
        assertEquals("c", foundRestaurant.getZipcode());
        assertEquals("d", foundRestaurant.getPhone());
        assertEquals("e", foundRestaurant.getWebsite());
        assertEquals("f", foundRestaurant.getEmail());
    }

    @Test
    public void deleteByIdDeletesCorrectRestaurant() throws Exception {
        Restaurant testRestaurant = setupRestaurant();
        Restaurant otherRestaurant = setupRestaurant();
        restaurantDao.deleteById(testRestaurant.getId());
        assertEquals(1, restaurantDao.getAll().size());
    }

    @Test
    public void clearAll() throws Exception {
        Restaurant testRestaurant = setupRestaurant();
        Restaurant otherRestaurant = setupRestaurant();
        restaurantDao.clearAll();
        assertEquals(0, restaurantDao.getAll().size());
    }

    @Test
    public void RestaurantReturnsFoodtypesCorrectly() throws Exception {
        Foodtype testFoodtype  = new Foodtype("Seafood");
        foodtypeDao.add(testFoodtype);

        Foodtype otherFoodtype  = new Foodtype("Bar Food");
        foodtypeDao.add(otherFoodtype);

        Restaurant testRestaurant = setupRestaurant();
        restaurantDao.add(testRestaurant);
        restaurantDao.addRestaurantToFoodtype(testRestaurant,testFoodtype);
        restaurantDao.addRestaurantToFoodtype(testRestaurant,otherFoodtype);

        Foodtype[] foodtypes = {testFoodtype, otherFoodtype}; //oh hi what is this?

        assertEquals(Arrays.asList(foodtypes), restaurantDao.getAllFoodtypesByRestaurant(testRestaurant.getId()));
    }


    //helpers

    public Restaurant setupRestaurant (){
        Restaurant restaurant = new Restaurant("Fish Omena", "214 NE Ngara", "97232", "254-402-9874", "http://fishwitch.com", "hellofishy@fishwitch.com");
        restaurantDao.add(restaurant);
        return restaurant;
    }

    public Restaurant setupAltRestaurant (){
        Restaurant restaurant = new Restaurant("Fish Omena", "214 NE Ngara", "97232", "254-402-9874");
        restaurantDao.add(restaurant);
        return restaurant;
    }
}