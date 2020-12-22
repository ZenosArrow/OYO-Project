package test;
import java.sql.Timestamp;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.springframework.test.util.ReflectionTestUtils;
import org.junit.jupiter.api.Test;

import scoremng.PlayerStats;
import scoremng.Score;
import scoremng.ScoreController;
import scoremng.ScoreService;

@RunWith(TestRunner.class)
@WebMvcTest(ScoreController.class)
public class TestController {
	
	private final String PLAYER_1 = "TestPlayerName";
	
	private final String PLAYER_2 = "TestPlayerName2";
	
	private final String PLAYER_3 = "TestPlayerName3";
	
	private final String PLAYER_1_LOWER = "testplayername";
	
	private final Integer SCORE_1 = 50000;
	private final Integer SCORE_2 = 60000;
	private final Integer SCORE_3 = 70000;
	
	private final String TIMESTAMP_STRING_1 = "2020-12-18 12:00:00.000";
	private final String TIMESTAMP_STRING_2 = "2020-12-19 12:00:00.000";
	private final String TIMESTAMP_STRING_3 = "2020-12-20 12:00:00.000";
	
	private final Timestamp TIMESTAMP_1 = Timestamp.valueOf(TIMESTAMP_STRING_1);
	
	private final Timestamp TIMESTAMP_2 = Timestamp.valueOf(TIMESTAMP_STRING_2);
	
	private final Timestamp TIMESTAMP_3 = Timestamp.valueOf(TIMESTAMP_STRING_3);
	
	@Test
	public void testCreateAndGetScore() {
		ScoreController controller = new ScoreController();
		ReflectionTestUtils.setField(controller, "scoreService", new ScoreService());
		
		Score newScore = new Score(null,PLAYER_1,SCORE_1,TIMESTAMP_1);
		
		Number newId = controller.createScore(newScore);
		
		assertNotNull("CreateScore did not return id",newId);
		
		Integer idInt = newId.intValue();
		
		Score getScore = controller.getScore(idInt);
		
		assertNotNull("Pulled score is null",getScore);
		
		assertEquals(getScore.getPlayer(),newScore.getPlayer(),"Player is not registered player");
		assertEquals(getScore.getScore(),newScore.getScore(),"Score is not registered score");
		assertEquals(getScore.getScoretime(),newScore.getScoretime(),"Scoretime is not registered scoretime");
	}
	
	@Test
	public void testDeleteScore() {
		ScoreController controller = new ScoreController();
		ReflectionTestUtils.setField(controller, "scoreService", new ScoreService());
		
		Score newScore = new Score(null,PLAYER_1,SCORE_1,TIMESTAMP_1);
		
		Number newId = controller.createScore(newScore);
		
		Integer idInt = newId.intValue();
		
		controller.deleteScore(idInt);
		
		Score getScore = controller.getScore(idInt);
		
		assertNull("Score was not deleted",getScore);
	}
	
	@Test
	public void testSearchNoConditions() {
		ScoreController controller = new ScoreController();
		ReflectionTestUtils.setField(controller, "scoreService", new ScoreService());
		
		Score newScore1 = new Score(null,PLAYER_1,SCORE_1,TIMESTAMP_1);
		Score newScore2 = new Score(null,PLAYER_2,SCORE_2,TIMESTAMP_2);
		Score newScore3 = new Score(null,PLAYER_3,SCORE_3,TIMESTAMP_3);
		
		controller.createScore(newScore1);
		controller.createScore(newScore2);
		controller.createScore(newScore3);
		
		List<Score> scoreListNoCond = controller.getScoreList(null,null,null,null,null);
		
		assertEquals(3,scoreListNoCond.size(),"Score list count is different than created count");
		
		assertEquals(scoreListNoCond.get(0).getPlayer(),newScore1.getPlayer(),"Player1 is not registered player");
		assertEquals(scoreListNoCond.get(0).getScore(),newScore1.getScore(),"Score1 is not registered score");
		assertEquals(scoreListNoCond.get(0).getScoretime(),newScore1.getScoretime(),"Scoretime1 is not registered scoretime");
		
		assertEquals(scoreListNoCond.get(1).getPlayer(),newScore2.getPlayer(),"Player2 is not registered player");
		assertEquals(scoreListNoCond.get(1).getScore(),newScore2.getScore(),"Score2 is not registered score");
		assertEquals(scoreListNoCond.get(1).getScoretime(),newScore2.getScoretime(),"Scoretime2 is not registered scoretime");
		
		assertEquals(scoreListNoCond.get(2).getPlayer(),newScore3.getPlayer(),"Player3 is not registered player");
		assertEquals(scoreListNoCond.get(2).getScore(),newScore3.getScore(),"Score3 is not registered score");
		assertEquals(scoreListNoCond.get(2).getScoretime(),newScore3.getScoretime(),"Scoretime3 is not registered scoretime");
	}
	
