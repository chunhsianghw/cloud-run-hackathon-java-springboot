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
    public String href;
  }

  static class Links {
    public Self self;
  }

  static class PlayerState {
    public Integer x;
    public Integer y;
    public String direction;
    public Boolean wasHit;
    public Integer score;
  }

  static class Arena {
    public List<Integer> dims;
    public Map<String, PlayerState> state;
  }

  static class ArenaUpdate {
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

  @PostMapping("/**")
  public String index(@RequestBody ArenaUpdate arenaUpdate) {
    System.out.println(arenaUpdate);
    
    Arena arena =arenaUpdate.arena;
    Map<String, PlayerState> playerState = arenaUpdate.arena.state;
    logger.info(playerState.keySet().toString());
    /*PlayerState me = playerState.get("阿翔");
	switch (me.direction) {
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
	return "F";
	*/
    String[] commands = new String[]{"F", "R", "L", "T"};
    int i = new Random().nextInt(4);
    return commands[i];
  }

}

