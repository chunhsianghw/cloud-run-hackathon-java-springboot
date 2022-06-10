package hello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Random;

@SpringBootApplication
@RestController
public class Application {
	private static final Logger logger = LoggerFactory.getLogger(Application.class);

	static class Self {
		@Override
		public String toString() {
			return "Self [href=" + href + "]";
		}

		public String href;
	}

	static class Links {
		@Override
		public String toString() {
			return "Links [self=" + self + "]";
		}

		public Self self;
	}

	static class PlayerState {
		@Override
		public String toString() {
			return "PlayerState [x=" + x + ", y=" + y + ", direction=" + direction + ", wasHit=" + wasHit + ", score="
					+ score + "]";
		}

		public Integer x;
		public Integer y;
		public String direction;
		public Boolean wasHit;
		public Integer score;
	}

	static class Arena {
		@Override
		public String toString() {
			return "Arena [dims=" + dims + ", state=" + state + "]";
		}

		public List<Integer> dims;
		public Map<String, PlayerState> state;
	}

	static class ArenaUpdate {
		@Override
		public String toString() {
			return "ArenaUpdate [_links=" + _links + ", arena=" + arena + "]";
		}

		public Links _links;
		public Arena arena;
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.initDirectFieldAccess();
	}

	@GetMapping("/")
	public String index() {
		return "Let the battle begin!";
	}

	public boolean canBeAttacked(String map[][], int x, int y) {
		if (x - 1 > 0 && "E".equals(map[x - 1][y]))
			return true;
		if (x - 2 > 0 && "E".equals(map[x - 2][y]))
			return true;
		if (x - 3 > 0 && "E".equals(map[x - 3][y]))
			return true;
		if (x + 1 < map.length && "W".equals(map[x + 1][y]))
			return true;
		if (x + 2 < map.length && "W".equals(map[x + 2][y]))
			return true;
		if (x + 3 < map.length && "W".equals(map[x + 3][y]))
			return true;
		if (y - 1 > 0 && "S".equals(map[y - 1][y]))
			return true;
		if (y - 2 > 0 && "S".equals(map[y - 2][y]))
			return true;
		if (y - 3 > 0 && "S".equals(map[y - 3][y]))
			return true;
		if (y + 1 < map[0].length && "N".equals(map[y + 1][y]))
			return true;
		if (y + 2 < map[0].length && "N".equals(map[y + 2][y]))
			return true;
		if (y + 3 < map[0].length && "N".equals(map[y + 3][y]))
			return true;
		return false;
	}

	public boolean canRunWay(String map[][], int x, int y, String direction) {
		switch (direction) {
		case "N":
			if (y - 1 > 0 && map[x][y - 1] == null) {
				return true;
			}
			break;
		case "S":
			if (y + 1 < map[0].length && map[x][y + 1] == null) {
				return true;
			}
			break;
		case "W":
			if (x + 1 < map.length && map[x + 1][y] == null) {
				return true;
			}
			break;
		case "E":
			if (x - 1 > 0 && map[x - 1][y] == null) {
				return true;
			}
			break;
		}
		return false;
	}

	public String findMaxEnemydirection(String map[][], PlayerState me) {
		int cntW = 0;
		if (me.x - 1 > 0 && map[me.x - 1][me.y] != null)
			cntW++;
		if (me.x - 2 > 0 && "E".equals(map[me.x - 2][me.y]))
			cntW++;
		if (me.x - 3 > 0 && "E".equals(map[me.x - 3][me.y]))
			cntW++;

		int cntE = 0;
		if (me.x + 1 < map.length && map[me.x + 1][me.y] != null)
			cntE++;
		if (me.x + 2 < map.length && map[me.x + 2][me.y] != null)
			cntE++;
		if (me.x + 3 < map.length && map[me.x + 3][me.y] != null)
			cntE++;

		int cntN = 0;
		if (me.y - 1 > 0 && map[me.y - 1][me.y] != null)
			cntN++;
		if (me.y - 2 > 0 && map[me.y - 2][me.y] != null)
			cntN++;
		if (me.y - 3 > 0 && map[me.y - 3][me.y] != null)
			cntN++;

		int cntS = 0;
		if (me.y + 1 < map[0].length && map[me.y + 1][me.y] != null)
			cntS++;
		if (me.y + 2 < map[0].length && map[me.y + 2][me.y] != null)
			cntS++;
		if (me.y + 3 < map[0].length && map[me.y + 3][me.y] != null)
			cntS++;

		int max = Math.max(Math.max(cntW, cntE),Math.max(cntN, cntS));
		if(max==0) return null;
		if(cntW==max) return "W";
		if(cntE==max) return "E";
		if(cntN==max) return "N";
		if(cntS==max) return "S";
		return null;
	}

	public String foo(ArenaUpdate arenaUpdate) {
		try {
			// System.out.println(arenaUpdate);
			Arena arena = arenaUpdate.arena;
			String map[][] = new String[arena.dims.get(0)][arena.dims.get(1)];
			Map<String, PlayerState> playerState = arenaUpdate.arena.state;
			System.out.println("me="+playerState);
			for (PlayerState p : playerState.values()) {
				map[p.x][p.y] = p.direction;
			}
			PlayerState me = playerState.get(arenaUpdate._links.self.href);

			if (canBeAttacked(map, me.x, me.y)) {
				if (canRunWay(map, me.x, me.y, me.direction)) {
					return "F";
				}

				String[] commands = new String[] { "R", "L" };
				int i = new Random().nextInt(2);
				return commands[i];
			}

			String targetD = findMaxEnemydirection(map, me);
			if(targetD==null) {
				String[] commands = new String[] { "F", "R", "L" };
				int i = new Random().nextInt(3);
				return commands[i];
			}
			if("N".equals(targetD)&&me.direction.equals("N")) {
				return "T";
			}else if("S".equals(targetD)&&me.direction.equals("S")) {
				return "T";
			}else if("W".equals(targetD)&&me.direction.equals("W")) {
				return "T";
			}else if("E".equals(targetD)&&me.direction.equals("E")) {
				return "T";
			}
			//String[] commands = new String[] { "R", "L" };
			//int i = new Random().nextInt(2);
			//return commands[i];
			return "R";
		} catch (Exception e) {
			e.printStackTrace();
			String[] commands = new String[] { "F", "R", "L", "T" };
			int i = new Random().nextInt(4);
			return commands[i];
		}
	}
	
	@PostMapping("/**")
	public String index(@RequestBody ArenaUpdate arenaUpdate) {
		String r = foo(arenaUpdate);
		System.out.println(r);
		return r;
	}

}
