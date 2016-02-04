/*
2. Projekt

Startup velky.hlad.sk chce postaviť aplikáciu na správu kuchárskych receptov. Usúdila, že na tento účel je najlepšie postaviť webovú službu.

Kuchársky recept obsahuje:

    názov receptu
    autora
    zoznam ingrediencí vrátane merných jednotiek (napr. 4 vajcia, 400 g hladkej múky, 1 lyžica cukru)
    postup prípravy. 

Implementujte nasledovné operácie:

   *  pridanie receptu do databázy receptov. Návratovou hodnotou je jednoznačný UUID identifikátor receptu. Vstupom je recept s vyššie uvedenými vlastnosťami.
   *  aktualizácia receptu na základe identifikátora
   *  odstránenie receptu na základe identifikátora
   *  získanie receptu na základe identifikátora
   *  vyhľadanie receptov obsahujúcich zadané kľúčové slovo či slová. V tejto funkcionalite implementujte fulltextové vyhľadávanie, pričom algoritmus môže byť aj veľmi jednoduchý.
   *  vyhľadávanie receptov podľa ingrediencií. Pokryte situácie, keď máte doma napr. vajcia, múku a bravčové mäso a chcete vedieť, aké recepty si môžete pripraviť. 

Pri všetkých operáciách ošetrite situácie, keď sa recept v databáze nenachádza, a to rozumným spôsobom, ktorý klienta dostatočne oboznámi s chybovým stavom.

Implementáciu databázy zvoľte podľa vlastného uváženia. Nezabúdajte na to, že ku databáze receptov budú pristupovať viacerí klienti naraz.
Zvoľte si aspoň jednu z nasledovných možností (REST/SOAP).

I. SOAP webservice

Implementujte webovú službu s vyššie uvedenými operáciami. Služba nech je realizovaná v štýle document/literal alebo RPC/encoded. 
Zverejnite WSDL so službou a vytvorte k nej klienta.
Na implementáciu servera i klienta použite ľubovoľnú technológiu.
Demonštrujte funkčnosť servera i klienta -- odporúčaná forma sú unit testy.

II. REST webservice
Navrhnite a implementujte REST API pre vyššie uvedené operácie na strane servera. Použite ľubovoľný vhodný framework: odporúčaný je Spring MVC (Spring Boot) alebo Restlet.
Implementujte klienta pre uvedenú webovú službu v odlišnej technológii / programovacom jazyku. Ak ste implementovali server v Jave, 
ste povinní implementovať klienta v odlišnom programovacom jazyku: napr. PHP / Python / Pascal a pod.
Demonštrujte funkčnosť servera i klienta -- odporúčaná forma sú unit testy.

Vyjasnenia

    v prípade voľby variantu I. SOAP môžete i klienta i server implementovať v rovnakej technológii na rovnakej platforme (JAX-WS server + JAX-WS klient).
    v prípade voľby variantu II. REST musíte klientskú stranu implementovať v odlišnej technológii než server. 
 */
package home.koprvelkyhlad;

import home.entity.CookingIngredient;
import home.entity.CookingRecipe;
import home.entity.RecipeID;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Janco1
 */
@RestController
@CrossOrigin
@RequestMapping("/velky.hlad.sk")
@Component
public class VelkyHladResource {

    private Database db = new Database();
    //private Logger logger=Shared.logger;

    @RequestMapping("/vsetkyRecepty")
    @ResponseStatus(HttpStatus.OK)
    public List<CookingRecipe> getAllCookingRecipes() {
        System.out.println("VelkyHladResource: REQUEST getAllCookingRecipes()");
        List<CookingRecipe> allCookingRecipes = null;
        try {
            allCookingRecipes = db.getAllCookingRecipes();
            System.out.println("VelkyHladResource: vraciam recepty size: " + allCookingRecipes.size());
        } catch (Exception exception) {
            Logger.getLogger(VelkyHladResource.class.getName()).log(Level.SEVERE, null, exception);
            throw new ServerException(exception.getMessage());
        }
        return allCookingRecipes;
    }

