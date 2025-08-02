package rougelike.game.entities;

public class InteractionResult {
    private Entity entity;
    private Entity source;
    private boolean gameWon;
    private boolean levelUp;
    private boolean heal;
    private boolean takeDamage;

    public InteractionResult() {
        // Set all interaction results to false by default
    };

    public InteractionResult(InteractionResultType[] results, Entity entity) {
        this.entity = entity;
        for (InteractionResultType result : results) {
            switch (result) {
                case GAME_WON:
                    this.gameWon = true;
                    break;
                case LEVEL_UP:
                    this.levelUp = true;
                    break;
                case HEAL:
                    this.heal = true;
                    break;
                case TAKE_DAMAGE:
                    this.takeDamage = true;
                    break;
            }
        }
    }

    public InteractionResult(InteractionResultType[] results, Entity entity, Entity source) {
        this.entity = entity;
        this.source = source;
        for (InteractionResultType result : results) {
            switch (result) {
                case GAME_WON:
                    this.gameWon = true;
                    break;
                case LEVEL_UP:
                    this.levelUp = true;
                    break;
                case HEAL:
                    this.heal = true;
                    break;
                case TAKE_DAMAGE:
                    this.takeDamage = true;
                    break;
            }
        }
    }

    public Entity getEntity() {
        return entity;
    }

    public boolean getGameWon() {
        return gameWon;
    }

    public boolean getLevelUp() {
        return levelUp;
    }

    public boolean getHeal() {
        return heal;
    }

    public boolean getTakeDamage() {
        return takeDamage;
    }

    public Entity getSource() {
        return source;
    }
}
