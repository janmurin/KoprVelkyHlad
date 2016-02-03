/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author Janco1
 */
public class CookingIngredientRowMapper implements RowMapper<CookingIngredient> {

    public CookingIngredientRowMapper() {
    }

    @Override
    public CookingIngredient mapRow(ResultSet rs, int i) throws SQLException {
        CookingIngredient ing = new CookingIngredient();
        ing.setIngredient(rs.getString("ingredient"));
        ing.setQuantity(rs.getString("quantity"));
        ing.setRecipe_id(rs.getLong("recipe_id"));
        return ing;
    }
    

}
