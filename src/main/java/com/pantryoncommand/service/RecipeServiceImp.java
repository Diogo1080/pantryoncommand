package com.pantryoncommand.service;

import com.pantryoncommand.command.Image;
import com.pantryoncommand.command.Paginated;
import com.pantryoncommand.command.ingredient.IngredientDetailsDto;
import com.pantryoncommand.command.recipe.CreateRecipeDto;
import com.pantryoncommand.command.recipe.UpdateRecipeDto;
import com.pantryoncommand.command.recipe.RecipeDetailsDto;
import com.pantryoncommand.converters.IngredientConverter;
import com.pantryoncommand.converters.RecipeConverter;
import com.pantryoncommand.error.ErrorMessages;
import com.pantryoncommand.exeption.DatabaseCommunicationException;
import com.pantryoncommand.exeption.file.FileNotAnImageException;
import com.pantryoncommand.exeption.file.FileNotFoundException;
import com.pantryoncommand.exeption.file.FileNotSavedException;
import com.pantryoncommand.exeption.file.ReadingFileException;
import com.pantryoncommand.exeption.ingredient.IngredientNotFoundException;
import com.pantryoncommand.exeption.recipe.RecipeNotFoundException;
import com.pantryoncommand.exeption.user.UserNotFoundException;
import com.pantryoncommand.persistence.entity.IngredientEntity;
import com.pantryoncommand.persistence.entity.RecipeEntity;
import com.pantryoncommand.persistence.entity.TempIngredientIdEntity;
import com.pantryoncommand.persistence.entity.UserEntity;
import com.pantryoncommand.persistence.repository.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tika.Tika;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * An {@link IngredientService} implementation
 */
@Service
public class RecipeServiceImp implements RecipeService {

    private final IngredientRepository ingredientRepository;
    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;
    private final FileSystemRepository fileSystemRepository;
    private final TempIngredientIdRepository tempIngredientIdRepositoryRepository;

    private static final Logger LOGGER = LogManager.getLogger(RecipeServiceImp.class);

    public RecipeServiceImp(IngredientRepository ingredientRepository,
                            RecipeRepository recipeRepository,
                            UserRepository userRepository,
                            FileSystemRepository fileSystemRepository,
                            TempIngredientIdRepository tempIngredientIdRepositoryRepository) {
        this.ingredientRepository = ingredientRepository;
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
        this.fileSystemRepository = fileSystemRepository;
        this.tempIngredientIdRepositoryRepository = tempIngredientIdRepositoryRepository;
    }


    /**
     * @see RecipeService#addNewRecipe(CreateRecipeDto)
     */
    public RecipeDetailsDto addNewRecipe(CreateRecipeDto createRecipeDto)
            throws UserNotFoundException,
            IngredientNotFoundException,
            DatabaseCommunicationException {

        // get user entity
        LOGGER.debug("Getting user with id {}", createRecipeDto.getUserId());
        UserEntity userEntity = userRepository.findById(createRecipeDto.getUserId())
                .orElseThrow(() -> {
                    LOGGER.error(ErrorMessages.USER_NOT_FOUND);
                    return new UserNotFoundException(ErrorMessages.USER_NOT_FOUND);
                });

        // get Ingredient entities
        List<IngredientEntity> ingredientList = new LinkedList<>();
        for (long id : createRecipeDto.getIngredientIds()) {
            LOGGER.debug("Getting ingredient with id {}", id);
            ingredientList.add(ingredientRepository.findById(id)
                    .orElseThrow(() -> {
                        LOGGER.error(ErrorMessages.INGREDIENT_NOT_FOUND);
                        return new IngredientNotFoundException(ErrorMessages.INGREDIENT_NOT_FOUND);
                    })
            );
        }

        // Build recipe Entity
        RecipeEntity recipeEntity = RecipeConverter.fromCreateRecipeDtoToRecipeEntity(createRecipeDto, userEntity);
        recipeEntity.setIngredients(ingredientList);
        recipeEntity.setUserEntity(userEntity);

        // Persist recipe into database
        try {
            LOGGER.debug("Saving recipe onto database");
            recipeRepository.save(recipeEntity);

        } catch (Exception e) {
            LOGGER.error("Failed while saving recipe into database - {}", recipeEntity);
            throw new DatabaseCommunicationException(ErrorMessages.DATABASE_COMMUNICATION_ERROR);

        }

        //Convert recipe to return to user
        return RecipeConverter.fromRecipeEntityToRecipeDetailsDto(recipeEntity, handleMultipleIngredients(recipeEntity));
    }