    @RequestMapping(value = "/pridajRecept", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public RecipeID addCookingRecipe(@RequestBody CookingRecipe recipe) {
        System.out.println("VelkyHladResource: REQUEST addCookingRecipe(@RequestBody CookingRecipe recipe)");
        CookingRecipe cr;
        try {
            cr = db.insertCookingRecipe(recipe);
            System.out.println("VelkyHladResource: pridany recipe: " + cr);
        } catch (Exception ex) {
            Logger.getLogger(VelkyHladResource.class.getName()).log(Level.SEVERE, null, ex);
            throw new DataException(ex.getMessage());
        }

        return new RecipeID(cr.getId());
    }

    @RequestMapping(value = "/upravRecept", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public CookingRecipe updateCookingRecipe(@RequestBody CookingRecipe recipe) {
        System.out.println("VelkyHladResource: REQUEST updateCookingRecipe(@RequestBody CookingRecipe recipe)");
        CookingRecipe cr = null;
        try {
            cr = db.updateCookingRecipe(recipe);
            System.out.println("VelkyHladResource: updatnuty recipe");
        } catch (Exception ex) {
            Logger.getLogger(VelkyHladResource.class.getName()).log(Level.SEVERE, null, ex);
            throw new ServerException(ex.getMessage());
        }
        return cr;
    }

    @RequestMapping(value = "/zmazatRecept/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void removeCookingRecipe(@PathVariable Long id) {
        System.out.println("VelkyHladResource: REQUEST delete recipe with id: " + id);
        try {
            db.removeCookingRecipe(id);
            System.out.println("VelkyHladResource: deleted recipe with id: " + id);
        } catch (Exception ex) {
            Logger.getLogger(VelkyHladResource.class.getName()).log(Level.SEVERE, null, ex);
            throw new ServerException(ex.getMessage());
        }
    }

    @RequestMapping(value = "/recept/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CookingRecipe getCookingRecipe(@PathVariable Long id) {
        System.out.println("VelkyHladResource: REQUEST get recipe with id: " + id);
        try {
            CookingRecipe cookingRecipe = db.getCookingRecipe(id);
            System.out.println("VelkyHladResource: vraciam recipe with id: " + cookingRecipe);
            return cookingRecipe;
        } catch (NotFoundException ex) {
            //Logger.getLogger(VelkyHladResource.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        } catch (Exception ex) {
            Logger.getLogger(VelkyHladResource.class.getName()).log(Level.SEVERE, null, ex);
            throw new ServerException(ex.getMessage());
        }
    }

    @RequestMapping(value = "/klucoveSlova")
    @ResponseStatus(HttpStatus.OK)
    public List<String> getKlucoveSlova() {
        List<String> slova = new ArrayList<>();
        slova.add("prve slovo");
        slova.add("druhe slovo");
        return slova;
    }

    @RequestMapping(value = "/ingredients")
    @ResponseStatus(HttpStatus.OK)
    public List<String> getIngredients() {
        List<String> ingredients = new ArrayList<>();
        ingredients.add("ci1");
        ingredients.add("ci2");
        return ingredients;
    }

    @RequestMapping(value = "/findWithKeywords", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public List<CookingRecipe> findWithKeywords(@RequestBody List<String> keywords) {
        System.out.println("VelkyHladResource: REQUEST get recipe with keywords: " + keywords);
        try {
            List<CookingRecipe> foundRecipes = db.findRecipesWithKeywords(keywords);
            return foundRecipes;
        } catch (Exception ex) {
            Logger.getLogger(VelkyHladResource.class.getName()).log(Level.SEVERE, null, ex);
            throw new ServerException(ex.getMessage());
        }
    }

    @RequestMapping(value = "/findWithIngredients", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public List<CookingRecipe> findWithIngredients(@RequestBody List<String> ingredients) {
        System.out.println("VelkyHladResource: REQUEST get recipe with ingredients: " + ingredients);
        try {
            List<CookingRecipe> foundRecipes = db.findRecipesWithIngredients(ingredients);
            return foundRecipes;
        } catch (Exception ex) {
            Logger.getLogger(VelkyHladResource.class.getName()).log(Level.SEVERE, null, ex);
            throw new ServerException(ex.getMessage());
        }
    }
    
    @RequestMapping(value = "/findWithExactIngredients", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public List<CookingRecipe> findWithExactIngredients(@RequestBody List<String> ingredients) {
        System.out.println("VelkyHladResource: REQUEST get recipe with exact ingredients: " + ingredients);
        try {
            List<CookingRecipe> foundRecipes = db.findRecipesWithExactIngredients(ingredients);
            return foundRecipes;
        } catch (Exception ex) {
            Logger.getLogger(VelkyHladResource.class.getName()).log(Level.SEVERE, null, ex);
            throw new ServerException(ex.getMessage());
        }
    }

}
