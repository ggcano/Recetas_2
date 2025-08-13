package com.example.recetas_2

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.recetas_2.data.ApiServiceImpl
import com.example.recetas_2.data.MealApiService
import com.example.recetas_2.data.model.Meal
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MealServiceTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    lateinit var mockMealApiService: MealApiService

    private lateinit var apiServiceImpl: ApiServiceImpl

    @Before
    fun setup() {
        apiServiceImpl = ApiServiceImpl(
            apiService = mockMealApiService
        )
    }

    @After
    fun tearDown() {
        // Limpiar recursos si es necesario
        // Por ejemplo, cerrar conexiones, reiniciar mocks, etc.
    }

    @Test
    fun `meal model should have correct properties`() {
        // Given - Crear un meal de prueba
        val testMeal = Meal(
            id = "52772",
            name = "Teriyaki Chicken Casserole",
            thumbnail = "https://www.themealdb.com/images/media/meals/wvpsxx1468256321.jpg",
            instructions = "Preheat oven to 350° F...",
            urlYoutube = "https://www.youtube.com/watch?v=4aZr5hZXP_s"
        )

        // When - No hay acciones ya que solo probamos el modelo

        // Then - Verificar las propiedades
        assertEquals("52772", testMeal.id)
        assertEquals("Teriyaki Chicken Casserole", testMeal.name)
        assertEquals(
            "https://www.themealdb.com/images/media/meals/wvpsxx1468256321.jpg",
            testMeal.thumbnail
        )
        assertNotNull(testMeal.instructions)
        assertTrue(testMeal.instructions!!.startsWith("Preheat oven"))
        assertEquals(
            "https://www.youtube.com/watch?v=4aZr5hZXP_s",
            testMeal.urlYoutube
        )
    }

    @Test
    fun `meal model should handle null values correctly`() {
        // Given - Crear un meal con valores nulos
        val testMeal = Meal(
            id = "52773",
            name = "Null Test Meal",
            thumbnail = "https://test.com/image.jpg",
            instructions = null,
            urlYoutube = null
        )

        // When - No hay acciones ya que solo probamos el modelo

        // Then - Verificar el manejo de nulos
        assertNull(testMeal.instructions)
        assertNull(testMeal.urlYoutube)
    }

    @Test
    fun `meal model equals should work correctly`() {
        // Given - Crear dos meals idénticos y uno diferente
        val meal1 = Meal(
            id = "1",
            name = "Meal 1",
            thumbnail = "url1",
            instructions = "inst1",
            urlYoutube = "yt1"
        )

        val meal2 = Meal(
            id = "1",
            name = "Meal 1",
            thumbnail = "url1",
            instructions = "inst1",
            urlYoutube = "yt1"
        )

        val meal3 = Meal(
            id = "2",
            name = "Meal 2",
            thumbnail = "url2",
            instructions = "inst2",
            urlYoutube = "yt2"
        )

        // Then - Verificar igualdad y desigualdad
        assertEquals(meal1, meal2)
        assertNotEquals(meal1, meal3)
        assertEquals(meal1.hashCode(), meal2.hashCode())
        assertNotEquals(meal1.hashCode(), meal3.hashCode())
    }

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
}