    /**
     * @see RecipeService#addImageToRecipe(MultipartFile, long)
     */
    @Override
    public RecipeDetailsDto addImageToRecipe(MultipartFile multipartImage, long recipeId) throws
            RecipeNotFoundException,
            FileNotSavedException {

        // get recipe entity
        LOGGER.debug("Getting recipe with id {}", recipeId);
        RecipeEntity recipeEntity = recipeRepository.findById(recipeId)
                .orElseThrow(() -> {
                    LOGGER.error(ErrorMessages.RECIPE_NOT_FOUND);
                    return new RecipeNotFoundException(ErrorMessages.RECIPE_NOT_FOUND);
                });

        //Check if file uploaded is image
        byte[] imageBytes;

        try {
            LOGGER.debug("Getting file bytes from file - {} - with not checked contentType - {}",
                    multipartImage.getOriginalFilename(),
                    multipartImage.getContentType());
            imageBytes = multipartImage.getBytes();

        } catch (IOException e) {
            LOGGER.error(ErrorMessages.READING_FILE_ERROR, e);
            throw new ReadingFileException(ErrorMessages.READING_FILE_ERROR, e);

        }

        Tika tika = new Tika();
        String detectedType = tika.detect(imageBytes);
        LOGGER.debug("Checking if detectedType is image - {}", detectedType);
        if (!detectedType.startsWith("image/")) {
            LOGGER.error(ErrorMessages.FILE_NOT_AN_IMAGE);
            throw new FileNotAnImageException(ErrorMessages.FILE_NOT_AN_IMAGE);

        }

        // Build Image
        Image image = new Image();
        image.setName(multipartImage.getOriginalFilename());
        image.setContent(imageBytes);


        //Persisting image on fileSystem
        try {
            LOGGER.debug("Saving image on fileSystem");
            recipeEntity.setPhoto(fileSystemRepository.save(image));

        } catch (Exception e) {
            LOGGER.error("Failed while saving image into fileSystem - {} - exception", image, e);
            throw new FileNotSavedException(ErrorMessages.FILE_WAS_NOT_SAVED_SUCCESSFULLY, e);

        }

        //Persisting image path on database
        try {
            LOGGER.debug("Saving image path on database");
            recipeRepository.save(recipeEntity);

        } catch (Exception e) {
            LOGGER.error("Failed while saving image into database - {}", recipeEntity);
            throw new FileNotSavedException(ErrorMessages.FILE_WAS_NOT_SAVED_SUCCESSFULLY);

        }

        // Convert recipe to return to user
        return RecipeConverter.fromRecipeEntityToRecipeDetailsDto(recipeEntity, handleMultipleIngredients(recipeEntity));
    }

    /**
     * @see RecipeService#getRecipeById(long)
     */
    @Override
    public RecipeDetailsDto getRecipeById(long recipeId) throws
            RecipeNotFoundException {
        // get recipe entity
        LOGGER.debug("Getting recipe with id {}", recipeId);
        RecipeEntity recipeEntity = recipeRepository.findById(recipeId)
                .orElseThrow(() -> {
                    LOGGER.error(ErrorMessages.RECIPE_NOT_FOUND);
                    return new RecipeNotFoundException(ErrorMessages.RECIPE_NOT_FOUND);
                });

        // Convert recipe to return to user
        return RecipeConverter.fromRecipeEntityToRecipeDetailsDto(recipeEntity, handleMultipleIngredients(recipeEntity));
    }

    /**
     * @see RecipeService#getRecipeImageById(long)
     */
    @Override
    public FileSystemResource getRecipeImageById(long recipeId) throws
            RecipeNotFoundException,
            FileNotFoundException {

        // get recipe entity
        LOGGER.debug("Getting recipe with id {}", recipeId);
        RecipeEntity recipeEntity = recipeRepository.findById(recipeId)
                .orElseThrow(() -> {
                    LOGGER.error(ErrorMessages.RECIPE_NOT_FOUND);
                    return new RecipeNotFoundException(ErrorMessages.RECIPE_NOT_FOUND);
                });

        //Retrieving image from fileSystem
        try {
            LOGGER.debug("Retrieving image from fileSystem with path - {}", recipeEntity.getPhoto());
            return fileSystemRepository.findInFileSystem(recipeEntity.getPhoto());

        } catch (Exception e) {
            LOGGER.error(ErrorMessages.FILE_NOT_FOUND);
            throw new FileNotFoundException(ErrorMessages.FILE_NOT_FOUND);

        }

    }

