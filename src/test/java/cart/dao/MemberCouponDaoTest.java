package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import cart.dto.CouponDto;
import cart.dto.MemberCouponDto;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

@JdbcTest
class MemberCouponDaoTest {

    private final RowMapper<MemberCouponDto> memberCouponDtoRowMapper = (rs, rn) -> new MemberCouponDto(
            rs.getLong("id"),
            rs.getLong("member_id"),
            rs.getLong("coupon_id"));

    @Autowired
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    private MemberCouponDao memberCouponDao;

    @BeforeEach
    void beforeEach() {
        memberCouponDao = new MemberCouponDao(dataSource);
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Test
    @DisplayName("Member Coupon 저장을 확인한다.")
    void createMemberCoupon_success() {
        MemberCouponDto saveCouponDto = new MemberCouponDto(1L, 2L, 3L);

        Long insert = memberCouponDao.insert(saveCouponDto);

        String sql = "SELECT * FROM member_coupon where id = ?";
        MemberCouponDto memberCouponDto = jdbcTemplate.queryForObject(
                sql,
                memberCouponDtoRowMapper,
                insert);
        assertThat(memberCouponDto).extracting(
                MemberCouponDto::getId,
                MemberCouponDto::getMemberId,
                MemberCouponDto::getCouponId
        ).contains(1L, 2L, 3L);
    }

    @Test
    @DisplayName("Member Coupon Dto 를 조회하는 기능 테스트")
    void findByIdTest() {
        String sql = "INSERT INTO member_coupon(id, member_id, coupon_id) VALUES(?, ?, ?)";
        jdbcTemplate.update(sql, 1L, 2L, 3L);
        MemberCouponDto queryResult = memberCouponDao.findById(1L).orElseThrow(IllegalArgumentException::new);
        assertThat(queryResult.getId()).isEqualTo(1L);
        assertThat(queryResult.getMemberId()).isEqualTo(2L);
        assertThat(queryResult.getCouponId()).isEqualTo(3L);
    }

    @Test
    @DisplayName("찾는 Member Coupon Dto 가 없는 경우 빈 Optional 을 반환한다.")
    void findById_returnEmpty() {
        Optional<MemberCouponDto> memberCouponDto = memberCouponDao.findById(1L);
        assertThat(memberCouponDto).isNotPresent();
    }

    @Test
    @DisplayName("Member id 로 해당하는 Member Coupon 을 찾는다.")
    void findByMemberId() {
        String sql = "INSERT INTO member_coupon(id, member_id, coupon_id) VALUES(?, ?, ?)";
        jdbcTemplate.update(sql, 1L, 2L, 3L);
        jdbcTemplate.update(sql, 2L, 2L, 4L);
        jdbcTemplate.update(sql, 3L, 1L, 5L);

        List<MemberCouponDto> memberCouponDtos = memberCouponDao.findByMemberId(2L);
        assertThat(memberCouponDtos).hasSize(2)
                .extracting(MemberCouponDto::getMemberId, MemberCouponDto::getCouponId)
                .containsExactly(tuple(2L, 3L), tuple(2L, 4L));
    }

}