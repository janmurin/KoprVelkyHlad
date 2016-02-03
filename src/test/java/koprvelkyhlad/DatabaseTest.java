/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package koprvelkyhlad;

import entity.CookingIngredient;
import entity.CookingRecipe;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Janco1
 */
public class DatabaseTest {

    Database db = new Database();

    public DatabaseTest() {

    }

    @Test
    public void testGetAllRecipes() throws Exception {
        System.out.println("");
        System.out.println("testGetAllRecipes");
        List<CookingRecipe> allCookingRecipes = db.getAllCookingRecipes();
        for (CookingRecipe cr : allCookingRecipes) {
            if (cr.getAuthor() == null || cr.getGuide() == null || cr.getId() == null || cr.getIngredients() == null || cr.getName() == null) {
                fail("returned object is null");
            }
            for (CookingIngredient ci : cr.getIngredients()) {
                if (ci.getIngredient() == null || ci.getQuantity() == null || ci.getRecipe_id() == null) {
                    fail("returned object is null");
                }
            }
        }
    }

    @Test
    public void insertCookingRecipe() throws Exception {
        System.out.println("");
        System.out.println("insertCookingRecipe");
        CookingRecipe novy = new CookingRecipe();
        novy.setAuthor("test author");
        novy.setGuide("default guide");
        novy.setName("test name");
        CookingIngredient ci = new CookingIngredient();
        ci.setIngredient("test ing1");
        ci.setQuantity("test qty1");
        CookingIngredient ci2 = new CookingIngredient();
        ci2.setIngredient("test ing2");
        ci2.setQuantity("test qty2");
        novy.getIngredients().add(ci);
        novy.getIngredients().add(ci2);

        CookingRecipe inserted = db.insertCookingRecipe(novy);
        ci.setRecipe_id(inserted.getId());
        ci2.setRecipe_id(inserted.getId());
        assertNotNull(inserted.getIngredients().get(0).getRecipe_id());
        assertNotNull(inserted.getIngredients().get(1).getRecipe_id());
        System.out.println("po inserte z db z DB" + inserted);
        CookingRecipe cookingRecipe = db.getCookingRecipe(inserted.getId());
        System.out.println("idckovy z DB" + cookingRecipe);
        System.out.println("novy: " + novy);
        cookingRecipe.getIngredients().removeAll(novy.getIngredients());
        System.out.println("po zmazani: " + cookingRecipe);

        assertTrue(cookingRecipe.getAuthor().equals(novy.getAuthor())
                && cookingRecipe.getName().equals(novy.getName())
                && cookingRecipe.getGuide().equals(novy.getGuide())
                && cookingRecipe.getIngredients().size() == 0);
    }

    @Test
    public void getCookingRecipe() throws Exception {
        System.out.println("");
        System.out.println("getCookingRecipe");
        List<CookingRecipe> allCookingRecipes = db.getAllCookingRecipes();
       CookingRecipe getRecipe = allCookingRecipes.get((int) (Math.random() * allCookingRecipes.size()));
        //CookingRecipe getRecipe = allCookingRecipes.get(0);
        System.out.println("getRecipe: " + getRecipe);
        CookingRecipe cookingRecipe = db.getCookingRecipe(getRecipe.getId());
        System.out.println("cookingRecipe: " + cookingRecipe);
        // overime ci maju rovnake hodnoty
        assertTrue(getRecipe.equals(cookingRecipe));
    }

    @Test
    public void removeCookingRecipe() throws Exception {
        System.out.println("");
        System.out.println("removeCookingRecipe");
        CookingRecipe novy = new CookingRecipe();
        novy.setAuthor("test author");
        novy.setGuide("default guide");
        novy.setName("test name");
        CookingIngredient ci = new CookingIngredient();
        ci.setIngredient("test ing1");
        ci.setQuantity("test qty1");
        CookingIngredient ci2 = new CookingIngredient();
        ci2.setIngredient("test ing2");
        ci2.setQuantity("test qty2");
        novy.getIngredients().add(ci);
        novy.getIngredients().add(ci2);

        CookingRecipe inserted = db.insertCookingRecipe(novy);
        db.removeCookingRecipe(inserted.getId());
        try {
            CookingRecipe cookingRecipe = db.getCookingRecipe(inserted.getId());
        } catch (NotFoundException notFoundException) {
            assertTrue(true);
        }
        assertFalse(false);
    }

    @Test
    public void updateCookingRecipe() throws Exception {
        System.out.println("");
        System.out.println("updateCookingRecipe");
        CookingRecipe novy = new CookingRecipe();
        novy.setAuthor("test author");
        novy.setGuide("default guide");
        novy.setName("test name");
        CookingIngredient ci = new CookingIngredient();
        ci.setIngredient("test ing1");
        ci.setQuantity("test qty1");
        CookingIngredient ci2 = new CookingIngredient();
        ci2.setIngredient("test ing2");
        ci2.setQuantity("test qty2");
        novy.getIngredients().add(ci);
        novy.getIngredients().add(ci2);

        CookingRecipe inserted = db.insertCookingRecipe(novy);
        inserted.setAuthor("modified author");
        inserted.setGuide("modified guide");
        inserted.setName("modified name");
        CookingRecipe updateCookingRecipe = db.updateCookingRecipe(inserted);
        assertTrue(inserted.equals(updateCookingRecipe));
    }

    @Test
    public void findRecipesWithKeywords() throws Exception {

    }

    @Test
    public void findRecipesWithIngredients() throws Exception {

    }

    @Test
    public void findRecipesWithExactIngredients() throws Exception {

    }

}
