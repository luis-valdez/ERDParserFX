package sample;

public class Relation {
    String entityName1;
    String entityName2;
    boolean isOneToOne;
    boolean isManyToOne;

    public Relation( String entityName1, String entityName2) {
        this.entityName1 = entityName1;
        this.entityName2 = entityName2;
    }

    public String getEntityName1() {
        return entityName1;
    }

    public void setEntityName1(String entityName1) {
        this.entityName1 = entityName1;
    }

    public String getEntityName2() {
        return entityName2;
    }

    public void setEntityName2(String entityName2) {
        this.entityName2 = entityName2;
    }

    public boolean isOneToOne() {
        return isOneToOne;
    }

    public void setOneToOne(boolean oneToOne) {
        isOneToOne = oneToOne;
    }

    public boolean isManyToOne() {
        return isManyToOne;
    }

    public void setManyToOne(boolean manyToOne) {
        isManyToOne = manyToOne;
    }
}
