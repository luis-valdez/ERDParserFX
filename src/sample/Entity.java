package sample;

import javafx.beans.property.SimpleStringProperty;

import java.util.ArrayList;

public class Entity {
    private SimpleStringProperty name = new SimpleStringProperty();
    private ArrayList<Attribute> attributeArrayList;
    private String relation;

    public Entity(SimpleStringProperty name, ArrayList<Attribute> attributeArrayList) {
        this.name = name;
        this.attributeArrayList = attributeArrayList;
    }

    public final String getName(){return name.get();}

    public final void setName(String value){name.set(value);}

    public SimpleStringProperty nameProperty() {return name;}

    public ArrayList<Attribute> getAttributeArrayList() {
        return attributeArrayList;
    }

    public void setAttributeArrayList(ArrayList<Attribute> attributeArrayList) {
        this.attributeArrayList = attributeArrayList;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }
}
