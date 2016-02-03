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
public class CookingRecipeRowMapper implements RowMapper<CookingRecipe> {

    public CookingRecipeRowMapper() {
    }

    @Override
    public CookingRecipe mapRow(ResultSet rs, int i) throws SQLException {
        CookingRecipe recipe = new CookingRecipe();
        recipe.setId(rs.getLong("id"));
        recipe.setAuthor(rs.getString("author"));
        recipe.setName(rs.getString("recipe_name"));
        recipe.setGuide(rs.getString("guide"));
        return recipe;
    }

}
