package agh.edu.pl.weedesign.library.services;

import agh.edu.pl.weedesign.library.entities.category.Category;
import agh.edu.pl.weedesign.library.entities.category.CategoryRepository;
import agh.edu.pl.weedesign.library.entities.reader.Reader;
import agh.edu.pl.weedesign.library.entities.reader.ReaderRepository;

public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public void addNewCategory(Category category) {
        categoryRepository.save(category);
    }

    public Category findCategory(String categoryName) {
        return categoryRepository.findByName(categoryName);
    }

}
