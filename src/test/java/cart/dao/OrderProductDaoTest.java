package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;

import anotation.RepositoryTest;
import cart.dao.dto.OrderProductDto;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

@RepositoryTest
class OrderProductDaoTest {

    private static final RowMapper<OrderProductDto> orderProductDtoRowMapper = (rs, rn) -> new OrderProductDto(
            rs.getLong("id"),
            rs.getLong("order_id"),
            rs.getLong("product_id"),
            rs.getInt("quantity")
    );

    @Autowired
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    private OrderProductDao orderProductDao;

    @BeforeEach
    void beforeEach() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        orderProductDao = new OrderProductDao(dataSource);
    }

    @Test
    @DisplayName("OrdersProduct 저장")
    void insert() {
        OrderProductDto orderProductDto = new OrderProductDto(2L, 3L, 1);

        Long orderProductId = orderProductDao.insert(orderProductDto);

        String sql = "Select * from orders_product where id = ?";
        OrderProductDto queryResultOrderProduct
                = jdbcTemplate.queryForObject(sql, orderProductDtoRowMapper, orderProductId);
        assertThat(queryResultOrderProduct)
                .extracting(
                        OrderProductDto::getId,
                        OrderProductDto::getOrderId,
                        OrderProductDto::getProductId,
                        OrderProductDto::getQuantity
                )
                .contains(orderProductId, 2L, 3L, 1);
    }

    @Test
    @DisplayName("OrdersProduct 조회하는 기능 테스트")
    void findById() {
        String sql = "INSERT INTO orders_product(id, order_id, product_id, quantity) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, 1L, 2L, 3L, 4);

        OrderProductDto queryResultOrderProduct = orderProductDao.findById(1L)
                .orElseThrow(IllegalArgumentException::new);

        assertThat(queryResultOrderProduct)
                .extracting(OrderProductDto::getId, OrderProductDto::getOrderId
                        , OrderProductDto::getProductId, OrderProductDto::getQuantity)
                .contains(1L, 2L, 3L, 4);
    }

    @Test
    @DisplayName("OrderId 로 OrderProduct 들을 찾는다.")
    void findOrderProductByOrderId() {

    }

}