	@Test
	public void testSearchBeforeDateCondition() {
		ScoreController controller = new ScoreController();
		ReflectionTestUtils.setField(controller, "scoreService", new ScoreService());
		
		Score newScore1 = new Score(null,PLAYER_1,SCORE_1,TIMESTAMP_1);
		Score newScore2 = new Score(null,PLAYER_2,SCORE_2,TIMESTAMP_2);
		Score newScore3 = new Score(null,PLAYER_3,SCORE_3,TIMESTAMP_3);
		
		controller.createScore(newScore1);
		controller.createScore(newScore2);
		controller.createScore(newScore3);
		
		List<Score> scoreListBeforeCond = controller.getScoreList(TIMESTAMP_STRING_2,null,null,null,null);
		
		assertEquals(1,scoreListBeforeCond.size(),"Score list count is different than list of scores with dates before " + TIMESTAMP_STRING_2);
		
		assertTrue("Retrieved data timestamp is after search condition beforeDate",scoreListBeforeCond.get(0).getScoretime().before(TIMESTAMP_2));
	}
	
	@Test
	public void testSearchAfterDateCondition() {
		ScoreController controller = new ScoreController();
		ReflectionTestUtils.setField(controller, "scoreService", new ScoreService());
		
		Score newScore1 = new Score(null,PLAYER_1,SCORE_1,TIMESTAMP_1);
		Score newScore2 = new Score(null,PLAYER_2,SCORE_2,TIMESTAMP_2);
		Score newScore3 = new Score(null,PLAYER_3,SCORE_3,TIMESTAMP_3);
		
		controller.createScore(newScore1);
		controller.createScore(newScore2);
		controller.createScore(newScore3);
		
		List<Score> scoreListAfterCond = controller.getScoreList(null,TIMESTAMP_STRING_2,null,null,null);
		
		assertEquals(1,scoreListAfterCond.size(),"Score list count is different than list of scores with dates after " + TIMESTAMP_STRING_2);
		
		assertTrue("Retrieved data timestamp is before search condition afterDate",scoreListAfterCond.get(0).getScoretime().after(TIMESTAMP_2));
	}
	
	@Test
	public void testSearchUserCondition() {
		ScoreController controller = new ScoreController();
		ReflectionTestUtils.setField(controller, "scoreService", new ScoreService());
		
		Score newScore1 = new Score(null,PLAYER_1,SCORE_1,TIMESTAMP_1);
		Score newScore2 = new Score(null,PLAYER_2,SCORE_2,TIMESTAMP_2);
		Score newScore3 = new Score(null,PLAYER_3,SCORE_3,TIMESTAMP_3);
		
		controller.createScore(newScore1);
		controller.createScore(newScore2);
		controller.createScore(newScore3);
		
		List<Score> scoreListPlayerCond = controller.getScoreList(null,null,PLAYER_1_LOWER + "," + PLAYER_2,null,null);
		
		assertEquals(2,scoreListPlayerCond.size(),"Score list count is different than list of scores with player names " + PLAYER_1 + "," + PLAYER_2);
		
		assertTrue("ScoreList entry 1 player is not within searched names",(scoreListPlayerCond.get(0).getPlayer().equals(PLAYER_1) || scoreListPlayerCond.get(0).getPlayer().equals(PLAYER_2)));
		assertTrue("ScoreList entry 2 player is not within searched names",(scoreListPlayerCond.get(1).getPlayer().equals(PLAYER_1) || scoreListPlayerCond.get(0).getPlayer().equals(PLAYER_2)));
	}
	
