package cr.ac.ucr.ecci.cql.campus20.ucr_eats.presenters;

import android.content.Context;

import com.squareup.picasso.Picasso;

import java.util.List;

import cr.ac.ucr.ecci.cql.campus20.ucr_eats.models.Meal;

/**
 * Presenter class for the Meals Activity
 */
public class MealsPresenter
{
    // Actual meal data
    private List<Meal> meals;

    public MealsPresenter(List<Meal> meals)
    {
        this.setMeals(meals);
    }

    public void setMeals(List<Meal> meals)
    {
        this.meals = meals;
    }

    public void onBindMealItemAtPosition(int position, MealsItemView mealView)
    {
        if(this.meals != null && this.meals.size() > 0)
        {
            Meal meal = this.meals.get(position);

            mealView.setName(meal.getName());
            mealView.setPrice(meal.getPrice());
            mealView.setServings(meal.getAvailableServings(), meal.getMaxServings());
            mealView.setPicture(meal.getPhoto());
        }
    }

}
