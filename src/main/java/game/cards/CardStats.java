package game.cards;

import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.persistence.Transient;

public interface CardStats {
    float getPowerLevel();
    float getApprovalRating();
    String getDescription();
}
