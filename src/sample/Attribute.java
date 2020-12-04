package sample;

import javafx.beans.property.SimpleStringProperty;

public class Attribute {
    private SimpleStringProperty name = new SimpleStringProperty();
    private String type;
    private boolean isUnique;
    private boolean isNotNull;

    public Attribute(SimpleStringProperty name, String type) {
        this.name = name;
        this.type = type;
    }

    public final String getName(){return name.get();}

    public final void setName(String value){name.set(value);}

    public SimpleStringProperty nameProperty() {return name;}

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean getIsNotNull(){
        return isNotNull;
    }
    public void setIsNotNull(boolean isNotNull){
        this.isNotNull = isNotNull;
    }
}
