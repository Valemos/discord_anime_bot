package game.cards;

import org.jetbrains.annotations.NotNull;

public interface CardStats extends Comparable<CardStats> {
    @Override
    int compareTo(@NotNull CardStats o);
    float getPowerLevel();
    float getApprovalRating();
}
