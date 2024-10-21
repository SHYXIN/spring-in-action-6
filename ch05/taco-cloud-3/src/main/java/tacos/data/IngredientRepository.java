package tacos.data;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;
import tacos.Ingredient;

public interface IngredientRepository
    extends CrudRepository<Ingredient, String> {
}