    /**
     * @see RecipeService#getRecipeList(List, PageRequest)
     */
    @Override
    public Paginated<RecipeDetailsDto> getRecipeList(List<Long> ingredients, PageRequest pagination) {

        // Setting up temp table
        LOGGER.debug("Setting all temp ingredients - {}", ingredients);
        for (long ingredient:ingredients) {
            TempIngredientIdEntity tempIngredient=TempIngredientIdEntity.builder().entityId(ingredient).build();
            tempIngredientIdRepositoryRepository.save(tempIngredient);
        }

        // Get recipes with pagination
        LOGGER.debug("Getting all recipes with pagination - {}", pagination);
        Page<RecipeEntity> recipeList = recipeRepository.findAllByIngredientId(pagination);

        // Deleting Temp
        for (long ingredient:ingredients) {
            TempIngredientIdEntity tempIngredient=TempIngredientIdEntity.builder().entityId(ingredient).build();
            tempIngredientIdRepositoryRepository.delete(tempIngredient);
        }

        // Convert list items from RecipeEntity to RecipeDetailsDto
        List<RecipeDetailsDto> recipeListResponse = new ArrayList<>();
        for (RecipeEntity recipeEntity : recipeList.getContent()) {
            recipeListResponse.add(RecipeConverter.fromRecipeEntityToRecipeDetailsDto(recipeEntity, handleMultipleIngredients(recipeEntity)));
        }

        //Build paginated
        Paginated<RecipeDetailsDto> paginated = new Paginated<>(
                recipeListResponse,
                recipeListResponse.size(),
                pagination.getPageNumber(),
                recipeList.getTotalPages(),
                recipeList.getTotalElements()
        );

        //Return paginated
        return paginated;
    }

    /**
     * @see RecipeService#updateRecipeById(long, long, UpdateRecipeDto)
     */
    @Override
    public RecipeDetailsDto updateRecipeById(long recipeId, long userId, UpdateRecipeDto updateRecipe) throws
            DatabaseCommunicationException,
            IngredientNotFoundException,
            UserNotFoundException,
            RecipeNotFoundException {

        // check if user exists
        LOGGER.debug("Getting user with id {}", userId);
        userRepository.findById(userId)
                .orElseThrow(() -> {
                    LOGGER.error(ErrorMessages.USER_NOT_FOUND);
                    return new UserNotFoundException(ErrorMessages.USER_NOT_FOUND);
                });

        // get recipe entity
        LOGGER.debug("Getting recipe with id {}", recipeId);
        RecipeEntity recipeEntity = recipeRepository.findById(recipeId)
                .orElseThrow(() -> {
                    LOGGER.error(ErrorMessages.RECIPE_NOT_FOUND);
                    return new RecipeNotFoundException(ErrorMessages.RECIPE_NOT_FOUND);
                });

        // get Ingredient entities
        List<IngredientEntity> ingredientList = new LinkedList<>();
        for (long id : updateRecipe.getIngredientIds()) {
            LOGGER.debug("Getting ingredient with id {}", id);
            ingredientList.add(ingredientRepository.findById(id)
                    .orElseThrow(() -> {
                        LOGGER.error(ErrorMessages.INGREDIENT_NOT_FOUND);
                        return new IngredientNotFoundException(ErrorMessages.INGREDIENT_NOT_FOUND);
                    })
            );
        }

        //Build recipe
        recipeEntity.setIngredients(ingredientList);
        recipeEntity.setName(updateRecipe.getName());
        recipeEntity.setPrepTime(updateRecipe.getPrepTime());
        recipeEntity.setSteps(updateRecipe.getSteps());

        // Save changes
        try {
            LOGGER.debug("Saving updated recipe into database");
            recipeRepository.save(recipeEntity);

        } catch (Exception e) {
            LOGGER.error("Failed while updating recipe into database - {} - with exception - {}", recipeEntity, e);
            throw new DatabaseCommunicationException(ErrorMessages.DATABASE_COMMUNICATION_ERROR, e);

        }

        // Convert to recipeDetails and return updated recipe
        return RecipeConverter.fromRecipeEntityToRecipeDetailsDto(recipeEntity, handleMultipleIngredients(recipeEntity));
    }

