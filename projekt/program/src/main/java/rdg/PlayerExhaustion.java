package rdg;
/**
 *
 * @author Rastislav Urbanek
 */
public class PlayerExhaustion {
    private int accountId;
    private int kills;
    private int bestDay;
    private int bestHour;
    private boolean percentil90;

    public int getAccountId() { return accountId; }
    public void setAccountId(int accountId) { this.accountId = accountId; }
    public int getKills() { return kills; }
    public void setKills(int kills) { this.kills = kills;}
    public int getBestDay() { return bestDay; }
    public void setBestDay(int bestDay) { this.bestDay = bestDay; }
    public int getBestHour() { return bestHour; }
    public void setBestHour(int bestHour) { this.bestHour = bestHour; }
    public boolean isPercentil90() { return percentil90; }
    public void setPercentil90(boolean percentil90) { this.percentil90 = percentil90; }


}
