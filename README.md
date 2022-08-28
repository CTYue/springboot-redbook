In this branch, you need to learn JdbcTemplate; 
* in package dao, write the CRUD method using jdbcTemplate.

## Reference
```java
/**
 * GET/SELECT
 */
public List<Player> getAllPlayers() {
    String sql = "SELECT * FROM PLAYER";
    return jdbcTemplate.query(sql, new BeanPropertyRowMapper<Player> (Player.class));
}

public Player getPlayerById(int id) {
    String sql = "SELECT * FROM PLAYER WHERE ID = ?";
    return jdbcTemplate.queryForObject(sql, 
                                       new BeanPropertyRowMapper<Player>(Player.class), 
                                       new Object[] {id});
}

/**
 * POST/Insert
 */
public int insertPlayer(Player player)
{
    String sql = "INSERT INTO PLAYER (ID, Name, Nationality,Birth_date, Titles) " + 
                                                                  "VALUES (?, ?, ?, ?, ?)";
    return jdbcTemplate.update( sql, new Object[] 
                               { player.getId(), player.getName(), player.getNationality(), 
                                 new Timestamp(player.getBirthDate().getTime()), 
                                 player.getTitles()  
                               });
}

/**
 * UPDATE
 */
public int updatePlayer(Player player){
    String sql = "UPDATE PLAYER " +
                 "SET Name = ?, Nationality = ?, Birth_date = ? , Titles = ? " +
                 "WHERE ID = ?";

    return jdbcTemplate.update( sql, new Object[] { 
                                   player.getName(), 
                                   player.getNationality(), 
                                   new Timestamp(player.getBirthDate().getTime()), 
                                   player.getTitles(), 
                                   player.getId() }
                              );
}

/**
 * DELETE
 */
public int deletePlayerById(int id) {
    String sql="DELETE FROM PLAYER WHERE ID = ?";
    return jdbcTemplate.update(sql, new Object[] {id});
}

/**
 * DDL
 */
public void createTournamentTable() {
    String sql = "CREATE TABLE TOURNAMENT (ID INTEGER, NAME VARCHAR(50), 
                                           LOCATION VARCHAR(50), PRIMARY KEY (ID))";
    jdbcTemplate.execute(sql);
    System.out.println("Table created");
}
```