    /**
     * @see RecipeService#updateImageToRecipe(MultipartFile, long, long)
     */
    @Override
    public RecipeDetailsDto updateImageToRecipe(MultipartFile multipartImage,long userId, long recipeId) throws
            UserNotFoundException,
            RecipeNotFoundException,
            ReadingFileException,
            FileNotAnImageException,
            FileNotFoundException,
            FileNotSavedException {

        // check if user exists
        LOGGER.debug("Getting user with id {}", userId);
        userRepository.findById(userId)
                .orElseThrow(() -> {
                    LOGGER.error(ErrorMessages.USER_NOT_FOUND);
                    return new UserNotFoundException(ErrorMessages.USER_NOT_FOUND);
                });

        // check if recipe exists
        LOGGER.debug("Getting recipe with id {}", recipeId);
        RecipeEntity recipeEntity = recipeRepository.findById(recipeId)
                .orElseThrow(() -> {
                    LOGGER.error(ErrorMessages.RECIPE_NOT_FOUND);
                    return new RecipeNotFoundException(ErrorMessages.RECIPE_NOT_FOUND);
                });

        //Check if file uploaded is image
        byte[] imageBytes;

        try {
            LOGGER.debug("Getting file bytes from file - {} - with not checked contentType - {}",
                    multipartImage.getOriginalFilename(),
                    multipartImage.getContentType());
            imageBytes = multipartImage.getBytes();

        } catch (IOException e) {
            LOGGER.error(ErrorMessages.READING_FILE_ERROR, e);
            throw new ReadingFileException(ErrorMessages.READING_FILE_ERROR, e);

        }

        Tika tika = new Tika();
        String detectedType = tika.detect(imageBytes);
        LOGGER.debug("Checking if detectedType is image - {}", detectedType);
        if (!detectedType.startsWith("image/")) {
            LOGGER.error(ErrorMessages.FILE_NOT_AN_IMAGE);
            throw new FileNotAnImageException(ErrorMessages.FILE_NOT_AN_IMAGE);

        }

        // Build Image
        Image image = new Image();
        image.setName(multipartImage.getOriginalFilename());
        image.setContent(imageBytes);

        // delete file from fileSystem
        try {
            LOGGER.debug("Deleting image from fileSystem - {}", recipeEntity.getPhoto());
            fileSystemRepository.deleteFileFromSystem(recipeEntity.getPhoto());
        } catch (Exception e) {
            LOGGER.error("Failed while deleting file from fileSystem", e);
            throw new FileNotFoundException(ErrorMessages.FILE_NOT_FOUND, e);
        }

        //Persisting image on fileSystem
        try {
            LOGGER.debug("Saving image on fileSystem");
            recipeEntity.setPhoto(fileSystemRepository.save(image));

        } catch (Exception e) {
            LOGGER.error("Failed while saving image into fileSystem - {} - exception", image, e);
            throw new FileNotSavedException(ErrorMessages.FILE_WAS_NOT_SAVED_SUCCESSFULLY, e);

        }

        //Persisting image path on database
        try {
            LOGGER.debug("Saving image path on database");
            recipeRepository.save(recipeEntity);

        } catch (Exception e) {
            LOGGER.error("Failed while saving image into database - {}", recipeEntity);
            throw new FileNotSavedException(ErrorMessages.FILE_WAS_NOT_SAVED_SUCCESSFULLY);

        }

        // Convert recipe to return to user
        return RecipeConverter.fromRecipeEntityToRecipeDetailsDto(recipeEntity, handleMultipleIngredients(recipeEntity));
    }

    /**
     * @see RecipeService#deleteRecipe(long)
     */
    @Override
    public void deleteRecipe(long recipeId) throws
            RecipeNotFoundException,
            FileNotFoundException,
            DatabaseCommunicationException {

        // check if recipe exists
        LOGGER.debug("Getting recipe with id {}", recipeId);
        RecipeEntity recipeEntity = recipeRepository.findById(recipeId)
                .orElseThrow(() -> {
                    LOGGER.error(ErrorMessages.RECIPE_NOT_FOUND);
                    return new RecipeNotFoundException(ErrorMessages.RECIPE_NOT_FOUND);
                });

        // delete file from fileSystem
        if (!recipeEntity.getPhoto().isEmpty()) {
            try {
                LOGGER.debug("Deleting image from fileSystem - {}", recipeEntity.getPhoto());
                fileSystemRepository.deleteFileFromSystem(recipeEntity.getPhoto());
            } catch (Exception e) {
                LOGGER.error("Failed while deleting file from fileSystem", e);
                throw new FileNotFoundException(ErrorMessages.FILE_NOT_FOUND, e);
            }
        }

        // Delete recipe
        try {
            LOGGER.debug("Deleting recipe from database");
            recipeRepository.delete(recipeEntity);

        } catch (Exception e) {
            LOGGER.error("Failed while deleting recipe from database - {} - with exception - {}", recipeEntity, e);
            throw new DatabaseCommunicationException(ErrorMessages.DATABASE_COMMUNICATION_ERROR, e);

        }

    }

    /**
     * Handles multiple ingredients
     *
     * @param recipeEntity {@link RecipeEntity}
     * @return a list of {@link IngredientDetailsDto}
     */
    private List<IngredientDetailsDto> handleMultipleIngredients(RecipeEntity recipeEntity) {
        List<IngredientDetailsDto> ingredientDetailsList = new ArrayList<>();

        for (IngredientEntity ingredient : recipeEntity.getIngredients()) {
            ingredientDetailsList.add(IngredientConverter.fromIngredientEntityToIngredientDetailsDto(ingredient));
        }

        return ingredientDetailsList;
    }


}
