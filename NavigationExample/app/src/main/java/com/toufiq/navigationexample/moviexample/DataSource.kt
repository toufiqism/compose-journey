package com.toufiq.navigationexample.moviexample

import com.toufiq.navigationexample.R

class DataSource {
    fun generateMovieList(): List<Movie> {
        return listOf(
            Movie(
                id = 1,
                name = "Avengers",
                category = "Superhero",
                image = R.drawable.avengers
            ),
            Movie(
                id = 2,
                name = "Captain America",
                category = "Superhero",
                image = R.drawable.captainamerica
            ),
            Movie(
                id = 3,
                name = "Captain Marvel",
                category = "Superhero",
                image = R.drawable.captainmarvel
            ),
            Movie(
                id = 4,
                name = "Dr. Strange",
                category = "Superhero",
                image = R.drawable.drstrange
            ),
            Movie(
                id = 5,
                name = "Iron Man",
                category = "Superhero",
                image = R.drawable.ironman
            ),
            Movie(
                id = 6,
                name = "Spider-Man",
                category = "Superhero",
                image = R.drawable.spiderman
            ),
            Movie(
                id = 7,
                name = "Thor",
                category = "Superhero",
                image = R.drawable.thor
            )
        )
    }
}