package fixture;

import static fixture.MemberFixture.MEMBER_1;
import static fixture.MemberFixture.MEMBER_2;
import static fixture.ProductFixture.PRODUCT_1;
import static fixture.ProductFixture.PRODUCT_2;
import static fixture.ProductFixture.PRODUCT_3;

import cart.domain.CartItem;

public class CartItemFixture {

    public static final CartItem CART_ITEM_1 = new CartItem(1L, 2, PRODUCT_1, MEMBER_1);
    public static final CartItem CART_ITEM_2 = new CartItem(2L, 4, PRODUCT_2, MEMBER_1);
    public static final CartItem CART_ITEM_3 = new CartItem(3L, 5, PRODUCT_3, MEMBER_2);

}