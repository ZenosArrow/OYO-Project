package scoremng;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Service;

import util.StringUtil;

@Service
public class ScoreService implements IScoreService {

	@Autowired
	private DataSource dataSource;
	private JdbcTemplate jtm;
	private SimpleJdbcInsert sji;
	
	private final String GET_BASIC = "SELECT * FROM Score";
	
	private final String GET_BY_ID = GET_BASIC + " where id = ?";
	
	private final String GET_PLAYER_STATS = "SELECT MAX.score as max, MAX.scoretime as maxTime, MIN.score as min, MIN.scoretime as minTime, AVG.score as avg "
			+ " FROM Score AVG "
			+ " INNER JOIN Score MAX "
			+ " on MAX.player = AVG.player "
			+ " AND MAX.score = "
			+ " (select max(score) from Score where lower(player) = lower(?))"
			+ " INNER JOIN Score MIN "
			+ " on MIN.player = MAX.player "
			+ " and MIN.score = "
			+ " (select min(score) from Score where lower(player) = lower(?))"
			+ " where lower(AVG.player) = lower(?)";
	
	private final String DELETE_BY_ID = "DELETE FROM Score where id = ?";
	
	@PostConstruct
	private void postConstruct() {
		jtm = new JdbcTemplate(dataSource);
		sji = new SimpleJdbcInsert(dataSource).withTableName("Score")
	                                                   .usingGeneratedKeyColumns("id");
	}
	
	@Override
	public Number createScore (Score score) {
		
		Map<String,Object> parameters = new HashMap<String,Object>(3);
		parameters.put("player",score.getPlayer());
		parameters.put("score",score.getScore());
		parameters.put("scoretime",score.getScoretime());
		
		Number newId = sji.executeAndReturnKey(parameters);			
		
		return newId;
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public Score getScoreById (Integer id) {
		Object[] param = {id};
		return jtm.queryForObject(GET_BY_ID,param, new BeanPropertyRowMapper<>(Score.class));
	}
	
	@Override
	public void deleteScore (Integer id) {
		jtm.update(DELETE_BY_ID,id);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public List<Score> getScoreList (String beforeDate, String afterDate, String players) {
		
		StringBuilder sql = new StringBuilder(GET_BASIC);
		StringBuilder whereClause = new StringBuilder("");
		List<String> paramList = new ArrayList<String>();
		boolean hasParam = false;
		
		if (!StringUtil.isNullOrEmpty(beforeDate)) {
			whereClause.append("scoretime < ?");
			paramList.add(beforeDate);
			hasParam = true;
		}
		
		if (!StringUtil.isNullOrEmpty(afterDate)) {
			if (hasParam) {
				whereClause.append(" AND ");
			} else {
				hasParam = true;
			}
			whereClause.append("scoretime > ?");
			paramList.add(afterDate);
		}
		
		if (!StringUtil.isNullOrEmpty(players)) {
			String[] playerArray = players.split(",");
			String inSt = String.join(",", Collections.nCopies(playerArray.length, "lower(?)"));
			if (hasParam) {
				whereClause.append(" AND ");
			} else {
				hasParam = true;
			}
			whereClause.append("lower(player) in (" + inSt + ")");
			paramList.addAll(Arrays.asList(players));
		}
		
		if (hasParam) {
			sql.append(" WHERE ");
			sql.append(whereClause);
			return jtm.query(sql.toString(),paramList.toArray(), new BeanPropertyRowMapper<>(Score.class));
		} else {
			return jtm.query(sql.toString(),new BeanPropertyRowMapper<>(Score.class));
		}
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public List<Object> getPlayerHistory (String player) {
		
		Object[] param = {player};
		List<PlayerStats> playerStats = jtm.query(GET_PLAYER_STATS,param,new BeanPropertyRowMapper<>(PlayerStats.class));
		
		List<Score> scoreList = getScoreList("","",player);
		
		List<Object> resultList = new ArrayList<Object>();
		resultList.add(playerStats);
		resultList.addAll(scoreList);
		return resultList;
	}
}
