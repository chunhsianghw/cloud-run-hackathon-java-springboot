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
  
  public boolean canBeAttacked(String map[][] , int x, int y) {
	  if(x-1>0&&"E".equals(map[x-1][y]))
		  return true;
	  if(x-2>0&&"E".equals(map[x-2][y]))
		  return true;
	  if(x-3>0&&"E".equals(map[x-3][y]))
		  return true;
	  if(x+1<map.length&&"W".equals(map[x+1][y]))
		  return true;
	  if(x+2<map.length&&"W".equals(map[x+2][y]))
		  return true;
	  if(x+3<map.length&&"W".equals(map[x+3][y]))
		  return true;
	  if(y-1>0&&"S".equals(map[y-1][y]))
		  return true;
	  if(y-2>0&&"S".equals(map[y-2][y]))
		  return true;
	  if(y-3>0&&"S".equals(map[y-3][y]))
		  return true;
	  if(y+1<map[0].length&&"N".equals(map[y+1][y]))
		  return true;
	  if(y+2<map[0].length&&"N".equals(map[y+2][y]))
		  return true;
	  if(y+3<map[0].length&&"N".equals(map[y+3][y]))
		  return true;
	  return false;
  }
  
  public boolean canRunWay(String map[][] , int x, int y, String direction) {
	  switch (direction) {
		case "N":
			if(y-1>0&&map[x][y-1]==null) {
				return true;
			}
			break;
		case "S":
			if(y+1<=map[0].length&&map[x][y+1]==null) {
				return true;
			}
			break;
		case "W":
			if(x+1<=map.length&&map[x+1][y]==null) {
				return true;
			}
			break;
		case "E":
			if(x-1>0&&map[x-1][y]==null) {
				return true;
			}
			break;
		}
	  return false;
  }
  
  public String findMaxEnemydirection(String map[][] , PlayerState me) {
	  int cntN = 0;
	  /*if(x-1>0&&"E".equals(map[x-1][y]))
		  cntN++;
	  if(x-2>0&&"E".equals(map[x-2][y]))
		  cntN++;
	  if(x-3>0&&"E".equals(map[x-3][y]))
		  cntN++;
	  
	  int cntN = 0;
	  if(x+1<map.length&&"W".equals(map[x+1][y]))
		  return true;
	  if(x+2<map.length&&"W".equals(map[x+2][y]))
		  return true;
	  if(x+3<map.length&&"W".equals(map[x+3][y]))
		  return true;
	  
	  int cntN = 0;
	  if(y-1>0&&"S".equals(map[y-1][y]))
		  return true;
	  if(y-2>0&&"S".equals(map[y-2][y]))
		  return true;
	  if(y-3>0&&"S".equals(map[y-3][y]))
		  return true;
	  
	  int cntN = 0;
	  if(y+1<map[0].length&&"N".equals(map[y+1][y]))
		  return true;
	  if(y+2<map[0].length&&"N".equals(map[y+2][y]))
		  return true;
	  if(y+3<map[0].length&&"N".equals(map[y+3][y]))
		  return true;
	  */
	  return null;
  }
  
  @PostMapping("/**")
  public String index(@RequestBody ArenaUpdate arenaUpdate) {
    //System.out.println(arenaUpdate);
    Arena arena =arenaUpdate.arena;
    String map[][] = new String[arena.dims.get(0)][arena.dims.get(1)];
    Map<String, PlayerState> playerState = arenaUpdate.arena.state;
    for(PlayerState p : playerState.values()) {
    	map[p.x][p.y]=p.direction;
    }
    PlayerState me = playerState.get(arenaUpdate._links.self.href);
	
    if(canBeAttacked(map, me.x, me.y)) {
    	if(canRunWay(map, me.x, me.y, me.direction)) {
    		return "F";
    	}
    	
    	String[] commands = new String[]{"R", "L"};
        int i = new Random().nextInt(4);
        return commands[i];
    }
    
    return "T";
    /*switch (me.direction) {
	case "N":
		if(me.y==0) {
			return "L";
		}
		break;
	case "W":
		if(me.x==0) {
			return "L";
		}
		break;
	case "S":
		if(me.y==arena.dims.get(1)) {
			return "L";
		}
		break;
	case "E":
		if(me.x==arena.dims.get(0)) {
			return "L";
		}
		break;
	}
	return "F";*/
	
    //String[] commands = new String[]{"F", "R", "L", "T"};
    //int i = new Random().nextInt(4);
    //return commands[i];
  }

}

