package cart.domain;

public class Coupon {

    private static final Double PERCENTAGE = 100.0;

    private final Long id;
    private final String name;
    private final Double discountRate;
    private final Integer discountPrice;

    public Coupon(final Long id, final String name, final Double discountRate, final Integer discountPrice) {
        this.id = id;
        this.name = name;
        this.discountRate = discountRate;
        this.discountPrice = discountPrice;
    }

    public Coupon(final String name, final Double discountRate, final Integer discountPrice) {
       this(null, name, discountRate, discountPrice);
    }

    public Integer apply(Integer originPrice) {
        int priceAfterDiscount = originPrice;
        priceAfterDiscount -= (int) (priceAfterDiscount * (getDiscountRate() / PERCENTAGE));
        priceAfterDiscount -= getDiscountPrice();
        return priceAfterDiscount;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getDiscountRate() {
        return discountRate;
    }

    public Integer getDiscountPrice() {
        return discountPrice;
    }

    @Override
    public String toString() {
        return "Coupon{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", discountRate=" + discountRate +
                ", discountPrice=" + discountPrice +
                '}';
    }

}
