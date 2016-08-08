//$Id$
package redisQuery;
import redis.clients.jedis.Jedis;

public class RedisSql {
	public static void main(String[] args) {
		Jedis a = new Jedis("localhost", 6379);
		System.out.println(a);
		a.close();
	}
}
