package com.kenzie.capstone.service.dependency;

import com.kenzie.capstone.service.LambdaRecipeService;
import com.kenzie.capstone.service.LambdaService;
import com.kenzie.capstone.service.dao.ExampleDao;

import com.kenzie.capstone.service.dao.RecipeDao;
import dagger.Module;
import dagger.Provides;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Module(
    includes = DaoModule.class
)
public class ServiceModule {

    @Singleton
    @Provides
    @Inject
    public LambdaService provideLambdaService(@Named("ExampleDao") ExampleDao exampleDao) {
        return new LambdaService(exampleDao);
    }

    @Singleton
    @Provides
    @Inject
    public LambdaRecipeService provideLambdaRecipeService(@Named("RecipeDao") RecipeDao recipeDao) {
        return new LambdaRecipeService(recipeDao);
    }
}

