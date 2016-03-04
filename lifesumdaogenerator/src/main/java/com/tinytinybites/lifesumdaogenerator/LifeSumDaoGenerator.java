package com.tinytinybites.lifesumdaogenerator;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.PropertyType;
import de.greenrobot.daogenerator.Schema;

public class LifeSumDaoGenerator {
    public static void main(String args[]) throws Exception {
        Schema schema = new Schema(4, "com.tinytinybites.lifesum.model");

        //Serving category
        Entity scategory = schema.addEntity("ServingCategory");
        scategory.addProperty(PropertyType.Long, "oid").primaryKey();
        scategory.addIntProperty("usemedian");
        scategory.addIntProperty("linearsize");
        scategory.addLongProperty("lastupdated");
        scategory.addLongProperty("created");
        scategory.addStringProperty("name");
        scategory.addIntProperty("defaultsize");
        scategory.addStringProperty("source");
        scategory.implementsSerializable();

        //Serving sizes (FK to serving_category)
        Entity ssize = schema.addEntity("ServingSize");
        ssize.addProperty(PropertyType.Long, "oid").primaryKey();
        ssize.addLongProperty("lastupdated");
        ssize.addLongProperty("created");
        ssize.addStringProperty("name");
        ssize.addDoubleProperty("proportion");
        ssize.addStringProperty("source");
        Property scategoryId = ssize.addLongProperty("servingcategory").getProperty();
        ssize.addToOne(scategory, scategoryId); //FK to serving category
        ssize.implementsSerializable();

        //Food
        Entity food = schema.addEntity("Food");
        food.addProperty(PropertyType.Long, "id").primaryKey();
        food.addStringProperty("title");
        food.addStringProperty("category");
        food.addDoubleProperty("gramsperserving");
        food.addIntProperty("calories");
        food.addIntProperty("measurementid");
        food.addIntProperty("typeofmeasurement");
        food.addIntProperty("showmeasurement");
        food.addDoubleProperty("protein");
        food.addDoubleProperty("carbohydrates");
        food.addDoubleProperty("fiber");
        food.addDoubleProperty("sugar");
        food.addDoubleProperty("fat");
        food.addDoubleProperty("saturatedfat");
        food.addDoubleProperty("unsaturatedfat");
        food.addDoubleProperty("cholesterol");
        food.addDoubleProperty("sodium");
        food.addDoubleProperty("potassium");
        food.addStringProperty("pcstext");
        Property scategoryId2 = food.addLongProperty("servingcategory").getProperty();
        food.addToOne(scategory, scategoryId2); //FK to serving category
        Property servingSizeId = food.addLongProperty("defaultserving").getProperty();
        food.addToOne(ssize, servingSizeId); //FK to serving size
        food.implementsSerializable();

        new DaoGenerator().generateAll(schema, "../app/src/main/java");
    }
}
