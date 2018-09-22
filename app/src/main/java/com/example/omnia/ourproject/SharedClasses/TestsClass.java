package com.example.omnia.ourproject.SharedClasses;

import junit.framework.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 3ZT on 03-Dec-17.
 */

public class TestsClass {

    String TestName;
    String CategoryName;
    boolean IsCategory;

    public String getTestName() {
        return TestName;
    }

    public void setTestName(String testName) {
        TestName = testName;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }



   public TestsClass(String testName,String categoryName,boolean isCategory){
       this.TestName=testName;
       this.CategoryName=categoryName;
       this.IsCategory=isCategory;
   }


}
