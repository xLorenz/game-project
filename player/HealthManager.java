package player;

public class HealthManager {
    public int health;
    private int maxHealth;

    private double defVulnerabilityTimer;
    private double vulnerabilityTimer = 0;
    public boolean vulnerable = true;

    public HealthManager(int maxHealth, double defVulnerabilityTimer) {
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.defVulnerabilityTimer = defVulnerabilityTimer;
    }

    public void updateTimers(double dt) {
        if (vulnerabilityTimer > 0) {
            vulnerabilityTimer -= dt;
        } else {
            vulnerabilityTimer = 0;
            vulnerable = true;
        }
    }

    public boolean damage(int ammount) {
        if (vulnerable && health > 0) {

            setInvulnerability();

            health -= ammount;
            if (health < 0)
                health = 0;
            return true;
        }
        return false;
    }

    public void setInvulnerability() {
        vulnerable = false;
        vulnerabilityTimer = defVulnerabilityTimer;
    }

    public void setInvulnerability(double time) {
        vulnerable = false;
        vulnerabilityTimer = time;
    }

    public double getPercent() {
        return health / maxHealth;
    }

    public void regenerateHealth() {
        health = maxHealth;
    }

    public void regenerateHealth(int ammount) {
        health += ammount;
        if (health > maxHealth) {
            health = maxHealth;
        }
    }
}
