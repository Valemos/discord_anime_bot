package game;

public interface DescriptionDisplayable {
    String getId();
    String getName();
    default String getFullName() {return null;}
    default String getAdditionalInfo() {return null;}
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
        String info = getAdditionalInfo();
        if(info != null)    return getIdNameStats() + "\n" + info;
        else                return getIdNameStats();
    }
}
