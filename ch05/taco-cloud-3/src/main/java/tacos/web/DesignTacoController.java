package tacos.web;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;
import tacos.Ingredient;
import tacos.Ingredient.Type;
import tacos.Taco;
import tacos.TacoOrder;
import tacos.User;
import tacos.data.IngredientRepository;
import tacos.data.TacoRepository;
import tacos.data.UserRepository;

import javax.validation.Valid;


@Slf4j
@Controller
@RequestMapping("/design")
@SessionAttributes("tacoOrder")
public class DesignTacoController {

  private final IngredientRepository ingredientRepo;

  private TacoRepository tacoRepo;

  private UserRepository userRepo;


  @Autowired
  public DesignTacoController(
      IngredientRepository ingredientRepo,
      TacoRepository tacoRepo,
      UserRepository userRepo) {
    this.ingredientRepo = ingredientRepo;
    this.tacoRepo = tacoRepo;
    this.userRepo = userRepo;
  }

  @ModelAttribute
  public void addIngredientsToModel(Model model) {
    Iterable<Ingredient> ingredients = ingredientRepo.findAll();

    Type[] types = Ingredient.Type.values();
    for (Type type : types) {
      model.addAttribute(type.toString().toLowerCase(),
          filterByType(ingredients, type));
    }
  }

  @ModelAttribute(name = "tacoOrder")
  public TacoOrder order() {
    return new TacoOrder();
  }

  @ModelAttribute(name = "taco")
  public Taco taco() {
    return new Taco();
  }

  @ModelAttribute(name = "user")
  public User user(Principal principal) {
    String username = principal.getName();
    User user = userRepo.findByUsername(username);
    return user;
  }

  @GetMapping
  public String showDesignForm() {
    return "design";
  }

  //    @PostMapping
//    public String processTaco(Taco taco, @ModelAttribute TacoOrder tacoOrder) {
//        tacoOrder.addTaco(taco);
//        log.info("Processing taco: {}", taco);
//
//        return "redirect:/orders/current";
//    }
  @PostMapping
  public String processTaco(@Valid Taco taco, Errors errors,
                            @ModelAttribute TacoOrder tacoOrder) {
    log.info("   --- Saving taco");

    if (errors.hasErrors()) {
      return "design";
    }

    tacoOrder.addTaco(taco);
    log.info("Processing taco: {}", taco);

    return "redirect:/orders/current";
  }


  private Iterable<Ingredient> filterByType(
      Iterable<Ingredient> ingredients, Type type) {
    return StreamSupport.stream(ingredients.spliterator(), false)
        .filter(x -> x.getType().equals(type))
        .collect(Collectors.toList());
  }
}
