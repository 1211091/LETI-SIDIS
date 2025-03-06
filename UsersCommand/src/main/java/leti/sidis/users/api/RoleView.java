package leti.sidis.users.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleView {
    private String admin = "USER_ADMIN";
    private String marketing_director = "MARKETING_DIRECTOR";
    private String product_manager = "PRODUCT_MANAGER";
    public String subscriber = "SUBSCRIBER";
    public String new_customer = "NEW_CUSTOMER";
}
