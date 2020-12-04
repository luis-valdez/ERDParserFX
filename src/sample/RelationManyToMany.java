package sample;

public class RelationManyToMany {

    String entityName1;
    String entityName2;

    public RelationManyToMany( String entityName1, String entityName2) {
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
}
