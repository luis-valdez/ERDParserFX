package sample;

import javafx.beans.property.SimpleStringProperty;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;


public class Controller {


    @FXML
    private CheckBox notNullCheckBox;
    @FXML
    private TextArea resultText;
    @FXML
    private Button nextButton;
    @FXML
    private ComboBox attributeCombo;

    @FXML
    private Label entityLabel;

    @FXML
    private Label attributeLabel;

    private ArrayList<Entity> entityArrayList;

    private String attributeType = "VARCHAR";

    private final String path = "./test2.json";


    @FXML
    private void initialize() {
        String manyToManyQuery = QueryBuilder.createManyToManyTable(createManyToManyRelations(path));
        ArrayList<Relation> relationArrayList = createRelations(path);

        AtomicInteger attributeIndex = new AtomicInteger();
        AtomicInteger entityIndex = new AtomicInteger();

        attributeCombo.setValue("VARCHAR");
        entityArrayList = parseJson(path, attributeType);
        attributeLabel.setText(entityArrayList.get(0).getAttributeArrayList().get(0).getName() + " <- Attribute");
        entityLabel.setText(entityArrayList.get(0).getName() + " <- Entity");

        AtomicReference<Attribute> nextAttribute = new AtomicReference<>();
        AtomicReference<Entity> nextEntity = new AtomicReference<>();

        nextButton.setOnAction(event -> {
                    System.out.println(entityIndex);
                    //Alter objects

                    Entity entity = entityArrayList.get(entityIndex.get());
                    System.out.println(entity.getName());
                    System.out.println(attributeIndex);
                    System.out.println(entity.getAttributeArrayList().get(0).getName());
                    Attribute attribute = entity.getAttributeArrayList().get( attributeIndex.get() );
                    System.out.println(attribute.getName());
                    attribute.setType(attributeCombo.getValue().toString());
                    attribute.setIsNotNull(notNullCheckBox.isSelected());

                    //Define the next attribute
                    nextAttribute.set(getNextAttribute(entityArrayList, entityIndex.get(), attributeIndex.get()));
                    attributeLabel.textProperty().bind(nextAttribute.get().nameProperty().concat(" <- Attribute"));

                    //increase counters
                    attributeIndex.getAndIncrement();

                    if(attributeIndex.get() == entity.getAttributeArrayList().size()){
                        //Change entity label with bind
                        nextEntity.set(getNextEntity(entityArrayList, entityIndex.get()));
                        entityLabel.textProperty().bind(nextEntity.get().nameProperty().concat(" <- Entity"));

                        attributeIndex.set(0);
                        entityIndex.getAndIncrement();

                        QueryBuilder queryBuilder = new QueryBuilder(entity);
                        String table = queryBuilder.createTable(relationArrayList);
                        resultText.setText(resultText.getText() + "\n\n" + table);
                    }
                    //reinitialize counters if both lists have been iterated
                    if(entityIndex.get() == entityArrayList.size()){
                        entityIndex.set(0);
                        attributeIndex.set(0);
                        resultText.setText(resultText.getText() +"\n\n" + manyToManyQuery);
                    }

                });

    }


