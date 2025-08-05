package com.example.recetas_2

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.recetas_2.data.ApiServiceImpl
import com.example.recetas_2.data.MealApiService
import com.example.recetas_2.data.MealRepository
import com.example.recetas_2.data.MealResponse
import com.example.recetas_2.data.model.Meal
import com.example.recetas_2.presentation.MainViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    lateinit var mockMealApiService: MealApiService

    private lateinit var viewModel: MainViewModel
    private val capturedMeals = mutableListOf<Meal>()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        val apiService = ApiServiceImpl(mockMealApiService)
        val repository = MealRepository(apiService)
        viewModel = MainViewModel(repository)

        // Registrar observer ANTES de ejecutar la acción

        viewModel.detailMeals.observeForever { meal ->
            if (meal != null) {
                capturedMeals.add(meal)
            }
        }
    }

    @After
    fun tearDown() {
        // Limpiar observer después de cada test
        capturedMeals.clear()
    }

    @Test
    fun `detailMealsQuery should update LiveData when successful`() = mainCoroutineRule.runTest {
        // 1. Configurar mock para función suspend
        val testMeal = Meal(
            id = "52772",
            name = "Teriyaki Chicken Casserole",
            thumbnail = "https://www.themealdb.com/images/media/meals/wvpsxx1468256321.jpg",
            instructions = "Preheat oven to 350° F. Spray a 9x13-inch baking pan with non-stick spray. Combine soy sauce, ½ cup water, brown sugar, ginger and garlic in a small saucepan and cover. Bring to a boil over medium heat. Remove lid and cook for one minute once boiling. Meanwhile, stir together the corn starch and 2 tablespoons of water in a separate dish until smooth. Once sauce is boiling, add mixture to the saucepan and stir to combine. Cook until the sauce starts to thicken then remove from heat. Place the chicken breasts in the prepared pan. Pour one cup of the sauce over top of chicken. Place chicken in oven and bake 35 minutes or until cooked through. Remove from oven and shred chicken in the dish using two forks. *Meanwhile, steam or cook the vegetables according to package directions. Add the cooked vegetables and rice to the casserole dish with the chicken. Add most of the remaining sauce, reserving a bit to drizzle over the top when serving. Gently toss everything together in the casserole dish until combined. Return to oven and cook 15 minutes. Remove from oven and let stand 5 minutes before serving. Drizzle each serving with remaining sauce. Enjoy!",
            urlYoutube = "https://www.youtube.com/watch?v=a96CZ0cVmXg"
        )
        val testMeal2 = Meal(
            id = "52773",
            name = "Teriyaki Chicken Casserole",
            thumbnail = "https://www.themealdb.com/images/media/meals/wvpsxx1468256321.jpg",
            instructions = "Preheat oven to 350° F...",
            urlYoutube = "https://www.youtube.com/watch?v=a96CZ0cVmXg"
        )
        val mockResponse = MealResponse(listOf(testMeal, testMeal2))

        `when`(mockMealApiService.detailsMeals("52772")).thenReturn(Response.success(mockResponse))

        // 2. Ejecutar
        viewModel.detailMealsQuery("52772")

        // 3. Avanzar las corrutinas
        advanceUntilIdle()

        // 4. Verificar
        assertEquals(1, capturedMeals.size) // Debería haber 1 actualización
        assertEquals(testMeal, capturedMeals[0])
        assertEquals(testMeal, viewModel.detailMeals.value)
        assertEquals(false, viewModel.loading.value)
    }
}


// Regla para corrutinas
class MainCoroutineRule : TestWatcher() {
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }

    fun runTest(block: suspend TestScope.() -> Unit) = testScope.runTest {
        block()
    }
}