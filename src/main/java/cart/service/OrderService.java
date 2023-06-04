package cart.service;

import cart.controller.request.OrderRequestDto;
import cart.controller.response.OrderResponseDto;
import cart.domain.CartItem;
import cart.domain.Coupon;
import cart.domain.Member;
import cart.domain.Order;
import cart.repository.CouponRepository;
import cart.repository.OrderRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CouponRepository couponRepository;

    public OrderService(final OrderRepository orderRepository, final CouponRepository couponRepository) {
        this.orderRepository = orderRepository;
        this.couponRepository = couponRepository;
    }

    public OrderResponseDto orderCartItems(Member member, OrderRequestDto orderRequestDto) {
        List<CartItem> cartItems = orderRepository.findCartItemsByIds(orderRequestDto.getCartItemIds());
        Coupon coupon = orderRequestDto.getCouponId()
                .flatMap(couponId -> couponRepository.findCouponByMemberAndMemberCouponId(member, couponId))
                .orElse(null);
        Order order = Order.of(member, coupon, cartItems);
        Order orderAfterSave = orderRepository.save(order);
        orderRepository.deleteCartItems(orderRequestDto.getCartItemIds());
        couponRepository.deleteMemberCouponById(orderRequestDto.getCouponId());
        return OrderResponseDto.from(orderAfterSave);
    }

    public OrderResponseDto findOrderById(final Member member ,final Long orderId) {
        Order orderById = orderRepository.findOrderById(member, orderId);

        return OrderResponseDto.from(orderById);
    }

    public List<OrderResponseDto> findAllOrder(Member member) {
        return orderRepository.findOrdersByMember(member)
                .stream()
                .map(OrderResponseDto::from)
                .collect(Collectors.toList());
    }

}
