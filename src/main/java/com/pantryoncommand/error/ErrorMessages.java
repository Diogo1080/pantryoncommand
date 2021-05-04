package com.pantryoncommand.error;

/**
 * Constant Error messages
 */

public class ErrorMessages {
    public static final String USER_NOT_FOUND = "Can't find any user with the given id";
    public static final String USER_ALREADY_EXISTS = "User with the given email already exists";

    public static final String CATEGORY_NOT_FOUND = "Can't find any category with the given id" ;
    public static final String CATEGORY_ALREADY_EXISTS = "Category with the given name already exists";

    public static final String INGREDIENT_NOT_FOUND ="Can't find any ingredient with the given id";
    public static final String INGREDIENT_ALREADY_EXISTS = "Ingredient with the given name already exists";

    public static final String RECIPE_NOT_FOUND = "Can't find any recipe with given constraints";

    public static final String FILE_NOT_FOUND = "Can't find any image with given id";
    public static final String FILE_WAS_NOT_UPLOADED_SUCCESSFULLY = "Image wasn't uploaded successfully";
    public static final String FILE_WAS_NOT_SAVED_SUCCESSFULLY = "Image wasn't saved successfully";
    public static final String FILE_NOT_AN_IMAGE = "File uploaded isn't an image";

    public static final String DATABASE_COMMUNICATION_ERROR = "Database communication error.";
    public static final String READING_FILE_ERROR = "File reading error";

    public static final String WRONG_CREDENTIALS = "The credentials given were wrong";
    public static final String OPERATION_FAILED = "Failed to process the requested operation";
}
