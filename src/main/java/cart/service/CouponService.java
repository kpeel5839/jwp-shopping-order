package cart.service;

import cart.controller.response.CouponResponseDto;
import cart.controller.response.DiscountResponseDto;
import cart.domain.Coupon;
import cart.domain.Discount;
import cart.domain.Member;
import cart.domain.MemberCoupon;
import cart.repository.CouponRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class CouponService {

    private final CouponRepository couponRepository;

    public CouponService(final CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }


    public CouponResponseDto issue(final Member member, final Long couponId) {
        MemberCoupon memberCoupons = couponRepository.saveCoupon(member, couponId);
        return CouponResponseDto.from(memberCoupons);
    }

    public List<CouponResponseDto> findAllByMember(final Member member) {
        List<MemberCoupon> memberCouponByMember = couponRepository.findMemberCouponByMember(member);

        return memberCouponByMember.stream()
                .map(CouponResponseDto::from)
                .collect(Collectors.toList());
    }

    public DiscountResponseDto calculateDiscountPrice(final Member member, final Integer originPrice, final Long memberCouponId) {
        Optional<Coupon> coupon = couponRepository
                .findCouponByMemberAndMemberCouponId(member, memberCouponId);

        int priceAfterDiscount = coupon
                .map(notNullCoupon -> notNullCoupon.apply(originPrice))
                .orElseGet(() -> originPrice);

        Discount discount = Discount.of(originPrice, priceAfterDiscount);
        return DiscountResponseDto.from(discount);
    }

}
