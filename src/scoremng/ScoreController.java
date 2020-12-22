package scoremng;

import java.net.URI;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans 
	.factory.annotation.Autowired; 
import org.springframework.http 
	.ResponseEntity; 
import org.springframework.web.bind 
	.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind 
	.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind 
	.annotation.RequestBody; 
import org.springframework.web.bind 
	.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind 
	.annotation.RestController; 
import org.springframework.web.servlet 
	.support.ServletUriComponentsBuilder;

import util.StringUtil; 

// Creating the REST controller 
@RestController
@RequestMapping(path = "/scoremaster") 
public class ScoreController { 

	@Autowired
	private ScoreService scoreService; 
	  
	@GetMapping( 
		path = "/scores/{id}", 
		produces = "application/json") 
	
	public Score getScore(@PathVariable Integer id) 
	{ 
		return scoreService 
			.getScoreById(id); 
	} 
		
	@GetMapping( 
		path = "/scores", 
		produces = "application/json") 

	public List<Score> getScoreList(@RequestParam("beforeDate") String beforeDate, @RequestParam("afterDate") String afterDate,
			@RequestParam("players") String players, @RequestParam("pageSize") String pageSize,  @RequestParam("pageId") String pageId) 
	{ 
		List<Score> scoreList = scoreService.getScoreList(beforeDate, afterDate, players); 
		
		if (!StringUtil.isNullOrEmpty(pageSize) && !StringUtil.isNullOrEmpty(pageId)) {
			Integer pageSizeInt = 0;
			Integer pageIdInt = 0;
			try {
				pageSizeInt = Integer.parseInt(pageSize);
				pageIdInt = Integer.parseInt(pageId);
			} catch (NumberFormatException nfe) {
				
			}
			int stIdx = 0;
			if (1 < pageIdInt) {
				stIdx = (pageIdInt - 1) * pageSizeInt;
			}
			
			if (scoreList.size() < stIdx || pageSizeInt == 0 || pageIdInt == 0) {
				scoreList = new ArrayList<Score>();
			} else {
				if (scoreList.size() < (stIdx + pageSizeInt)) {
					scoreList = scoreList.subList(stIdx,stIdx+(stIdx + pageSizeInt - scoreList.size()));
				} else {
					scoreList = scoreList.subList(stIdx,stIdx + pageSizeInt);
				}
			}
		}
		
		return scoreList;
		
	} 

	  
		// Create a POST method 
		// to add an employee 
		// to the list 
	@PutMapping( 
		path = "/enterscore", 
		consumes = "application/json") 

	public Number createScore( 
		@RequestBody Score score) 
	{ 
			Number newId = scoreService 
				.createScore(score);
			
			return newId;

	} 
	

	@PutMapping( 
		path = "/deletescore/{id}", 
		consumes = "application/json") 
	
	public void deleteScore(@PathVariable Integer id) 
	{ 
		scoreService 
			.deleteScore(id); 
	} 
	
	@GetMapping( 
	path = "/playerhistory/{player}", 
	produces = "application/json") 
	public List<Object> getPlayerHistory(@PathVariable String player) {
		return scoreService.getPlayerHistory(player);
	}
 
 	
} 