    public static JSONObject getJsonFromFile(File inputSource){
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader(inputSource));
            return (JSONObject) obj;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<Entity> parseJson (String path, String attributeType){
        // read
        File file = new File(path);
        JSONObject ERD_JSON = getJsonFromFile(file);
        JSONArray entities = (JSONArray) ERD_JSON.get("entidades");

        //iterate over entities
        ArrayList<Entity> entityArrayList = new ArrayList<>();
        ArrayList<Attribute> attributeArrayList = new ArrayList<>();
        for (Object entity: entities) {
            JSONObject JSONentity = (JSONObject) entity ;
            JSONArray JSONArrayAttributes = (JSONArray) JSONentity.get("atributos");
            String entityName = JSONentity.get("nombre").toString();

            //iterate over attributes
            for(Object attribute: JSONArrayAttributes){

                JSONObject JSONAttribute = (JSONObject) attribute;
                String strAttributeName = JSONAttribute.get("nombre").toString();
                System.out.println(strAttributeName);
                attributeArrayList.add(new Attribute(new SimpleStringProperty(strAttributeName), attributeType));

            }
            SimpleStringProperty stringProperty = new SimpleStringProperty(entityName);
            entityArrayList.add(new Entity(stringProperty, (ArrayList<Attribute>) attributeArrayList.clone()));
            attributeArrayList.clear();

        }

        return entityArrayList;
    }
    public ArrayList<Relation> createRelations(String path){
        ArrayList<Relation> relationsArray = new ArrayList<>();
        // read
        File file = new File(path);
        JSONObject ERD_JSON = getJsonFromFile(file);
        JSONArray relations = (JSONArray) ERD_JSON.get("relaciones");

        //iterate over relations
        for (Object relation: relations) {
            JSONObject JSONrelation = (JSONObject) relation ;
            JSONArray cardinalities = (JSONArray) JSONrelation.get("cardinalidades");
            JSONObject cardinality1 = (JSONObject) cardinalities.get(0);
            JSONObject cardinality2 = (JSONObject) cardinalities.get(1);

            //check if the relation is one to one
            if(cardinality1.get("max").toString().equals("1") && cardinality2.get("max").toString().equals("1")) {
                Relation r = new Relation(cardinality1.get("entidad").toString(), cardinality2.get("entidad").toString());
                r.setOneToOne(true);
                relationsArray.add(r);
            }

            //check if the relation is many to one
        }
        return relationsArray;
    }

    public ArrayList<RelationManyToMany> createManyToManyRelations(String path){
        ArrayList<RelationManyToMany> manyToManyArray = new ArrayList<>();
        // read
        File file = new File(path);
        JSONObject ERD_JSON = getJsonFromFile(file);
        JSONArray relations = (JSONArray) ERD_JSON.get("relaciones");

        //iterate over relations
        for (Object relation: relations) {
            JSONObject JSONrelation = (JSONObject) relation ;
            JSONArray cardinalities = (JSONArray) JSONrelation.get("cardinalidades");
            JSONObject cardinality1 = (JSONObject) cardinalities.get(0);
            JSONObject cardinality2 = (JSONObject) cardinalities.get(1);
            //check if the relation is many to many
            if(cardinality1.get("max").toString().equals("n") && cardinality2.get("max").toString().equals("n")){
                String entity1 = cardinality1.get("entidad").toString();
                String entity2 = cardinality2.get("entidad").toString();
                manyToManyArray.add(new RelationManyToMany(entity1, entity2));
            }
        }
        return manyToManyArray;
    }

    public Attribute getNextAttribute(ArrayList<Entity> entityArrayList, int currentEntityIndex, int currentAttributeIndex) {
        ArrayList<Attribute> currentAttributeList = entityArrayList.get(currentEntityIndex).getAttributeArrayList();
        Attribute nextAttribute;
        //Check if the current entity is the last one and also the current attribute is the last one
        if(entityArrayList.size() == currentEntityIndex + 1 && currentAttributeList.size() == currentAttributeIndex + 1){
            Entity entity = entityArrayList.get(0);
            nextAttribute = entity.getAttributeArrayList().get(0);
        }
        // Check if the current attribute list is at the last element
        else if (currentAttributeList.size() == currentAttributeIndex + 1){
            Entity entity = entityArrayList.get(currentEntityIndex + 1);
            nextAttribute= entity.getAttributeArrayList().get(0);
        } else {
            nextAttribute = currentAttributeList.get( currentAttributeIndex + 1 );
        }
        return nextAttribute;
    }

    public Entity getNextEntity(ArrayList<Entity> entityArrayList, int currentEntityIndex) {
        Entity entity;
        if(entityArrayList.size() == currentEntityIndex + 1) {
            entity = entityArrayList.get(0);
        }
        else entity = entityArrayList.get(currentEntityIndex + 1);
        return entity;
    }
}
