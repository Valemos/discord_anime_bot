package game;

public interface DescriptionDisplayable {
    String getId();
    String getName();
    default String getFullName() {return null;}
    default String getDescription() {return null;}
    String getStatsString();

    default String getNameStats(){
        String fullName = getFullName();
        return (fullName == null ? getName() : fullName) + ": " + getStatsString();
    }

    default String getFormattedId(){
        return "`" + getId() + "`";
    }

    default String getIdName(){
        return getFormattedId() + " - " + getName();
    }

    default String getIdNameStats(){
        return getFormattedId() + " - " + getNameStats();
    }

    default String getFullDescription() {
        String desc = getDescription();
        if(desc != null)    return getIdNameStats() + "\n" + desc;
        else                return getIdNameStats();
    }
}
