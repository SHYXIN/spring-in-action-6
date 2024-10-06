package tacos;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;



import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;


import org.hibernate.validator.constraints.CreditCardNumber;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document
public class TacoOrder implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  private Long id;

  private Date placedAt = new Date();

  // delivery and credit card properties omitted for brevity's sake

  @NotBlank(message = "Delivery name is required")
  private String deliveryName;

  @NotBlank(message = "Street is required")
  private String deliveryStreet;

  @NotBlank(message = "City is required")
  private String deliveryCity;

  @NotBlank(message = "State is required")
  private String deliveryState;

  @NotBlank(message = "Zip code is required")
  private String deliveryZip;

  @CreditCardNumber(message = "Not a valid credit card number")
  private String ccNumber; // 信用卡号码

  @Pattern(regexp = "^(0[1-9]|1[0-2])([\\/])([2-9][0-9])$",
      message = "must be formatted MM/YY")
  private String ccExpiration;  // 信用卡的到期日期

  @Digits(integer = 3, fraction = 0, message = "Invalid CVV")
  private String ccCVV;   //  信用卡的 CVV 安全码


  private List<Taco> tacos = new ArrayList<>();

  public void addTaco(Taco taco) {
    this.tacos.add(taco);
  }


}