	@Test
	public void testSearchAllConditions() {
		ScoreController controller = new ScoreController();
		ReflectionTestUtils.setField(controller, "scoreService", new ScoreService());
		
		Score newScore1 = new Score(null,PLAYER_1,SCORE_1,TIMESTAMP_1);
		Score newScore2 = new Score(null,PLAYER_2,SCORE_2,TIMESTAMP_2);
		Score newScore3 = new Score(null,PLAYER_3,SCORE_3,TIMESTAMP_3);
		
		controller.createScore(newScore1);
		controller.createScore(newScore2);
		controller.createScore(newScore3);
		
		List<Score> scoreListAllCond = controller.getScoreList(TIMESTAMP_STRING_3,TIMESTAMP_STRING_1,PLAYER_2,null,null);
		
		assertEquals(1,scoreListAllCond.size(),"Score list count is different than list of scores with before " + TIMESTAMP_STRING_3 + ", after " + TIMESTAMP_STRING_1 + " with player name " + PLAYER_2;
		
		assertTrue("Retrieved data player is not within searched names",(scoreListAllCond.get(0).getPlayer().equals(PLAYER_2)));
		assertTrue("Retrieved data timestamp is before search condition afterDate",scoreListAllCond.get(0).getScoretime().after(TIMESTAMP_1));
		assertTrue("Retrieved data timestamp is after search condition beforeDate",scoreListAllCond.get(0).getScoretime().before(TIMESTAMP_3));
	}
	
	@Test
	public void testPagingNotNumberConditions() {
		ScoreController controller = new ScoreController();
		ReflectionTestUtils.setField(controller, "scoreService", new ScoreService());
		
		Score newScore1 = new Score(null,PLAYER_1,SCORE_1,TIMESTAMP_1);
		Score newScore2 = new Score(null,PLAYER_2,SCORE_2,TIMESTAMP_2);
		Score newScore3 = new Score(null,PLAYER_3,SCORE_3,TIMESTAMP_3);
		
		controller.createScore(newScore1);
		controller.createScore(newScore2);
		controller.createScore(newScore3);
		
		List<Score> scoreListNotNumCond = controller.getScoreList(null,null,null,"a","a");
		
		assertEquals(3,scoreListNotNumCond.size(),"Score list count is being paged despite invalid page input");
	}
	
	@Test
	public void testPagingNotNumberConditions() {
		ScoreController controller = new ScoreController();
		ReflectionTestUtils.setField(controller, "scoreService", new ScoreService());
		
		Score newScore1 = new Score(null,PLAYER_1,SCORE_1,TIMESTAMP_1);
		Score newScore2 = new Score(null,PLAYER_2,SCORE_2,TIMESTAMP_2);
		Score newScore3 = new Score(null,PLAYER_3,SCORE_3,TIMESTAMP_3);
		
		controller.createScore(newScore1);
		controller.createScore(newScore2);
		controller.createScore(newScore3);
		
		List<Score> scoreListNotNumCond = controller.getScoreList(null,null,null,"a","a");
		
		assertEquals(3,scoreListNotNumCond.size(),"Score list count is being paged despite invalid page input");
	}
	
	@Test
	public void testPagingPageOneConditions() {
		ScoreController controller = new ScoreController();
		ReflectionTestUtils.setField(controller, "scoreService", new ScoreService());
		
		Score newScore1 = new Score(null,PLAYER_1,SCORE_1,TIMESTAMP_1);
		Score newScore2 = new Score(null,PLAYER_2,SCORE_2,TIMESTAMP_2);
		Score newScore3 = new Score(null,PLAYER_3,SCORE_3,TIMESTAMP_3);
		
		controller.createScore(newScore1);
		controller.createScore(newScore2);
		controller.createScore(newScore3);
		
		List<Score> scoreListPageOneCond = controller.getScoreList(null,null,null,"2","1");
		
		assertEquals(2,scoreListPageOneCond.size(),"Score list count is not 2");
	}
	
	@Test
	public void testPagingPageTwoConditions() {
		ScoreController controller = new ScoreController();
		ReflectionTestUtils.setField(controller, "scoreService", new ScoreService());
		
		Score newScore1 = new Score(null,PLAYER_1,SCORE_1,TIMESTAMP_1);
		Score newScore2 = new Score(null,PLAYER_2,SCORE_2,TIMESTAMP_2);
		Score newScore3 = new Score(null,PLAYER_3,SCORE_3,TIMESTAMP_3);
		
		controller.createScore(newScore1);
		controller.createScore(newScore2);
		controller.createScore(newScore3);
		
		List<Score> scoreListPageTwoCond = controller.getScoreList(null,null,null,"2","2");
		
		assertEquals(1,scoreListPageTwoCond.size(),"Score list count is not 1");
	}
	
	@Test
	public void testGetPlayerHistory() {
		ScoreController controller = new ScoreController();
		ReflectionTestUtils.setField(controller, "scoreService", new ScoreService());
		
		Score newScore1 = new Score(null,PLAYER_1,SCORE_1,TIMESTAMP_1);
		Score newScore2 = new Score(null,PLAYER_1_LOWER,SCORE_2,TIMESTAMP_2);
		Score newScore3 = new Score(null,PLAYER_3,SCORE_3,TIMESTAMP_3);
		
		controller.createScore(newScore1);
		controller.createScore(newScore2);
		controller.createScore(newScore3);
		
		List<Object> playerHistoryList = controller.getPlayerHistory(PLAYER_1_LOWER);
		
		boolean hasPlayerStats = false;
		PlayerStats playerStats;
		
		try {
			playerStats = (PlayerStats) playerHistoryList.get(0);
			hasPlayerStats = true;
		} catch (ClassCastException cce) {
		}
		
		assertTrue("Does not contain playerStats",hasPlayerStats);
		
		assertEquals(3,playerHistoryList.size(),"List count is not player stats (1) + score history (2) = 3 lines");
		
		Score getScore1;
		Score getScore2;
		try {
			getScore1 = (Score) playerHistoryList.get(1);
			getScore2 = (Score) playerHistoryList.get(2);
		} catch (ClassCastException cce) {
			getScore1 = new Score(null,"",null,null);
			getScore2 = new Score(null,"",null,null);
		}
		
		assertEquals(PLAYER_1_LOWER,getScore1.getPlayer().toLowerCase(),"Score 1 player is not " + PLAYER_1 + " or " + PLAYER_1_LOWER);
		assertEquals(PLAYER_1_LOWER,getScore2.getPlayer().toLowerCase(),"Score 2 player is not " + PLAYER_1 + " or " + PLAYER_1_LOWER);
		
		Integer max = 0;
		Integer min = 0;
		float avg = 0;
		
		if (getScore1.getScore() < getScore2.getScore()) {
			max = getScore2.getScore();
			min = getScore1.getScore();
		} else if (getScore1.getScore() > getScore2.getScore()) {
			max = getScore1.getScore();
			min = getScore2.getScore();
		} else {
			max = getScore1.getScore();
			min = getScore1.getScore();
		}
		
		avg = (max + min) / 2;
		
		assertEquals(playerStats.getAvg(),avg,"Player stats average not equal to average of two scores");
		assertEquals(playerStats.getMax(),max,"Player stats average not equal to maximum of two scores");
		assertEquals(playerStats.getMin(),min,"Player stats average not equal to minimum of two scores");
		
		
	}
	
	
	
	
	
	
	
	
	
}
