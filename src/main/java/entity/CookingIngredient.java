/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.util.Objects;

/**
 *
 * @author Janco1
 */
public class CookingIngredient {
    
    private Long recipe_id;
    private String ingredient;
    private String quantity;

    public Long getRecipe_id() {
        return recipe_id;
    }

    public void setRecipe_id(Long recipe_id) {
        this.recipe_id = recipe_id;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient.replaceAll("'", "").trim();
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity.replaceAll("'", "").trim();
    }

    @Override
    public String toString() {
        return "CookingIngredient{" + "recipe_id=" + recipe_id + ", ingredient=" + ingredient + ", quantity=" + quantity + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CookingIngredient other = (CookingIngredient) obj;
        if (!Objects.equals(this.ingredient, other.ingredient)) {
            return false;
        }
        if (!Objects.equals(this.quantity, other.quantity)) {
            return false;
        }
        if (!Objects.equals(this.recipe_id, other.recipe_id)) {
            return false;
        }
        return true;
    }

    
    
    
}
