/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home.koprvelkyhlad;

import home.entity.CookingIngredientRowMapper;
import home.entity.CookingRecipeRowMapper;
import home.entity.CookingRecipe;
import home.entity.CookingIngredient;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.hsqldb.jdbc.JDBCDataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 *
 * @author Janco1
 */
public class Database {

    private JdbcTemplate jdbcTemplate;
    private PropertyChangeSupport changes = new PropertyChangeSupport(this);

    public Database() {

        JDBCDataSource dataSource = new JDBCDataSource();
        dataSource.setUrl("jdbc:hsqldb:hsql://localhost:12350/hladdb");
        dataSource.setUser("sa");
        dataSource.setPassword("");
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<CookingRecipe> getAllCookingRecipes() throws Exception {
        System.out.println("Database: getAllCookingRecipes()");
        // load recepty bez ingrediencii
        RowMapper<CookingRecipe> rowMapper = new CookingRecipeRowMapper();
        String sql = "SELECT * FROM recipes order by id asc";
        List<CookingRecipe> recipes = jdbcTemplate.query(sql, rowMapper);

        // load ingrediencie k receptom
        RowMapper<CookingIngredient> mapper2 = new CookingIngredientRowMapper();
        sql = "SELECT * FROM ingredients order by recipe_id,ingredient,quantity asc";
        List<CookingIngredient> ingredients = jdbcTemplate.query(sql, mapper2);

        // priradime ingrediencie k svojim receptom
        for (CookingRecipe cr : recipes) {
            for (CookingIngredient ci : ingredients) {
                if (ci.getRecipe_id().equals(cr.getId())) {
                    cr.getIngredients().add(ci);
                }
            }
        }

        return recipes;
    }

    public synchronized CookingRecipe insertCookingRecipe(CookingRecipe recipe) throws Exception {
        System.out.println("Database: insertCookingRecipe(" + recipe + ")");
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate);
        insert.withTableName("recipes");
        insert.setGeneratedKeyName("id");
        Map<String, Object> mapa = new HashMap<String, Object>();
        mapa.put("recipe_name", recipe.getName());
        mapa.put("author", recipe.getAuthor());
        mapa.put("guide", recipe.getGuide());
        //mapa.put("incident_ID", incidentID);
        Number insertedRecipeId = insert.executeAndReturnKey(mapa);
        recipe.setId(insertedRecipeId.longValue());

        insertIngredients(recipe.getIngredients(), insertedRecipeId.longValue());
        for (CookingIngredient ci:recipe.getIngredients()) {
            ci.setRecipe_id(insertedRecipeId.longValue());
        }
        return recipe;
    }

    private void insertIngredients(List<CookingIngredient> ingredients, long recipe_id) {
        System.out.println("Database: insertIngredients(" + ingredients + ", " + recipe_id + ")");
// vytvor register log pre dany blocek
        StringBuilder sql = new StringBuilder("insert into ingredients(recipe_id,ingredient,quantity) values\n");
        if (ingredients.size() == 0) {
            throw new RuntimeException("ingredients size must not be 0");
        }
        for (int i = 0; i < ingredients.size() - 1; i++) {
            CookingIngredient ing = ingredients.get(i);
            sql.append("(" + recipe_id + "  ,'" + ing.getIngredient() + "','" + ing.getQuantity() + "' ),\n");
        }
        CookingIngredient ing = ingredients.get(ingredients.size() - 1);
        sql.append("(" + recipe_id + "  ,'" + ing.getIngredient() + "','" + ing.getQuantity() + "' );");
        jdbcTemplate.execute(sql.toString());
    }

    public CookingRecipe getCookingRecipe(Long id) throws Exception {
        System.out.println("Database: getAllCookingRecipes()");
        // load recepty bez ingrediencii
        RowMapper<CookingRecipe> rowMapper = new CookingRecipeRowMapper();
        String sql = "SELECT * FROM recipes where id=" + id + " ";
        List<CookingRecipe> recipes = jdbcTemplate.query(sql, rowMapper);
        CookingRecipe recipe = null;
        if (recipes.isEmpty()) {
            throw new NotFoundException("nenasiel sa recept s id: " + id);
        } else {
            recipe = recipes.get(0);
            // load ingrediencie k receptom
            RowMapper<CookingIngredient> mapper2 = new CookingIngredientRowMapper();
            sql = "SELECT * FROM ingredients where recipe_id=" + id+" order by recipe_id,ingredient,quantity asc;";
            List<CookingIngredient> ingredients = jdbcTemplate.query(sql, mapper2);
            recipe.setIngredients(ingredients);
        }

        return recipe;
    }

