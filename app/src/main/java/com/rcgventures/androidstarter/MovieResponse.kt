package com.rcgventures.androidstarter

import kotlinx.serialization.Serializable

@Serializable
class MovieResponse(val movies: List<Movie>? = null)