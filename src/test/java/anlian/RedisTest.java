package anlian;



import may.yuntian.AnlianApplication;
import may.yuntian.untils.AlRedisUntil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Map;

@SpringBootTest(classes = AnlianApplication.class)
public class RedisTest {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private AlRedisUntil alRedisUntil;
	@Test
	public void name() {

        JedisConnectionFactory jedisConnectionFactory = (JedisConnectionFactory) stringRedisTemplate.getConnectionFactory();
        assert jedisConnectionFactory != null;
        jedisConnectionFactory.setDatabase(2);
        stringRedisTemplate.setConnectionFactory(jedisConnectionFactory);
        Map map = AlRedisUntil.jsonToMap(stringRedisTemplate.opsForValue().get("token_exchange")) ;
        System.out.println(map);

	}
	
	

}
