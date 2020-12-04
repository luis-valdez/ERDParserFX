package sample;

import java.util.ArrayList;

public class QueryBuilder {
    Entity entity;
    //ArrayList<Relation> relationArrayList;

    public QueryBuilder(Entity entity) {
        this.entity = entity;
    }

    public String createTable(ArrayList<Relation> relationArrayList){
        StringBuilder sb = new StringBuilder( String.format("CREATE TABLE IF NOT EXISTS %s ( \n", entity.getName()));
        sb.append("ID INT PRIMARY KEY NOT NULL, \n");
        int i = 1;
        //Add one to one relations
        for (Relation r: relationArrayList) {
            if(r.getEntityName1().equals(entity.getName())){
                sb.append(String.format("%s_id INT NOT NULL REFERENCES %s(%s_id), \n", r.entityName2, r.entityName2, r.entityName2));
                break;
            }
        }
        for (Attribute attribute : entity.getAttributeArrayList()) {


            //attribute type
            switch(attribute.getType()) {
                case "VARCHAR":
                    sb.append(String.format("%s %s (%d)", attribute.getName(),  attribute.getType(), 100));
                    break;
                case "INT":
                case "BOOLEAN":
                case "DATETIME":
                    sb.append(String.format("%s %s", attribute.getName(), attribute.getType()));
                    break;
            }
            //check if not null property is true
            if(attribute.getIsNotNull()) sb.append(" NOT NULL");
            if(i != entity.getAttributeArrayList().size()) sb.append(",\n");
            else sb.append("\n");
            i++;


        }
        sb.append(");");
        return sb.toString();
    }

    public static String createManyToManyTable(ArrayList<RelationManyToMany> relationArray) {
        StringBuilder sb = new StringBuilder("-- MANY TO MANY RELATIONS \n");
        for (RelationManyToMany relation: relationArray) {

            String entity1 = relation.getEntityName1();
            String entity2 = relation.getEntityName2();
            sb.append(String.format("CREATE TABLE IF NOT EXISTS %s_%s ( \n", entity1, entity2 ));
            sb.append(String.format("%s_id integer references %s(id), \n", entity1, entity1));
            sb.append(String.format("%s_id integer references %s(id), \n", entity2, entity2));
            sb.append(String.format("PRIMARY KEY(%s_id, %s_id)\n", entity1, entity2));
            sb.append(");\n");
        }

        return sb.toString();
    }
}