    public void removeCookingRecipe(Long id) throws Exception {
        System.out.println("Database: removeCookingRecipe(" + id + ")");
        StringBuilder sb = new StringBuilder("delete from ingredients where recipe_id=" + id + ";\n");
        sb.append("delete from recipes where id=" + id + ";");
        jdbcTemplate.execute(sb.toString());
    }

    public synchronized CookingRecipe updateCookingRecipe(CookingRecipe recipe) throws Exception {
        System.out.println("Database: updateCookingRecipe(" + recipe + ")");
        StringBuilder sb = new StringBuilder("delete from ingredients where recipe_id=" + recipe.getId() + ";\n");
        sb.append("update recipes set recipe_name='" + recipe.getName() + "', author='" + recipe.getAuthor() + "', guide='" + recipe.getGuide() + "' where id=" + recipe.getId() + " ;\n");
        jdbcTemplate.execute(sb.toString());

        insertIngredients(recipe.getIngredients(), recipe.getId());
        return recipe;
    }

    public List<CookingRecipe> findRecipesWithKeywords(List<String> keywords) throws Exception {
        System.out.println("Database: findRecipesWithKeywords(" + keywords + ")");
        List<CookingRecipe> allCookingRecipes = getAllCookingRecipes();
        List<CookingRecipe> vyhovujuce = new ArrayList<>();
        for (CookingRecipe cr : allCookingRecipes) {
            String guide = cr.getGuide().toLowerCase();
            for (String keyword : keywords) {
                if (guide.contains(keyword)) {
                    vyhovujuce.add(cr);
                    break;
                }
            }
        }
        System.out.println("Database: vyhovujucich receptov: " + vyhovujuce.size());
        return vyhovujuce;
    }

    public List<CookingRecipe> findRecipesWithIngredients(List<String> ingredients) throws Exception {
        System.out.println("Database: findRecipesWithIngredients(" + ingredients + ")");
        List<CookingRecipe> allCookingRecipes = getAllCookingRecipes();
        List<CookingRecipe> vyhovujuce = new ArrayList<>();
        boolean naslo = false;
        for (CookingRecipe cr : allCookingRecipes) {
            // iterujeme vsetky ingrediencie receptu
            for (CookingIngredient ci : cr.getIngredients()) {
                naslo = false;
                for (String ci2 : ingredients) {
                    if (ci.getIngredient().equalsIgnoreCase(ci2)) {
                        vyhovujuce.add(cr);
                        naslo = true;
                        break;
                    }
                }
                if (naslo) {
                    break;
                }
            }
        }
        System.out.println("Database: vyhovujucich receptov: " + vyhovujuce.size());
        return vyhovujuce;
    }

    public List<CookingRecipe> findRecipesWithExactIngredients(List<String> ingredients) throws Exception {
        System.out.println("Database: findRecipesWithExactIngredients(" + ingredients + ")");
        List<CookingRecipe> allCookingRecipes = getAllCookingRecipes();
        List<CookingRecipe> vyhovujuce = new ArrayList<>();
        boolean obsahuje = false;
        for (CookingRecipe cr : allCookingRecipes) {
            // iterujeme vsetky ingrediencie receptu
            // prechadzame ingrediencie a zistujeme ci sa dana ingrediencia nachadza
            for (String ci2 : ingredients) {
                obsahuje = false;// predpokladame ze danu ingredienciu neobsahuje
                for (CookingIngredient ci : cr.getIngredients()) {
                    if (ci.getIngredient().equalsIgnoreCase(ci2)) {
                        obsahuje = true;
                        break;
                    }
                }
                if (!obsahuje) {
                    break;
                }
            }
            // ak najdeme vsetky ingrediencie v recepte, tak vyhovuje
            if (obsahuje) {
                vyhovujuce.add(cr);
            }
        }
        System.out.println("Database: vyhovujucich receptov: " + vyhovujuce.size());
        return vyhovujuce;
    }

}
